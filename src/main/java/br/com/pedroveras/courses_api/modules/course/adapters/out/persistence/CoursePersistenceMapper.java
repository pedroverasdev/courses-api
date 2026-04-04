package br.com.pedroveras.courses_api.modules.course.adapters.out.persistence;

import org.springframework.stereotype.Component;

import br.com.pedroveras.courses_api.modules.course.application.domain.CourseEntity;
import br.com.pedroveras.courses_api.modules.course.adapters.out.database.CourseJPAEntity;

@Component
public class CoursePersistenceMapper {
    public CourseEntity toDomain(CourseJPAEntity entity) {
        return CourseEntity.builder()
            .id(entity.getId())
            .name(entity.getName())
            .description(entity.getDescription())
            .category(entity.getCategory())
            .active(entity.getActive())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    public CourseJPAEntity toJpa(CourseEntity entity) {
        return CourseJPAEntity.builder()
            .id(entity.getId())
            .name(entity.getName())
            .description(entity.getDescription())
            .category(entity.getCategory())
            .active(entity.getActive())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
}
