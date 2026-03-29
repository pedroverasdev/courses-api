package br.com.pedroveras.courses_api.modules.course.application.ports.in;

import java.util.UUID;

import br.com.pedroveras.courses_api.modules.course.application.commands.UpdateCourseCommand;

public interface UpdateCourseInputPort {
    void execute(UUID courseId, UpdateCourseCommand updateCourseCommand);
}
