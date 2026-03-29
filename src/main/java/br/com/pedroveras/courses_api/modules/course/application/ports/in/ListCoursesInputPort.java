package br.com.pedroveras.courses_api.modules.course.application.ports.in;

import java.util.List;

import br.com.pedroveras.courses_api.modules.course.CourseEntity;

public interface ListCoursesInputPort {
    List<CourseEntity> execute();
}
