package br.com.pedroveras.courses_api.modules.course.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCourseDTO {
    @NotBlank(message = "O nome é obrigatório")
    private String name;

    @NotBlank(message = "A descrição é obrigatória")
    @Length(min = 10, max = 100, message = "A descrição deve conter entre (10) e (200) caracteres")
    private String description;

    @NotBlank(message = "A categoria é obrigatória")
    private String category;
    
    private Boolean active;
}
