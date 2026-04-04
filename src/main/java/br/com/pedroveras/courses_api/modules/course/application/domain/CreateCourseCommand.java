package br.com.pedroveras.courses_api.modules.course.application.domain;

public record CreateCourseCommand(
    String name,
    String description,
    String category,
    Boolean active
) {}
