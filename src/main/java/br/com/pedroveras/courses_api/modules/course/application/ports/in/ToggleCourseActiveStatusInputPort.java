package br.com.pedroveras.courses_api.modules.course.application.ports.in;

import java.util.UUID;

public interface ToggleCourseActiveStatusInputPort {
    void execute(UUID courseId);
}
