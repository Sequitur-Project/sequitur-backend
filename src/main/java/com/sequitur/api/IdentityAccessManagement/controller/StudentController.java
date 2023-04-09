package com.sequitur.api.IdentityAccessManagement.controller;

import com.sequitur.api.IdentityAccessManagement.domain.model.Student;
import com.sequitur.api.IdentityAccessManagement.domain.service.StudentService;
import com.sequitur.api.IdentityAccessManagement.resource.SaveStudentResource;
import com.sequitur.api.IdentityAccessManagement.resource.StudentResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "students", description = "Students API")
@RestController
@RequestMapping("/api")
public class StudentController {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private StudentService studentService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Students returned", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/universities/{universityId}/students")
    public Page<StudentResource> getAllStudentsByUniversityId(
            @PathVariable(name = "universityId") Long universityId,
            Pageable pageable) {
        Page<Student> studentPage = studentService.getAllStudentsByUniversityId(universityId, pageable);
        List<StudentResource> resources = studentPage.getContent().stream().map(this::convertToResource).collect(Collectors.toList());
        return new PageImpl<StudentResource>(resources, pageable, resources.size());
    }

    @GetMapping("/universities/{universityId}/students/{studentId}")
    public StudentResource getStudentByIdAndUniversityId(@PathVariable(name = "universityId") Long universityId,
                                             @PathVariable(name = "studentId") Long studentId) {
        return convertToResource(studentService.getStudentByIdAndUniversityId(universityId, studentId));
    }

    @PostMapping("/universities/{universityId}/students")
    public StudentResource createStudent(@PathVariable(name = "universityId") Long universityId,
                                   @Valid @RequestBody SaveStudentResource resource) {
        return convertToResource(studentService.createStudent(universityId, convertToEntity(resource)));

    }

    @PutMapping("/universities/{universityId}/students/{studentId}")
    public StudentResource updateStudent(@PathVariable(name = "universityId") Long universityId,
                                   @PathVariable(name = "studentId") Long studentId,
                                   @Valid @RequestBody SaveStudentResource resource) {
        return convertToResource(studentService.updateStudent(universityId, studentId, convertToEntity(resource)));
    }

    @DeleteMapping("/universities/{universityId}/students/{studentId}")
    public ResponseEntity<?> deleteStudent(@PathVariable(name = "universityId") Long universityId,
                                        @PathVariable(name = "studentId") Long studentId) {
        return studentService.deleteStudent(universityId, studentId);
    }

    @Operation(summary = "Get Students", description = "Get All Students by Pages", tags = { "students" })
    @GetMapping("/students")
    public Page<StudentResource> getAllStudents(
            @Parameter(description="Pageable Parameter")
            Pageable pageable) {
        Page<Student> studentsPage = studentService.getAllStudents(pageable);
        List<StudentResource> resources = studentsPage.getContent().stream().map(this::convertToResource).collect(Collectors.toList());

        return new PageImpl<StudentResource>(resources,pageable , resources.size());
    }

    private Student convertToEntity(SaveStudentResource resource) {
        return mapper.map(resource, Student.class);
    }

    private StudentResource convertToResource(Student entity) {
        return mapper.map(entity, StudentResource.class);
    }

}
