package br.com.pedroveras.courses_api.modules.course;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import br.com.pedroveras.courses_api.modules.course.application.useCases.DeleteCourseUseCase;

@ExtendWith(MockitoExtension.class)
public class DeleteCourseUseCaseTest {

    @InjectMocks
    private DeleteCourseUseCase deleteCourseUseCase;

    @Mock
    private CourseRepository courseRepository;

    @Test
    @DisplayName("Should throw when course is not found and not delete anything")
    public void should_throw_when_course_is_not_found() {
        var idCourse = UUID.randomUUID();
        when(courseRepository.findById(idCourse)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deleteCourseUseCase.execute(idCourse))
            .isInstanceOf(CourseNotFoundException.class);

        verify(courseRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should delete course when it exists")
    public void should_delete_course_when_it_exists() {
        var idCourse = UUID.randomUUID();
        var course = new CourseEntity();
        course.setId(idCourse);
        when(courseRepository.findById(idCourse)).thenReturn(Optional.of(course));

        deleteCourseUseCase.execute(idCourse);

        verify(courseRepository).findById(idCourse);
        verify(courseRepository).delete(course);
    }
}
