package com.sequitur.api.Subscriptions.controller;

import com.sequitur.api.Subscriptions.domain.model.Subscription;
import com.sequitur.api.Subscriptions.domain.service.SubscriptionService;
import com.sequitur.api.Subscriptions.resource.SaveSubscriptionResource;
import com.sequitur.api.Subscriptions.resource.SubscriptionResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
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

@RestController
@RequestMapping("/api")
@Tag(name = "subscriptions", description = "Subscriptions API")
public class SubscriptionController {
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private SubscriptionService subscriptionService;

    @Operation(summary = "Get Subscriptions", description = "Get All Subscriptions by Pages", tags = { "subscriptions" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Subscriptions returned", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/subscriptions")
    public Page<SubscriptionResource> getAllSubscriptions(Pageable pageable) {
        Page<Subscription> subscriptionsPage = subscriptionService.getAllSubscriptions(pageable);
        List<SubscriptionResource> resources = subscriptionsPage.getContent().stream().map(this::convertToResource).collect(Collectors.toList());

        return new PageImpl<>(resources, pageable, resources.size());
    }

    @Operation(summary = "Get Subscription by Id", description = "Get a Subscription by specifying Id", tags = { "subscriptions" })
    @GetMapping("/subscriptions/{id}")
    public SubscriptionResource getSubscriptionById(
            @Parameter(description="Subscription Id")
            @PathVariable(name = "id") Long subscriptionId) {
        return convertToResource(subscriptionService.getSubscriptionById(subscriptionId));
    }

    @PostMapping("/subscriptions")
    @Transactional
    public SubscriptionResource createSubscription(@Valid @RequestBody SaveSubscriptionResource resource)  {
        Subscription subscription = convertToEntity(resource);
        return convertToResource(subscriptionService.createSubscription(subscription));
    }

    @PutMapping("/subscriptions/{id}")
    public SubscriptionResource updateSubscription(@PathVariable(name = "id") Long subscriptionId, @Valid @RequestBody SaveSubscriptionResource resource) {
        Subscription subscription = convertToEntity(resource);
        return convertToResource(subscriptionService.updateSubscription(subscriptionId, subscription));
    }

    @DeleteMapping("/subscriptions/{id}")
    public ResponseEntity<?> deleteSubscription(@PathVariable(name = "id") Long subscriptionId) {
        return subscriptionService.deleteSubscription(subscriptionId);
    }
    // Auto Mapper
    private Subscription convertToEntity(SaveSubscriptionResource resource) {
        return mapper.map(resource, Subscription.class);
    }

    private SubscriptionResource convertToResource(Subscription entity) {
        return mapper.map(entity, SubscriptionResource.class);
    }
}
