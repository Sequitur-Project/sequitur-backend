package com.sequitur.api.DataCollection.service;

import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.dialogflow.v2.*;
import com.google.protobuf.Empty;
import com.google.protobuf.FieldMask;
import com.google.protobuf.Struct;
import com.sequitur.api.DataCollection.domain.model.Intent;
import com.sequitur.api.DataCollection.domain.model.Response;
import com.sequitur.api.DataCollection.domain.model.TrainingPhrase;
import com.sequitur.api.DataCollection.domain.repository.IntentRepository;
import com.sequitur.api.DataCollection.domain.repository.TrainingPhraseRepository;
import com.sequitur.api.DataCollection.domain.service.TrainingPhraseService;
import com.sequitur.api.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TrainingPhraseServiceImpl implements TrainingPhraseService {

    @Autowired
    private TrainingPhraseRepository trainingPhraseRepository;

    @Autowired
    private IntentRepository intentRepository;

    @Override
    public ResponseEntity<?> deleteTrainingPhrase(String trainingPhraseId, UUID intentId) {
        try (IntentsClient intentsClient = IntentsClient.create()) {
            // Get the corresponding intent from your local database.
            Optional<Intent> optionalIntent = intentRepository.findById(intentId);
            if (!optionalIntent.isPresent()) {
                // Handle error when intent is not found.
                return ResponseEntity.notFound().build();
            }
            Intent intent = optionalIntent.get();

            // Get the intent name to use in the GetIntentRequest.
            String intentName = "projects/sequitur-yqvh/locations/global/agent/intents/" + intentId.toString();

            // Get the existing intent from Dialogflow.
            GetIntentRequest getIntentRequest = GetIntentRequest.newBuilder()
                    .setName(intentName)
                    .setLanguageCode("es")
                    .build();
            com.google.cloud.dialogflow.v2.Intent existingIntent = intentsClient.getIntent(getIntentRequest);

            // Find the training phrase with the specified ID in the intent's list of training phrases.
            Optional<TrainingPhrase> optionalTrainingPhrase = trainingPhraseRepository.findByIdAndIntentId(trainingPhraseId, intentId);
            if (!optionalTrainingPhrase.isPresent()) {
                // Handle error when training phrase is not found.
                return ResponseEntity.notFound().build();
            }
            TrainingPhrase trainingPhrase = optionalTrainingPhrase.get();

            // Remove the training phrase from the intent's list of training phrases.
            intent.getTrainingPhrases().remove(trainingPhrase);

            // Create a new list of Dialogflow training phrases without the deleted training phrase.
            List<com.google.cloud.dialogflow.v2.Intent.TrainingPhrase> existingTrainingPhrases = new ArrayList<>();
            for (TrainingPhrase tp : intent.getTrainingPhrases()) {
                com.google.cloud.dialogflow.v2.Intent.TrainingPhrase.Part trainingPhrasePart = com.google.cloud.dialogflow.v2.Intent.TrainingPhrase.Part.newBuilder()
                        .setText(tp.getText())
                        .build();
                com.google.cloud.dialogflow.v2.Intent.TrainingPhrase dfTrainingPhrase = com.google.cloud.dialogflow.v2.Intent.TrainingPhrase.newBuilder()
                        .addParts(trainingPhrasePart)
                        .build();
                existingTrainingPhrases.add(dfTrainingPhrase);
            }

            // Update the intent with the new training phrases.
            com.google.cloud.dialogflow.v2.Intent.Builder intentBuilder = existingIntent.toBuilder();
            intentBuilder.clearTrainingPhrases();
            intentBuilder.addAllTrainingPhrases(existingTrainingPhrases);
            com.google.cloud.dialogflow.v2.Intent updatedIntent = intentBuilder.build();

            // Update the intent on Dialogflow.
            UpdateIntentRequest updateIntentRequest = UpdateIntentRequest.newBuilder()
                    .setIntent(updatedIntent)
                    .setLanguageCode("es")
                    .setUpdateMask(FieldMask.newBuilder().addPaths("training_phrases"))
                    .build();
            intentsClient.updateIntent(updateIntentRequest);

            // Delete the training phrase from your local database.
            trainingPhraseRepository.delete(trainingPhrase);

            // Return a success response.
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("Error deleting training phrase: " + e.getMessage());
            // Return an error response.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public TrainingPhrase updateTrainingPhrase(String trainingPhraseId, UUID intentId, TrainingPhrase trainingPhraseRequest) {
        try (IntentsClient intentsClient = IntentsClient.create()) {
            // Get the corresponding intent from your local database.
            Optional<Intent> optionalIntent = intentRepository.findById(intentId);
            if (!optionalIntent.isPresent()) {
                // Handle error when intent is not found.
                return null;
            }
            Intent intent = optionalIntent.get();

            // Get the intent name to use in the GetIntentRequest.
            String intentName = "projects/sequitur-yqvh/locations/global/agent/intents/" + intentId.toString();

            // Get the existing intent from Dialogflow.
            GetIntentRequest getIntentRequest = GetIntentRequest.newBuilder()
                    .setName(intentName)
                    .setLanguageCode("es")
                    .build();
            com.google.cloud.dialogflow.v2.Intent existingIntent = intentsClient.getIntent(getIntentRequest);

            // Find the training phrase to update and update its text
            Optional<TrainingPhrase> optionalTrainingPhrase = trainingPhraseRepository.findByIdAndIntentId(trainingPhraseId, intentId);
            if (!optionalTrainingPhrase.isPresent()) {
                // Handle error when training phrase is not found.
                return null;
            }
            TrainingPhrase trainingPhrase = optionalTrainingPhrase.get();
            trainingPhrase.setText(trainingPhraseRequest.getText());
            trainingPhraseRepository.save(trainingPhrase);

            // Convert the updated training phrase object to a Dialogflow training phrase object.
            com.google.cloud.dialogflow.v2.Intent.TrainingPhrase.Part part = com.google.cloud.dialogflow.v2.Intent.TrainingPhrase.Part.newBuilder()
                    .setText(trainingPhrase.getText())
                    .build();
            com.google.cloud.dialogflow.v2.Intent.TrainingPhrase dialogflowTrainingPhrase = com.google.cloud.dialogflow.v2.Intent.TrainingPhrase.newBuilder()
                    .addParts(part)
                    .build();

            // Create a new list of Dialogflow training phrases by replacing the old training phrase with the updated one.
            List<com.google.cloud.dialogflow.v2.Intent.TrainingPhrase> existingTrainingPhrases = new ArrayList<>();
            for (TrainingPhrase tp : intent.getTrainingPhrases()) {
                if (tp.getId().equals(trainingPhraseId)) {
                    existingTrainingPhrases.add(dialogflowTrainingPhrase);
                } else {
                    com.google.cloud.dialogflow.v2.Intent.TrainingPhrase.Part trainingPhrasePart = com.google.cloud.dialogflow.v2.Intent.TrainingPhrase.Part.newBuilder()
                            .setText(tp.getText())
                            .build();
                    com.google.cloud.dialogflow.v2.Intent.TrainingPhrase dfTrainingPhrase = com.google.cloud.dialogflow.v2.Intent.TrainingPhrase.newBuilder()
                            .addParts(trainingPhrasePart)
                            .build();
                    existingTrainingPhrases.add(dfTrainingPhrase);
                }
            }

            // Update the intent with the new training phrases
            com.google.cloud.dialogflow.v2.Intent.Builder intentBuilder = existingIntent.toBuilder();
            intentBuilder.clearTrainingPhrases();
            intentBuilder.addAllTrainingPhrases(existingTrainingPhrases);
            com.google.cloud.dialogflow.v2.Intent updatedIntent = intentBuilder.build();

            // Update the intent on Dialogflow.
            UpdateIntentRequest updateIntentRequest = UpdateIntentRequest.newBuilder()
                    .setIntent(updatedIntent)
                    .setLanguageCode("es")
                    .setUpdateMask(FieldMask.newBuilder().addPaths("training_phrases"))
                    .build();
            intentsClient.updateIntent(updateIntentRequest);

            // Return the updated training phrase.
            return trainingPhrase;
        } catch (Exception e) {
            System.err.println("Error updating training phrase: " + e.getMessage());
            return null;
        }
    }

    @Override
    public TrainingPhrase createTrainingPhrase(UUID intentId, TrainingPhrase trainingPhrase) {
        try (IntentsClient intentsClient = IntentsClient.create()) {
            // Get the corresponding intent from your local database.
            Optional<Intent> optionalIntent = intentRepository.findById(intentId);
            if (!optionalIntent.isPresent()) {
                // Handle error when intent is not found.
                return null;
            }
            Intent intent = optionalIntent.get();

            // Get the intent name to use in the GetIntentRequest.
            String intentName = "projects/sequitur-yqvh/locations/global/agent/intents/" + intentId.toString();

            // Get the existing intent from Dialogflow.
            GetIntentRequest getIntentRequest = GetIntentRequest.newBuilder()
                    .setName(intentName)
                    .setLanguageCode("es")
                    .build();
            com.google.cloud.dialogflow.v2.Intent existingIntent = intentsClient.getIntent(getIntentRequest);

            // Set the training phrase ID to be the same in both Dialogflow and local database
            String trainingPhraseId = UUID.randomUUID().toString();
            trainingPhrase.setId(trainingPhraseId);

            // Create a new training phrase with the provided text and ID.
            trainingPhrase.setIntent(intent);
            trainingPhraseRepository.save(trainingPhrase);

            // Add the new training phrase to the intent's list of training phrases
            intent.getTrainingPhrases().add(trainingPhrase);

            // Convert the provided training phrase object to a Dialogflow training phrase object.
            com.google.cloud.dialogflow.v2.Intent.TrainingPhrase.Part part = com.google.cloud.dialogflow.v2.Intent.TrainingPhrase.Part.newBuilder()
                    .setText(trainingPhrase.getText())
                    .build();
            com.google.cloud.dialogflow.v2.Intent.TrainingPhrase dialogflowTrainingPhrase = com.google.cloud.dialogflow.v2.Intent.TrainingPhrase.newBuilder()
                    .addParts(part)
                    .setName(trainingPhraseId)
                    .build();

            // Create a new list of Dialogflow training phrases by adding the new training phrase to the existing list.
            List<com.google.cloud.dialogflow.v2.Intent.TrainingPhrase> existingTrainingPhrases = new ArrayList<>();
            for (TrainingPhrase tp : intent.getTrainingPhrases()) {
                com.google.cloud.dialogflow.v2.Intent.TrainingPhrase.Part trainingPhrasePart = com.google.cloud.dialogflow.v2.Intent.TrainingPhrase.Part.newBuilder()
                        .setText(tp.getText())
                        .build();
                com.google.cloud.dialogflow.v2.Intent.TrainingPhrase dfTrainingPhrase = com.google.cloud.dialogflow.v2.Intent.TrainingPhrase.newBuilder()
                        .addParts(trainingPhrasePart)
                        .setName(tp.getId())
                        .build();
                existingTrainingPhrases.add(dfTrainingPhrase);
            }
            existingTrainingPhrases.add(dialogflowTrainingPhrase);

            // Update the intent with the new training phrases
            com.google.cloud.dialogflow.v2.Intent.Builder intentBuilder = existingIntent.toBuilder();
            intentBuilder.clearTrainingPhrases();
            intentBuilder.addAllTrainingPhrases(existingTrainingPhrases);
            com.google.cloud.dialogflow.v2.Intent updatedIntent = intentBuilder.build();

            // Update the intent on Dialogflow.
            UpdateIntentRequest updateIntentRequest = UpdateIntentRequest.newBuilder()
                    .setIntent(updatedIntent)
                    .setLanguageCode("es")
                    .setUpdateMask(FieldMask.newBuilder().addPaths("training_phrases"))
                    .build();
            intentsClient.updateIntent(updateIntentRequest);

            // Return the created training phrase.
            return trainingPhrase;
        } catch (Exception e) {
            System.err.println("Error creating training phrase: " + e.getMessage());
            return null;
        }
    }

    @Override
    public TrainingPhrase getTrainingPhraseById(String trainingPhraseId) {
        return trainingPhraseRepository.findById(trainingPhraseId)
                .orElseThrow(() -> new ResourceNotFoundException("TrainingPhrase", "Id", trainingPhraseId));
    }

    @Override
    public Page<TrainingPhrase> getAllTrainingPhrases(Pageable pageable) {
        return trainingPhraseRepository.findAll(pageable);
    }

    @Override
    public Page<TrainingPhrase> getAllTrainingPhrasesByIntentId(UUID intentId, Pageable pageable) {
        return trainingPhraseRepository.findByIntentId(intentId, pageable);
    }

    @Override
    public TrainingPhrase getTrainingPhraseByIdAndIntentId(UUID intentId, String trainingPhraseId) {
        return trainingPhraseRepository.findByIdAndIntentId(trainingPhraseId, intentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "TrainingPhrase not found with Id " + trainingPhraseId +
                                " and IntentId " + intentId));
    }
}
