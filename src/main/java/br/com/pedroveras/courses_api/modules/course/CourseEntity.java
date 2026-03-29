package br.com.pedroveras.courses_api.modules.course;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseEntity {
  private UUID id;

  private String name;

  private String description;

  private String category;

  private CourseStatus active;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  public void toggleStatus() {
    this.active = this.active.toggle();
  }
}
