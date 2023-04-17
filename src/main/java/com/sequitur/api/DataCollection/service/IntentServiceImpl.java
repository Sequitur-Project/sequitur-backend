package com.sequitur.api.DataCollection.service;

import com.google.cloud.dialogflow.v2.*;
import com.google.protobuf.FieldMask;
import com.sequitur.api.DataCollection.domain.model.Intent;
import com.sequitur.api.DataCollection.domain.repository.IntentRepository;
import com.sequitur.api.DataCollection.domain.service.IntentService;
import com.sequitur.api.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.UUID;

@Service
public class IntentServiceImpl implements IntentService {

    @Autowired
    private IntentRepository intentRepository;

    @Override
    public ResponseEntity<?> deleteIntent(UUID intentUuid) {
        try (IntentsClient intentsClient = IntentsClient.create()) {
            String intentName = "projects/sequitur-yqvh/locations/global/agent/intents/" + intentUuid.toString();

            // Create the DeleteIntentRequest object.
            DeleteIntentRequest deleteIntentRequest = DeleteIntentRequest.newBuilder()
                    .setName(intentName)
                    .build();

            // Send the DeleteIntentRequest to Dialogflow and return the HTTP response.
            intentsClient.deleteIntent(deleteIntentRequest);

            // Delete the intent from your local database.
            intentRepository.deleteById(intentUuid);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("Error deleting intent: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public Intent updateIntent(UUID intentUuid, Intent intentRequest) {
        try (IntentsClient intentsClient = IntentsClient.create()) {
            // Find the intent in Dialogflow by its UUID.
            IntentName intentName = IntentName.ofProjectLocationIntentName("sequitur-yqvh", "global", intentUuid.toString());
            com.google.cloud.dialogflow.v2.Intent existingIntent = intentsClient.getIntent(intentName);

            // Update the display name of the existing intent.
            existingIntent = existingIntent.toBuilder()
                    .setDisplayName(intentRequest.getDisplayName())
                    .build();

            // Build the UpdateIntentRequest.
            UpdateIntentRequest request = UpdateIntentRequest.newBuilder()
                    .setIntent(existingIntent)
                    .setLanguageCode("es")
                    .setUpdateMask(FieldMask.newBuilder().addPaths("display_name"))
                    .build();

            // Send the request to Dialogflow and retrieve the updated intent.
            com.google.cloud.dialogflow.v2.Intent updatedIntent = intentsClient.updateIntent(request);

            // Create a new Intent object with the updated properties.
            Intent localIntent = intentRepository.findById(intentUuid)
                    .orElseThrow(() -> new ResourceNotFoundException("Intent", "UUID", intentUuid.toString()));
            localIntent.setDisplayName(updatedIntent.getDisplayName());

            // Save the updated Intent object to your local database.
            intentRepository.save(localIntent);

            // Return the updated Intent object.
            return localIntent;
        } catch (Exception e) {
            System.err.println("Error updating intent: " + e.getMessage());
            return null;
        }
    }

    public static byte[] toByteArray(UUID uuid) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES * 2);
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());
        return buffer.array();
    }
    @Override
    public Intent createIntent(Intent intent) {
        try (IntentsClient intentsClient = IntentsClient.create()) {
            // Convert the provided intent object to a Dialogflow intent object.
            com.google.cloud.dialogflow.v2.Intent dialogflowIntent = com.google.cloud.dialogflow.v2.Intent.newBuilder()
                    .setDisplayName(intent.getDisplayName())
                    .build();

            // Create the CreateIntentRequest object with the parent project ID.
            CreateIntentRequest createIntentRequest = CreateIntentRequest.newBuilder()
                    .setParent("projects/sequitur-yqvh/agent")
                    .setIntent(dialogflowIntent)
                    .setLanguageCode("es")
                    .setIntentView(IntentView.INTENT_VIEW_FULL)
                    .build();

            // Send the CreateIntentRequest to Dialogflow and return the created intent.
            com.google.cloud.dialogflow.v2.Intent createdIntent = intentsClient.createIntent(createIntentRequest);

            // Convert the created intent object back to a local intent object.
            Intent localIntent = new Intent();
            localIntent.setId(UUID.fromString(createdIntent.getName().split("/")[4]));
            localIntent.setDisplayName(createdIntent.getDisplayName());

            // Save the created intent in your local database.
            intentRepository.save(localIntent);

            // Return the created intent.
            return localIntent;
        } catch (Exception e) {
            System.err.println("Error creating intent: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Intent getIntentById(UUID intentId) {
        String intentIdStr = intentId.toString();
        String[] parts = intentIdStr.split("-");
        Long mostSignificantBits = Long.parseLong(parts[0], 16);
        Long leastSignificantBits = Long.parseLong(parts[1], 16);
        Long id = new UUID(mostSignificantBits, leastSignificantBits).getMostSignificantBits();
        Intent intent = intentRepository.findById(intentId).orElseThrow(() -> new ResourceNotFoundException("Intent", "Id", intentId));
        return intent;
    }

    @Override
    public Page<Intent> getAllIntents(Pageable pageable) {
        return intentRepository.findAll(pageable);
    }
}
