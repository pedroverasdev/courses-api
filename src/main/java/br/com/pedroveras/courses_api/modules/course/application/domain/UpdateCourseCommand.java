package br.com.pedroveras.courses_api.modules.course.application.domain;

public record UpdateCourseCommand(
    String name,
    String description,
    String category
) {}
