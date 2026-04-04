package br.com.pedroveras.courses_api.modules.course;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.pedroveras.courses_api.exceptions.CourseFoundException;
import br.com.pedroveras.courses_api.exceptions.CourseNotFoundException;
import br.com.pedroveras.courses_api.modules.course.application.domain.CourseEntity;
import br.com.pedroveras.courses_api.modules.course.application.domain.CourseStatus;
import br.com.pedroveras.courses_api.modules.course.application.domain.UpdateCourseCommand;
import br.com.pedroveras.courses_api.modules.course.application.useCases.UpdateCourseUseCase;

@ExtendWith(MockitoExtension.class)
public class UpdateCourseUseCaseTest {

    @InjectMocks
    private UpdateCourseUseCase updateCourseUseCase;

    @Mock
    private CourseRepository courseRepository;

    @Test
    @DisplayName("Should throw when course is not found and not update anything")
    public void should_throw_when_course_is_not_found() {
        var idCourse = UUID.randomUUID();
        var updatedCourse = new UpdateCourseCommand(
            "Angular",
            "Curso de Angular da Rocketseat",
            "Frontend"
        );

        when(courseRepository.findById(idCourse)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> updateCourseUseCase.execute(
            idCourse,
            updatedCourse
        )).isInstanceOf(CourseNotFoundException.class);

        verify(courseRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw when updating with a name that already exists")
    void should_throw_when_name_already_exists_for_another_course() {
        var idCourse = UUID.randomUUID();
        var otherId = UUID.randomUUID();

        var existingCourse = aCourse(idCourse);
        var otherCourse = aCourse(otherId);

        when(courseRepository.findById(idCourse)).thenReturn(Optional.of(existingCourse));
        when(courseRepository.findByName("React")).thenReturn(Optional.of(otherCourse));

        var command = new UpdateCourseCommand(
            "React",
            "Nova descrição",
            "Frontend"
        );

        assertThatThrownBy(() -> updateCourseUseCase.execute(idCourse, command))
            .isInstanceOf(CourseFoundException.class);

        verify(courseRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update course when it exists")
    public void should_update_course_when_it_exists() {
        var idCourse = UUID.randomUUID();
        var course = aCourse(idCourse);
        when(courseRepository.findById(idCourse)).thenReturn(Optional.of(course));

        var updateCourse = new UpdateCourseCommand(
            "React",
            "Curso de React da Rocketseat",
            "Frontend"
        );
        
        updateCourseUseCase.execute(idCourse, updateCourse);

        assertThat(course.getName()).isEqualTo(updateCourse.name());
        assertThat(course.getDescription()).isEqualTo(updateCourse.description());
        verify(courseRepository).save(course);
    }

    @Test
    @DisplayName("Should allow update when name belongs to same course")
    void should_allow_same_name_for_same_course() {
        var idCourse = UUID.randomUUID();
        var course = aCourse(idCourse);

        when(courseRepository.findById(idCourse)).thenReturn(Optional.of(course));
        when(courseRepository.findByName("Angular")).thenReturn(Optional.of(course));

        var command = new UpdateCourseCommand(
            "Angular",
            "Nova descrição",
            "Frontend"
        );

        updateCourseUseCase.execute(idCourse, command);

        assertThat(course.getDescription()).isEqualTo(command.description());
        verify(courseRepository).save(course);
    }

    @Test
    @DisplayName("Should update course without validating name when name is null")
    void should_update_without_name_validation_when_name_is_null() {
        var idCourse = UUID.randomUUID();
        var course = aCourse(idCourse);

        when(courseRepository.findById(idCourse)).thenReturn(Optional.of(course));

        var command = new UpdateCourseCommand(
            null,
            "Nova descrição",
            "Backend"
        );

        updateCourseUseCase.execute(idCourse, command);

        assertThat(course.getDescription()).isEqualTo(command.description());
        verify(courseRepository, never()).findByName(any());
        verify(courseRepository).save(course);
    }

    public static CourseEntity aCourse(UUID id) {
        return CourseEntity.builder()
            .id(id)
            .name("Angular")
            .description("Curso de Angular da Rocketseat")
            .category("Frontend")
            .active(CourseStatus.ACTIVE)
            .createdAt(LocalDateTime.now())
            .build();
    }
}
