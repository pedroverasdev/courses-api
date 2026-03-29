package br.com.pedroveras.courses_api.modules.course.useCases;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.pedroveras.courses_api.modules.course.CourseEntity;
import br.com.pedroveras.courses_api.modules.course.CourseRepository;
import br.com.pedroveras.courses_api.modules.course.application.ports.in.ListCoursesInputPort;

@Service
public class ListCoursesUseCase implements ListCoursesInputPort {
    private final CourseRepository courseRepository;

    public ListCoursesUseCase(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public List<CourseEntity> execute(){
        List<CourseEntity> courses = this.courseRepository.findAll();

        return courses.stream()
            .sorted(Comparator.comparing(CourseEntity::getCreatedAt))
            .toList();
    }
}
