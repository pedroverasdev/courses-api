package br.com.pedroveras.courses_api.modules.course.application.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.pedroveras.courses_api.modules.course.CourseStatus;
import br.com.pedroveras.courses_api.modules.course.application.commands.UpdateCourseCommand;
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

  public void apply(UpdateCourseCommand command) {
    if (command.name() != null) {
      this.name = command.name();
    }
    if (command.description() != null) {
      this.description = command.description();
    }
    if (command.category() != null) {
      this.category = command.category();
    }
  }
}
