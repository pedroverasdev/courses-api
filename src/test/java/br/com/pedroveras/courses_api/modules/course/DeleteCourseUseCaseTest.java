package br.com.pedroveras.courses_api.modules.course;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import br.com.pedroveras.courses_api.modules.course.useCases.DeleteCourseUseCase;

@ExtendWith(MockitoExtension.class)
public class DeleteCourseUseCaseTest {
    @InjectMocks
    private DeleteCourseUseCase deleteCourseUseCase;

    @Mock
    private CourseRepository courseRepository;

    @Test
    @DisplayName("Should not be possible to delete a course if course does not found")
    public void should_not_be_delete_course_with_course_not_found() {
        when(courseRepository.findById(null)).thenReturn(Optional.empty());
        assertThrows(CourseNotFoundException.class, () -> deleteCourseUseCase.execute(null));
    }

    @Test
    public void should_not_be_able_to_delete_course_with_course_not_found(){
        var idCourse = UUID.randomUUID();
        when(courseRepository.findById(idCourse)).thenReturn(Optional.empty());
        assertThrows(CourseNotFoundException.class, () -> deleteCourseUseCase.execute(idCourse));
    }

    @Test
    @DisplayName("Should delete course when it exists")
    public void should_be_able_to_delete_course_with_course_found() {
        var idCourse = UUID.randomUUID();
        var course = new CourseEntity();
        course.setId(idCourse);
        when(courseRepository.findById(idCourse)).thenReturn(Optional.of(course));

        deleteCourseUseCase.execute(idCourse);

        verify(courseRepository).delete(course);
    }
}
