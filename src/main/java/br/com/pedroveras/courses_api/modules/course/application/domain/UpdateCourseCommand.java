package br.com.pedroveras.courses_api.modules.course.application.commands;

public record UpdateCourseCommand(
    String name,
    String description,
    String category
) {}
