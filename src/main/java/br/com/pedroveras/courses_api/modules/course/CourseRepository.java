package br.com.pedroveras.courses_api.modules.course;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseRepository {
    Optional<CourseEntity> findByName(String name);
    Optional<CourseEntity> findById(UUID id);
    List<CourseEntity> findAll();
    CourseEntity save(CourseEntity course);
    void delete(CourseEntity course);
}
