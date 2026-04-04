package br.com.pedroveras.courses_api.modules.course.application.domain;

public enum CourseStatus {
    ACTIVE, INACTIVE;

    public CourseStatus toggle() {
        return this == ACTIVE ? INACTIVE : ACTIVE;
    }
}