package br.com.pedroveras.courses_api.modules.course.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseIdDTO {
    private UUID id;
}
