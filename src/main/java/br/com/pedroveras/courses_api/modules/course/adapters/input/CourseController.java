package br.com.pedroveras.courses_api.modules.course.adapters.input;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pedroveras.courses_api.modules.course.CourseMapper;
import br.com.pedroveras.courses_api.modules.course.application.ports.in.CreateCourseInputPort;
import br.com.pedroveras.courses_api.modules.course.application.ports.in.DeleteCourseInputPort;
import br.com.pedroveras.courses_api.modules.course.application.ports.in.ListCoursesInputPort;
import br.com.pedroveras.courses_api.modules.course.application.ports.in.ToggleCourseActiveStatusInputPort;
import br.com.pedroveras.courses_api.modules.course.application.ports.in.UpdateCourseInputPort;
import br.com.pedroveras.courses_api.modules.course.dto.CreateCourseDTO;
import br.com.pedroveras.courses_api.modules.course.dto.CourseIdDTO;
import br.com.pedroveras.courses_api.modules.course.dto.ListCourseDTO;
import br.com.pedroveras.courses_api.modules.course.dto.UpdateCourseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/courses")
@Tag(name = "Courses", description = "Informações dos cursos")
@ApiResponses({
    @ApiResponse(responseCode = "200", content = {
        @Content(schema = @Schema(implementation = ListCourseDTO.class))
    })
})
public class CourseController {
    private final CreateCourseInputPort createCourseUseCase;
    private final ListCoursesInputPort listCoursesUseCase;
    private final UpdateCourseInputPort updateCourseUseCase;
    private final ToggleCourseActiveStatusInputPort toggleCourseActiveStatusUseCase;
    private final DeleteCourseInputPort deleteCourseUseCase;
    private final CourseMapper courseMapper;

    public CourseController(
        CreateCourseInputPort createCourseUseCase,
        ListCoursesInputPort listCoursesUseCase,
        UpdateCourseInputPort updateCourseUseCase,
        ToggleCourseActiveStatusInputPort toggleCourseActiveStatusUseCase,
        DeleteCourseInputPort deleteCourseUseCase,
        CourseMapper courseMapper
    ) {
        this.createCourseUseCase = createCourseUseCase;
        this.listCoursesUseCase = listCoursesUseCase;
        this.updateCourseUseCase = updateCourseUseCase;
        this.toggleCourseActiveStatusUseCase = toggleCourseActiveStatusUseCase;
        this.deleteCourseUseCase = deleteCourseUseCase;
        this.courseMapper = courseMapper;
    }
    
    @PostMapping("/")
    @Operation(summary = "Cadastro de curso", description = "Essa função é responsável por cadastrar um curso")
    public ResponseEntity<Object> create(@Valid @RequestBody CreateCourseDTO createCourseDTO) {
        UUID result = createCourseUseCase.execute(courseMapper.toCreateCommand(createCourseDTO));
        return ResponseEntity.ok().body(new CourseIdDTO(result));
    }

    @GetMapping("/list")
    @Operation(summary = "Listagem de cursos", description = "Essa função é responsável por listar os cursos")
    public ResponseEntity<Object> listAll(){
        var result = this.listCoursesUseCase.execute().stream()
            .map(courseMapper::toListCourseDTO)
            .toList();
        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar curso", description = "Essa função é responsável por editar um curso")
    public ResponseEntity<Object> updateCourse(
        @PathVariable UUID id,
        @Valid @RequestBody UpdateCourseDTO updateCourseDTO
    ) {
        this.updateCourseUseCase.execute(id, courseMapper.toUpdateCommand(updateCourseDTO));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/active")
    @Operation(summary = "Editar status de curso", description = "Essa função é responsável por editar o status de um curso")
    public ResponseEntity<Object> toggleActiveCourse(@PathVariable UUID id) {
        this.toggleCourseActiveStatusUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar curso", description = "Essa função é responsável por deletar um curso")
    public ResponseEntity<Object> deleteCourse(
        @PathVariable UUID id
    ) {
        this.deleteCourseUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}