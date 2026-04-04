package br.com.pedroveras.courses_api.modules.course.dto;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateCourseDTO {
    private String name;

    @Length(min = 10, max = 100, message = "A descrição deve conter entre (10) e (200) caracteres")
    private String description;

    private String category;
}
