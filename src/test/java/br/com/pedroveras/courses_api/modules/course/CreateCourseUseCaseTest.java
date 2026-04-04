package br.com.pedroveras.courses_api.modules.course;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import br.com.pedroveras.courses_api.exceptions.CourseFoundException;
import br.com.pedroveras.courses_api.modules.course.application.commands.CreateCourseCommand;
import br.com.pedroveras.courses_api.modules.course.application.useCases.CreateCourseUseCase;

@ExtendWith(MockitoExtension.class)
public class CreateCourseUseCaseTest {
  @Mock
  private CourseRepository courseRepository;

  @InjectMocks
  private CreateCourseUseCase createCourseUseCase;

  @Test
  @DisplayName("Should not be possible to create a course if course already exists.")
  public void should_not_be_create_course_with_course_already_exists() {
    var courseName = "Curso já cadastrado";
    var existingCourse = CourseEntity.builder()
        .id(UUID.randomUUID())
        .name(courseName)
        .description("Descrição existente com mais de 10 caracteres")
        .category("Backend")
        .build();

    when(courseRepository.findByName(courseName)).thenReturn(Optional.of(existingCourse));

    var courseCommand = new CreateCourseCommand(
      courseName,
      "Outra descrição com tamanho suficiente",
      "Backend",
      true
    );

    assertThrows(CourseFoundException.class, () -> createCourseUseCase.execute(courseCommand));

    verify(courseRepository, never()).save(any());
  }

  @Test
  @DisplayName("Should be possible to create a course if course does not exist.")
  public void should_be_able_to_create_course_with_course_not_found() {
    var courseName = "Curso já cadastrado";
    var course = new CourseEntity();
    course.setId(UUID.randomUUID());
    course.setName(courseName);
    course.setDescription("Descrição existente com mais de 10 caracteres");
    course.setCategory("Backend");
    when(courseRepository.findByName(courseName)).thenReturn(Optional.empty());
    when(courseRepository.save(any())).thenReturn(course);

    var courseCommand = new CreateCourseCommand(
      courseName,
      "Descrição existente com mais de 10 caracteres",
      "Backend",
      true
    );

    var result = createCourseUseCase.execute(courseCommand);
    assertThat(result).isNotNull();
    assertThat(result).isInstanceOf(UUID.class);
    verify(courseRepository).save(any());
  }
}
