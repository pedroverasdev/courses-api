package br.com.pedroveras.courses_api.modules.course.useCases;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.pedroveras.courses_api.exceptions.CourseNotFoundException;
import br.com.pedroveras.courses_api.modules.course.CourseEntity;
import br.com.pedroveras.courses_api.modules.course.CourseRepository;
import br.com.pedroveras.courses_api.modules.course.application.ports.in.DeleteCourseInputPort;

@Service
public class DeleteCourseUseCase implements DeleteCourseInputPort {
    private final CourseRepository courseRepository;

    public DeleteCourseUseCase(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public void execute(UUID courseId) {
        CourseEntity course = courseRepository.findById(courseId)
            .orElseThrow(CourseNotFoundException::new);

        this.courseRepository.delete(course);
    }
}
