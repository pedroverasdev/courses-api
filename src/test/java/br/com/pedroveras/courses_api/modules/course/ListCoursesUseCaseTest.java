package br.com.pedroveras.courses_api.modules.course;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.pedroveras.courses_api.modules.course.application.domain.CourseEntity;
import br.com.pedroveras.courses_api.modules.course.application.useCases.ListCoursesUseCase;

@ExtendWith(MockitoExtension.class)
public class ListCoursesUseCaseTest {

  @Mock
  private CourseRepository courseRepository;

  @InjectMocks
  private ListCoursesUseCase listCoursesUseCase;

  @Test
  @DisplayName("Should return empty list when there are no courses")
  void should_return_empty_list_when_repository_has_no_courses() {
    when(courseRepository.findAll()).thenReturn(List.of());

    var result = listCoursesUseCase.execute();

    assertThat(result).isEmpty();
    verify(courseRepository).findAll();
  }

  @Test
  @DisplayName("Should return all courses sorted by createdAt ascending")
  void should_return_courses_sorted_by_created_at() {
    var later = LocalDateTime.of(2025, 6, 1, 10, 0);
    var earlier = LocalDateTime.of(2025, 3, 1, 10, 0);

    var courseNewer = CourseEntity.builder()
        .id(UUID.randomUUID())
        .name("Curso B")
        .description("Descrição com mais de dez caracteres")
        .category("Backend")
        .createdAt(later)
        .build();

    var courseOlder = CourseEntity.builder()
        .id(UUID.randomUUID())
        .name("Curso A")
        .description("Outra descrição com tamanho suficiente")
        .category("Frontend")
        .createdAt(earlier)
        .build();

    when(courseRepository.findAll()).thenReturn(List.of(courseNewer, courseOlder));

    var result = listCoursesUseCase.execute();

    assertThat(result)
        .hasSize(2)
        .extracting(CourseEntity::getCreatedAt)
        .containsExactly(earlier, later);
    verify(courseRepository).findAll();
  }
}
