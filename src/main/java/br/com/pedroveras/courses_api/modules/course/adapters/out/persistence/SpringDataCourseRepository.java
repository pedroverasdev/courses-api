package br.com.pedroveras.courses_api.modules.course.adapters.out.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.pedroveras.courses_api.modules.course.adapters.out.database.CourseJPAEntity;


public interface SpringDataCourseRepository extends JpaRepository<CourseJPAEntity, UUID> {
    Optional<CourseJPAEntity> findByName(String name);
}
