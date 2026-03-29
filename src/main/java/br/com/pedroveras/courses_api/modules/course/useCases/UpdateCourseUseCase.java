package br.com.pedroveras.courses_api.modules.course.useCases;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.pedroveras.courses_api.exceptions.CourseNotFoundException;
import br.com.pedroveras.courses_api.modules.course.CourseEntity;
import br.com.pedroveras.courses_api.modules.course.CourseRepository;
import br.com.pedroveras.courses_api.modules.course.application.commands.UpdateCourseCommand;
import br.com.pedroveras.courses_api.modules.course.application.ports.in.UpdateCourseInputPort;

@Service
public class UpdateCourseUseCase implements UpdateCourseInputPort {
    private final CourseRepository courseRepository;

    public UpdateCourseUseCase(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public void execute(UUID courseId, UpdateCourseCommand updateCourseCommand) {
        CourseEntity course = courseRepository.findById(courseId)
            .orElseThrow(CourseNotFoundException::new);

        if (updateCourseCommand.name() != null) {
            course.setName(updateCourseCommand.name());
        }

        if (updateCourseCommand.description() != null) {
            course.setDescription(updateCourseCommand.description());
        }

        if (updateCourseCommand.category() != null) {
            course.setCategory(updateCourseCommand.category());
        }

        this.courseRepository.save(course);
    }
}
