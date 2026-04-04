package br.com.pedroveras.courses_api.modules.course;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import br.com.pedroveras.courses_api.exceptions.CourseNotFoundException;
import br.com.pedroveras.courses_api.modules.course.application.domain.CourseEntity;
import br.com.pedroveras.courses_api.modules.course.application.domain.CourseStatus;
import br.com.pedroveras.courses_api.modules.course.application.useCases.ToggleCourseActiveStatusUseCase;

@ExtendWith(MockitoExtension.class)
public class ToggleCourseActiveStatusUseCaseTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private ToggleCourseActiveStatusUseCase toggleCourseActiveStatusUseCase;

    @Test
    @DisplayName("Should throw when course is not found and not persist")
    public void should_throw_when_course_is_not_found() {
        UUID idCourse = UUID.randomUUID();

        when(courseRepository.findById(idCourse)).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> toggleCourseActiveStatusUseCase.execute(idCourse));

        verify(courseRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should toggle ACTIVE course to INACTIVE and persist")
    public void should_toggle_active_course_to_inactive_and_persist() {
        UUID idCourse = UUID.randomUUID();
        CourseEntity course = aCourse(idCourse, CourseStatus.ACTIVE);
        when(courseRepository.findById(idCourse)).thenReturn(Optional.of(course));

        toggleCourseActiveStatusUseCase.execute(idCourse);

        assertThat(course.getActive()).isEqualTo(CourseStatus.INACTIVE);
        verify(courseRepository).save(course);
    }

    @Test
    @DisplayName("Should toggle INACTIVE course to ACTIVE and persist")
    public void should_toggle_inactive_course_to_active_and_persist() {
        UUID idCourse = UUID.randomUUID();
        CourseEntity course = aCourse(idCourse, CourseStatus.INACTIVE);
        when(courseRepository.findById(idCourse)).thenReturn(Optional.of(course));

        toggleCourseActiveStatusUseCase.execute(idCourse);

        assertThat(course.getActive()).isEqualTo(CourseStatus.ACTIVE);
        verify(courseRepository).save(course);
    }

    private static CourseEntity aCourse(UUID id, CourseStatus active) {
        return CourseEntity.builder()
            .id(id)
            .name("Angular")
            .description("Curso de Angular da Rocketseat")
            .category("Frontend")
            .active(active)
            .createdAt(LocalDateTime.now())
            .build();
    }
}
