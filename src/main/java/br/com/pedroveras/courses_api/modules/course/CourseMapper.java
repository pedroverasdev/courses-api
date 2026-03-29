package br.com.pedroveras.courses_api.modules.course;

import org.springframework.stereotype.Component;

import br.com.pedroveras.courses_api.modules.course.application.commands.CreateCourseCommand;
import br.com.pedroveras.courses_api.modules.course.application.commands.UpdateCourseCommand;
import br.com.pedroveras.courses_api.modules.course.dto.CreateCourseDTO;
import br.com.pedroveras.courses_api.modules.course.dto.ListCourseDTO;
import br.com.pedroveras.courses_api.modules.course.dto.UpdateCourseDTO;

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