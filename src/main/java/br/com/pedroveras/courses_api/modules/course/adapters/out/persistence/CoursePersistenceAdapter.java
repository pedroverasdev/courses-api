package br.com.pedroveras.courses_api.modules.course.adapters.out.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import br.com.pedroveras.courses_api.modules.course.CourseRepository;
import br.com.pedroveras.courses_api.modules.course.application.domain.CourseEntity;

@Component
public class CoursePersistenceAdapter implements CourseRepository {
    private final SpringDataCourseRepository springDataCourseRepository;
    private final CoursePersistenceMapper mapper;

    public CoursePersistenceAdapter(
        SpringDataCourseRepository springDataCourseRepository,
        CoursePersistenceMapper mapper
    ) {
        this.springDataCourseRepository = springDataCourseRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<CourseEntity> findByName(String name) {
        return springDataCourseRepository.findByName(name).map(mapper::toDomain);
    }

    @Override
    public Optional<CourseEntity> findById(UUID id) {
        return springDataCourseRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<CourseEntity> findAll() {
        return springDataCourseRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public CourseEntity save(CourseEntity course) {
        return mapper.toDomain(springDataCourseRepository.save(mapper.toJpa(course)));
    }

    @Override
    public void delete(CourseEntity course) {
        springDataCourseRepository.delete(mapper.toJpa(course));
    }
}
