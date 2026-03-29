package br.com.pedroveras.courses_api.modules.course.useCases;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.pedroveras.courses_api.exceptions.CourseFoundException;
import br.com.pedroveras.courses_api.modules.course.CourseEntity;
import br.com.pedroveras.courses_api.modules.course.CourseRepository;
import br.com.pedroveras.courses_api.modules.course.CourseStatus;
import br.com.pedroveras.courses_api.modules.course.application.commands.CreateCourseCommand;
import br.com.pedroveras.courses_api.modules.course.application.ports.in.CreateCourseInputPort;

@Service
public class CreateCourseUseCase implements CreateCourseInputPort {
    
    private final CourseRepository courseRepository;

    public CreateCourseUseCase(CourseRepository courseRepository) {
      this.courseRepository = courseRepository;
    }

    @Override
    public UUID execute(CreateCourseCommand createCourseCommand) {
      this.courseRepository.findByName(createCourseCommand.name()).ifPresent(course -> {
        throw new CourseFoundException();
      });

      CourseEntity course = CourseEntity.builder()
        .name(createCourseCommand.name())
        .description(createCourseCommand.description())
        .category(createCourseCommand.category())
        .active(Boolean.TRUE.equals(createCourseCommand.active()) ? CourseStatus.ACTIVE : CourseStatus.INACTIVE)
        .build();

      CourseEntity savedCourse = this.courseRepository.save(course);
      return savedCourse.getId();
    }
}
