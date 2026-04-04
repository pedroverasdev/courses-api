package br.com.pedroveras.courses_api.modules.course.application.ports.in;

import java.util.UUID;

import br.com.pedroveras.courses_api.modules.course.application.domain.CreateCourseCommand;

public interface CreateCourseInputPort {
    UUID execute(CreateCourseCommand createCourseCommand);
}
