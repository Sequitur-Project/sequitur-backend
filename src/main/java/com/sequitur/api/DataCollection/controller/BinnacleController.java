package com.sequitur.api.DataCollection.controller;

import com.sequitur.api.DataCollection.domain.model.Binnacle;
import com.sequitur.api.DataCollection.domain.service.BinnacleService;
import com.sequitur.api.DataCollection.resource.BinnacleResource;
import com.sequitur.api.DataCollection.resource.SaveBinnacleResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@Tag(name = "binnacles", description = "Binnacles API")
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api")
public class BinnacleController {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private BinnacleService binnacleService;


    @GetMapping("/students/{studentId}/binnacles/{binnacleId}")
    public BinnacleResource getBinnacleByIdAndStudentId(@PathVariable(name = "studentId") Long studentId,
                                                            @PathVariable(name = "binnacleId") Long binnacleId) {
        return convertToResource(binnacleService.getBinnacleByIdAndStudentId(binnacleId,studentId));
    }
    @GetMapping("/students/{studentId}/binnacles")
    public BinnacleResource getBinnacleByStudentId(@PathVariable(name = "studentId") Long studentId) {
        return convertToResource(binnacleService.getByStudentId(studentId));
    }

    @PostMapping("/students/{studentId}/binnacles")
    public BinnacleResource createBinnacle(@PathVariable(name = "studentId") Long studentId,
                                                   @Valid @RequestBody SaveBinnacleResource resource) {
        return convertToResource(binnacleService.createBinnacle(studentId, convertToEntity(resource)));

    }

    @DeleteMapping("/students/{studentId}/binnacles/{binnacleId}")
    public ResponseEntity<?> deleteConversation(@PathVariable(name = "studentId") Long studentId,
                                                @PathVariable(name = "binnacleId") Long binnacleId) {
        return binnacleService.deleteBinnacle(studentId, binnacleId);
    }

    @Operation(summary = "Get Binnacles", description = "Get All Binnacles by Pages", tags = { "binnacles" })
    @GetMapping("/binnacles")
    public Page<BinnacleResource> getAllBinnacles(
            @Parameter(description="Pageable Parameter")
            Pageable pageable) {
        Page<Binnacle> binnaclesPage = binnacleService.getAllBinnacles(pageable);
        List<BinnacleResource> resources = binnaclesPage.getContent().stream().map(this::convertToResource).collect(Collectors.toList());

        return new PageImpl<BinnacleResource>(resources,pageable , resources.size());
    }

    private Binnacle convertToEntity(SaveBinnacleResource resource) {
        return mapper.map(resource, Binnacle.class);
    }

    private BinnacleResource convertToResource(Binnacle entity) {
        return mapper.map(entity, BinnacleResource.class);
    }
}
