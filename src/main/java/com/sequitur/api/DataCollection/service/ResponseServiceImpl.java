package com.sequitur.api.DataCollection.service;

import com.google.api.gax.rpc.ApiException;
import com.google.cloud.dialogflow.v2.*;
import com.google.cloud.dialogflow.v2beta1.ResponseMessage;
import com.google.protobuf.FieldMask;
import com.google.protobuf.ListValue;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import com.sequitur.api.DataCollection.domain.model.Intent;
import com.sequitur.api.DataCollection.domain.model.Response;
import com.sequitur.api.DataCollection.domain.repository.IntentRepository;
import com.sequitur.api.DataCollection.domain.repository.ResponseRepository;
import com.sequitur.api.DataCollection.domain.service.ResponseService;
import com.sequitur.api.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ResponseServiceImpl implements ResponseService {

    @Autowired
    private ResponseRepository responseRepository;

    @Autowired
    private IntentRepository intentRepository;

    @Override
    public ResponseEntity<?> deleteResponse(String responseId, UUID intentId) {
        Optional<Response> optionalResponse = responseRepository.findById(responseId);

        if (!optionalResponse.isPresent()) {
            // Handle error when response is not found.
            return ResponseEntity.notFound().build();
        }

        Response response = optionalResponse.get();

        if (!response.getIntent().getId().equals(intentId)) {
            // Handle error when response does not belong to the specified intent.
            return ResponseEntity.badRequest().build();
        }

        // Delete the response from your local database.
        responseRepository.delete(response);

        try (IntentsClient intentsClient = IntentsClient.create()) {
            String intentName = "projects/sequitur-yqvh/locations/global/agent/intents/" + intentId.toString();

            // Retrieve the existing list of responses for the intent.
            List<com.google.cloud.dialogflow.v2.Intent.Message> messages = new ArrayList<>();
            for (Response r : responseRepository.findByIntentId(intentId, PageRequest.of(0, Integer.MAX_VALUE)).getContent()) {
                messages.add(com.google.cloud.dialogflow.v2.Intent.Message.newBuilder()
                        .setText(com.google.cloud.dialogflow.v2.Intent.Message.Text.newBuilder()
                                .addText(r.getMessageText()).build()).build());
            }

            // Update the intent in Dialogflow with the updated list of responses.
            com.google.cloud.dialogflow.v2.Intent.Builder intentBuilder = com.google.cloud.dialogflow.v2.Intent.newBuilder()
                    .setName(intentName)
                    .addAllMessages(messages);
            UpdateIntentRequest updateIntentRequest = UpdateIntentRequest.newBuilder()
                    .setIntent(intentBuilder.build())
                    .setLanguageCode("es")
                    .setUpdateMask(FieldMask.newBuilder().addPaths("messages"))
                    .build();
            com.google.cloud.dialogflow.v2.Intent updatedIntent = intentsClient.updateIntent(updateIntentRequest);

            // Return a success response.
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("Error deleting response: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public Response updateResponse(String responseId,UUID intentId, Response responseRequest) {
        try (IntentsClient intentsClient = IntentsClient.create()) {
            // Get the corresponding intent from your local database.
            Optional<Intent> optionalIntent = intentRepository.findById(intentId);
            if (!optionalIntent.isPresent()) {
                // Handle error when intent is not found.
                return null;
            }
            Intent intent = optionalIntent.get();

            String intentName = "projects/sequitur-yqvh/locations/global/agent/intents/" + intentId.toString();

            // Get the existing response from your local database.
            Optional<Response> optionalResponse = responseRepository.findById(responseId);
            if (!optionalResponse.isPresent()) {
                // Handle error when response is not found.
                return null;
            }
            Response response = optionalResponse.get();

            // Update the response with the new data.
            response.setMessageText(responseRequest.getMessageText());
            responseRepository.save(response);

            // Retrieve the existing list of responses for the intent.
            List<com.google.cloud.dialogflow.v2.Intent.Message> messages = new ArrayList<>();
            for (Response r : responseRepository.findByIntentId(intentId, PageRequest.of(0, Integer.MAX_VALUE)).getContent()) {
                messages.add(com.google.cloud.dialogflow.v2.Intent.Message.newBuilder()
                        .setText(com.google.cloud.dialogflow.v2.Intent.Message.Text.newBuilder()
                                .addText(r.getMessageText()).build()).build());
            }

            // Update the corresponding message in the list of responses.
            for (int i = 0; i < messages.size(); i++) {
                com.google.cloud.dialogflow.v2.Intent.Message message = messages.get(i);
                if (message.getText().getTextList().contains(response.getMessageText())) {
                    messages.set(i, com.google.cloud.dialogflow.v2.Intent.Message.newBuilder()
                            .setText(com.google.cloud.dialogflow.v2.Intent.Message.Text.newBuilder()
                                    .addText(responseRequest.getMessageText()).build()).build());
                    break;
                }
            }

            // Update the intent in Dialogflow with the updated list of responses.
            com.google.cloud.dialogflow.v2.Intent.Builder intentBuilder = com.google.cloud.dialogflow.v2.Intent.newBuilder()
                    .setDisplayName(intent.getDisplayName())
                    .setName(intentName)
                    .addAllMessages(messages);
            UpdateIntentRequest updateIntentRequest = UpdateIntentRequest.newBuilder()
                    .setIntent(intentBuilder.build())
                    .setLanguageCode("es")
                    .setUpdateMask(FieldMask.newBuilder().addPaths("messages"))
                    .build();
            com.google.cloud.dialogflow.v2.Intent updatedIntent = intentsClient.updateIntent(updateIntentRequest);

            // Return the updated response.
            return response;
        } catch (Exception e) {
            System.err.println("Error updating response: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Response createResponse(UUID intentId, Response response) {
        try (IntentsClient intentsClient = IntentsClient.create()) {
            // Get the corresponding intent from your local database.
            Optional<Intent> optionalIntent = intentRepository.findById(intentId);
            if (!optionalIntent.isPresent()) {
                // Handle error when intent is not found.
                return null;
            }
            Intent intent = optionalIntent.get();

            String intentName = "projects/sequitur-yqvh/locations/global/agent/intents/" + intentId.toString();

            // Save the new response to your local database.
            response.setId(UUID.randomUUID().toString());
            response.setIntent(intent);
            responseRepository.save(response);

            // Retrieve the existing list of responses for the intent.
            List<com.google.cloud.dialogflow.v2.Intent.Message> messages = new ArrayList<>();
            for (Response r : responseRepository.findByIntentId(intentId, PageRequest.of(0, Integer.MAX_VALUE)).getContent()) {
                messages.add(com.google.cloud.dialogflow.v2.Intent.Message.newBuilder()
                        .setText(com.google.cloud.dialogflow.v2.Intent.Message.Text.newBuilder()
                                .addText(r.getMessageText()).build()).build());
            }

            // Check if the new response is already present in the list of responses
            boolean responseExists = messages.stream()
                    .flatMap(message -> message.getText().getTextList().stream())
                    .anyMatch(text -> text.equals(response.getMessageText()));

            // Add the new response to the list of responses if it doesn't already exist
            if (!responseExists) {
                messages.add(com.google.cloud.dialogflow.v2.Intent.Message.newBuilder()
                        .setText(com.google.cloud.dialogflow.v2.Intent.Message.Text.newBuilder()
                                .addText(response.getMessageText()).build()).build());
            }

            // Update the intent in Dialogflow with the updated list of responses.
            com.google.cloud.dialogflow.v2.Intent.Builder intentBuilder = com.google.cloud.dialogflow.v2.Intent.newBuilder()
                    .setDisplayName(intent.getDisplayName())
                    .setName(intentName)
                    .addAllMessages(messages);
            UpdateIntentRequest updateIntentRequest = UpdateIntentRequest.newBuilder()
                    .setIntent(intentBuilder.build())
                    .setLanguageCode("es")
                    .setUpdateMask(FieldMask.newBuilder().addPaths("messages"))
                    .build();
            com.google.cloud.dialogflow.v2.Intent updatedIntent = intentsClient.updateIntent(updateIntentRequest);

            // Return the created response.
            return response;
        } catch (Exception e) {
            System.err.println("Error creating response: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Response getResponseById(String responseId) {
        return responseRepository.findById(responseId)
                .orElseThrow(() -> new ResourceNotFoundException("Response", "Id", responseId));
    }

    @Override
    public Page<Response> getAllResponses(Pageable pageable) {
        return responseRepository.findAll(pageable);
    }

    @Override
    public Page<Response> getAllResponsesByIntentId(UUID intentId, Pageable pageable) {
        return responseRepository.findByIntentId(intentId, pageable);
    }

    @Override
    public Response getResponseByIdAndIntentId(UUID intentId, String responseId) {
        return responseRepository.findByIdAndIntentId(responseId, intentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Response not found with Id " + responseId +
                                " and IntentId " + intentId));
    }
}
