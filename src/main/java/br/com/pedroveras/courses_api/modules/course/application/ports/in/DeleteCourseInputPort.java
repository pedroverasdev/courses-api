package br.com.pedroveras.courses_api.modules.course.application.ports.in;

import java.util.UUID;

public interface DeleteCourseInputPort {
    void execute(UUID courseId);
}
