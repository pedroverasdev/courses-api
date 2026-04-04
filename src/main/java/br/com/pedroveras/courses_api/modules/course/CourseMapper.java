package br.com.pedroveras.courses_api.modules.course;

import org.springframework.stereotype.Component;

import br.com.pedroveras.courses_api.modules.course.adapters.input.dto.CreateCourseDTO;
import br.com.pedroveras.courses_api.modules.course.adapters.input.dto.ListCourseDTO;
import br.com.pedroveras.courses_api.modules.course.adapters.input.dto.UpdateCourseDTO;
import br.com.pedroveras.courses_api.modules.course.application.domain.CourseEntity;
import br.com.pedroveras.courses_api.modules.course.application.domain.CourseStatus;
import br.com.pedroveras.courses_api.modules.course.application.domain.CreateCourseCommand;
import br.com.pedroveras.courses_api.modules.course.application.domain.UpdateCourseCommand;

@Component
public class CourseMapper {
    public CreateCourseCommand toCreateCommand(CreateCourseDTO dto) {
        return new CreateCourseCommand(
            dto.getName(),
            dto.getDescription(),
            dto.getCategory(),
            dto.getActive()
        );
    }

    public UpdateCourseCommand toUpdateCommand(UpdateCourseDTO dto) {
        return new UpdateCourseCommand(
            dto.getName(),
            dto.getDescription(),
            dto.getCategory()
        );
    }

    public ListCourseDTO toListCourseDTO(CourseEntity course) {
        return new ListCourseDTO(
            course.getId(),
            course.getName(),
            course.getDescription(),
            course.getCategory(),
            course.getActive() == CourseStatus.ACTIVE,
            course.getCreatedAt(),
            course.getUpdatedAt()
        );
    }
}