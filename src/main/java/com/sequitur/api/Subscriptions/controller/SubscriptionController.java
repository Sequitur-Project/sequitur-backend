package com.sequitur.api.Subscriptions.controller;

import com.sequitur.api.IdentityAccessManagement.domain.model.Manager;
import com.sequitur.api.IdentityAccessManagement.domain.repository.ManagerRepository;
import com.sequitur.api.IdentityAccessManagement.domain.service.ManagerService;
import com.sequitur.api.Subscriptions.domain.model.Payment;
import com.sequitur.api.Subscriptions.domain.model.Subscription;
import com.sequitur.api.Subscriptions.domain.repository.PaymentRepository;
import com.sequitur.api.Subscriptions.domain.service.SubscriptionService;
import com.sequitur.api.Subscriptions.resource.*;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.param.*;
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
    @Autowired
    private ManagerService managerService;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private PaymentRepository paymentRepository;

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

    @PostMapping("/managers/{managerId}/subscribe/{subscriptionId}")
    public ResponseEntity<?> subscribe(@PathVariable Long subscriptionId, @PathVariable Long managerId, @RequestBody PaymentMethodRequest paymentMethodRequest) {

        Manager manager = managerService.getManagerById(managerId);
        if (manager == null) {
            return ResponseEntity.badRequest().body("Invalid manager ID");
        }
        // Get the subscription from the database.
        Subscription subscription = subscriptionService.getSubscriptionById(subscriptionId);
        if (subscription == null) {
            return ResponseEntity.badRequest().body("Invalid subscription ID");
        }

        Stripe.apiKey = "sk_test_51Mnr5BCaixXEExr04hcKsVtTKSdGU7i4cO5zwnmXSMZyaPnSxBjCWh7RATWQFw7FKenQE71kvKrrjXzHaaZURmeb00I4OHSveL";

        // Create a Customer object with Stripe.
        CustomerCreateParams customerCreateParams = new CustomerCreateParams.Builder()
                .setEmail(manager.getEmail())
                .setName(manager.getFirstName()+" "+ manager.getLastName())
                .build();
        Customer customer;
        try {
            customer = Customer.create(customerCreateParams);
        } catch (StripeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error creating customer");
        }

        // Create a PaymentIntent with Stripe.
        PaymentIntentCreateParams params = new PaymentIntentCreateParams.Builder()
                .setAmount(Math.round(subscription.getPrice() * 100)) // Stripe accepts payment amount in cents, so we multiply by 100
                .setCurrency("usd")
                .setDescription("Subscripcion Mensual de Sequitur")
                .addPaymentMethodType("card")
                .setCustomer(customer.getId())
                .build();
        PaymentIntent paymentIntent;
        try {
            paymentIntent = PaymentIntent.create(params);
        } catch (StripeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error creating payment request");
        }

        // Create the PaymentMethod object with Stripe.
       PaymentMethodCreateParams.CardDetails card = new PaymentMethodCreateParams.CardDetails.Builder()
                .setNumber(paymentMethodRequest.getCardNumber())
                .setExpMonth((long) Integer.parseInt(paymentMethodRequest.getExpiryMonth()))
                .setExpYear((long) Integer.parseInt(paymentMethodRequest.getExpiryYear()))
                .setCvc(paymentMethodRequest.getCvc())
                .build();

        PaymentMethodCreateParams paymentMethodParams = PaymentMethodCreateParams.builder()
                .setType(PaymentMethodCreateParams.Type.CARD)
                .setCard(card)
                .build();
        PaymentMethod paymentMethod;
        try {
            paymentMethod = PaymentMethod.create(paymentMethodParams);
        } catch (StripeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error creating payment method");
        }
        try {
            paymentMethod.attach(PaymentMethodAttachParams.builder()
                    .setCustomer(customer.getId())
                    .build());
        } catch (StripeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error attaching payment method");
        }

        // Confirm the payment with the provided payment method.
        PaymentIntentConfirmParams confirmParams = new PaymentIntentConfirmParams.Builder()
                .setPaymentMethod(paymentMethod.getId())
                .build();

        try {
            paymentIntent.confirm(confirmParams);
        } catch (StripeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error confirming payment");
        }

        // Save the payment in the database.
        Payment payment = new Payment();
        payment.setAmount(paymentIntent.getAmount() / 100.0); // Convert amount back to dollars
        payment.setCurrency(paymentIntent.getCurrency());
        payment.setDescription("Subscripcion Mensual de Sequitur");
        payment.setPaymentId(paymentIntent.getId());
        payment.setManager(manager);
        payment.setSubscription(subscription);
        paymentRepository.save(payment);

        manager.setSubscription(subscription);
        // Set the manager's subscription status to true.
        manager.setSubscribed(true);
        managerRepository.save(manager);
        // Return the payment ID to the client.
        return ResponseEntity.ok(new PaymentResponse(paymentIntent.getClientSecret()));
    }


    /*@PostMapping("/confirm-payment")
    public ResponseEntity<?> confirmPayment(@RequestBody ConfirmPaymentRequest request) {
        // Get the payment from the database.
        Payment payment = paymentRepository.findByPaymentId(request.getPaymentId());
        if (payment == null) {
            return ResponseEntity.badRequest().body("Invalid payment ID");
        }

        // Verify the payment with Stripe.
        Stripe.apiKey = "sk_test_51Mnr5BCaixXEExr04hcKsVtTKSdGU7i4cO5zwnmXSMZyaPnSxBjCWh7RATWQFw7FKenQE71kvKrrjXzHaaZURmeb00I4OHSveL";
        PaymentIntent paymentIntent;
        try {
            paymentIntent = PaymentIntent.retrieve(request.getPaymentId());
            if (!paymentIntent.getStatus().equals("succeeded")) {
                return ResponseEntity.badRequest().body("Payment failed");
            }
        } catch (StripeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error verifying payment");
        }

        // Complete the subscription.
        Manager manager = payment.getManager();
        Subscription subscription = subscriptionService.getSubscriptionById(payment.getSubscription().getId());
        if (subscription == null) {
            return ResponseEntity.badRequest().body("Invalid subscription ID");
        }
        manager.setSubscription(subscription);
        // Set the manager's subscription status to true.
        manager.setSubscribed(true);
        managerRepository.save(manager);

        // Return a success response.
        return ResponseEntity.ok("Payment confirmed and subscription completed");
    }
*/


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
