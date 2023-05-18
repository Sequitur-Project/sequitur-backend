package com.sequitur.api.DataCollection.service;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.cloud.dialogflow.v2.*;
import com.sequitur.api.DataCollection.domain.model.BotMessage;
import com.sequitur.api.DataCollection.domain.model.Conversation;
import com.sequitur.api.DataCollection.domain.model.StudentMessage;
import com.sequitur.api.DataCollection.domain.repository.BotMessageRepository;
import com.sequitur.api.DataCollection.domain.repository.ConversationRepository;
import com.sequitur.api.DataCollection.domain.repository.StudentMessageRepository;
import com.sequitur.api.DataCollection.domain.service.StudentMessageService;
import com.sequitur.api.DiagnosticAndTreatment.domain.model.Result;
import com.sequitur.api.DiagnosticAndTreatment.domain.model.UniversityDepressionIndicatorSet;
import com.sequitur.api.DiagnosticAndTreatment.domain.repository.ResultRepository;
import com.sequitur.api.DiagnosticAndTreatment.domain.repository.UniversityDepressionIndicatorSetRepository;
import com.sequitur.api.IdentityAccessManagement.domain.model.Psychologist;
import com.sequitur.api.IdentityAccessManagement.domain.model.Student;
import com.sequitur.api.IdentityAccessManagement.domain.repository.PsychologistRepository;
import com.sequitur.api.IdentityAccessManagement.domain.repository.StudentRepository;
import com.sequitur.api.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class StudentMessageServiceImpl implements StudentMessageService {

    @Autowired
    private StudentMessageRepository studentMessageRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private BotMessageRepository botMessageRepository;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UniversityDepressionIndicatorSetRepository universityDepressionIndicatorSetRepository;

    @Autowired
    private PsychologistRepository psychologistRepository;

    @Override
    public ResponseEntity<?> deleteStudentMessage(Long studentMessageId, Long conversationId) {
        StudentMessage studentMessage = studentMessageRepository.findById(studentMessageId)
                .orElseThrow(() -> new ResourceNotFoundException("StudentMessage", "Id", studentMessageId));
        studentMessageRepository.delete(studentMessage);
        return ResponseEntity.ok().build();
    }

    @Override
    public StudentMessage updateStudentMessage(Long studentMessageId, Long conversationId, StudentMessage studentMessageRequest) {
        StudentMessage studentMessage = studentMessageRepository.findById(studentMessageId)
                .orElseThrow(() -> new ResourceNotFoundException("StudentMessage", "Id", studentMessageId));
        studentMessage.setMessage(studentMessageRequest.getMessage());
        return studentMessageRepository.save(studentMessage);
    }

    @Override
    public StudentMessage createStudentMessage(Long conversationId, StudentMessage studentMessage) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation", "Id", conversationId));

        studentMessage.setConversation(conversation);
        StudentMessage savedMessage = studentMessageRepository.save(studentMessage);

        // call Dialogflow to get bot's response
        String projectId = "sequitur-yqvh";
        String sessionId = conversationId.toString();
        String languageCode = "es";
        String text = studentMessage.getMessage();
        try (SessionsClient sessionsClient = SessionsClient.create()){
            SessionName session = SessionName.of(projectId, sessionId);
            TextInput.Builder textInput = TextInput.newBuilder().setText(text).setLanguageCode(languageCode);
            QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();
            DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);
            QueryResult queryResult = response.getQueryResult();
            String fulfillmentText = queryResult.getFulfillmentText();

            // create new BotMessage and add it to conversation
            BotMessage botMessage = new BotMessage();
            botMessage.setMessage(fulfillmentText);
            botMessage.setConversation(conversation);
            botMessageRepository.save(botMessage);

            // calculate score if it's the last question
            if (queryResult.getIntent().getDisplayName().equals("PHQ-9 Q9")) {
                List<StudentMessage> studentMessages = conversation.getStudentMessages();
                int score = 0;
                int currentQuestion = 1; // initialize to first question
                for (StudentMessage message : studentMessages) {
                    try {
                        int answer = Integer.parseInt(message.getMessage().trim());
                        if (answer >= 0 && answer <= 3) { // check if valid answer
                            score += answer;
                            currentQuestion++;
                        }
                    } catch (NumberFormatException e) {
                        // do nothing, not a valid integer answer
                    }
                }
                // create new Result and add it to conversation
                Result result = new Result();
                result.setConversation(conversation);
                result.setStudent(conversation.getStudent());
                result.setScore(score);

                String status;
                if (score >= 0 && score <= 4) {
                    status = "Depresión Mínima";
                } else if (score >= 5 && score <= 9) {
                    status = "Depresión Leve";
                } else if (score >= 10 && score <= 14) {
                    status = "Depresión Moderada";
                } else if (score >= 15 && score <= 19) {
                    status = "Depresión Moderamente Severa";
                } else {
                    status = "Depresión Severa";
                }

                result.setStatus(status);
                conversation.getResults().add(result);
                resultRepository.save(result);

                // update university depression indicators
                List<Student> students = studentRepository.findByUniversityId(conversation.getStudent().getUniversity().getId());
                Long universityId = conversation.getStudent().getUniversity().getId();
                List<Psychologist> psychologists = psychologistRepository.findByUniversityId(universityId);
                int totalStudents = students.size();
                int totalPsychologists = psychologists.size();
                int depressionLeveCount = 0;
                int depressionMinimaCount = 0;
                int depressionModeradaCount = 0;
                int depressionModeradaSeveraCount = 0;
                int depressionSeveraCount = 0;
                int[] depressionCount = new int[5]; // to count students in each depression level
                for (Student student : students) {
                    List<Result> results = resultRepository.findByStudentId(student.getId());
                    if (!results.isEmpty()) {
                        Result latestResult = results.get(results.size() - 1);
                        switch (latestResult.getStatus()) {
                            case "Depresión Mínima":
                                depressionCount[0]++;
                                depressionMinimaCount++;
                                break;
                            case "Depresión Leve":
                                depressionCount[1]++;
                                depressionLeveCount++;
                                break;
                            case "Depresión Moderada":
                                depressionCount[2]++;
                                depressionModeradaCount++;
                                break;
                            case "Depresión Moderadamente Severa":
                                depressionCount[3]++;
                                depressionModeradaSeveraCount++;
                                break;
                            case "Depresión Severa":
                                depressionCount[4]++;
                                depressionSeveraCount++;
                                break;
                            default:
                                // do nothing
                                break;
                        }
                    }
                }

                // calculate percentages
                int totalDepressed = depressionCount[1] + depressionCount[2] + depressionCount[3] + depressionCount[4];
                int totalNonDepressed = depressionCount[0];
                double depressionPercentage = (double) totalDepressed / totalStudents * 100;
                double noDepressionPercentage = (double) totalNonDepressed / totalStudents * 100;

                // save/update university depression indicators
                UniversityDepressionIndicatorSet indicators = universityDepressionIndicatorSetRepository.findAllByUniversityId(conversation.getStudent().getUniversity().getId());
                if (indicators == null) {
                    indicators = new UniversityDepressionIndicatorSet();
                    indicators.setUniversity(conversation.getStudent().getUniversity());
                }
                indicators.setDepressionPercentage(depressionPercentage);
                indicators.setNoDepressionPercentage(noDepressionPercentage);
                indicators.setStudentsQuantity(totalStudents);
                indicators.setPsychologistsQuantity(totalPsychologists);
                indicators.setDepressionLeveQuantity(depressionLeveCount);
                indicators.setDepressionMinimaQuantity(depressionMinimaCount);
                indicators.setDepressionModeradaQuantity(depressionModeradaCount);
                indicators.setDepressionModeradaSeveraQuantity(depressionModeradaSeveraCount);
                indicators.setDepressionSeveraQuantity(depressionSeveraCount);
                universityDepressionIndicatorSetRepository.save(indicators);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return savedMessage;
    }





    @Override
    public StudentMessage getStudentMessageById(Long studentMessageId) {
        return studentMessageRepository.findById(studentMessageId)
                .orElseThrow(() -> new ResourceNotFoundException("StudentMessage", "Id", studentMessageId));
    }

    @Override
    public Page<StudentMessage> getAllStudentMessages(Pageable pageable) {
        return studentMessageRepository.findAll(pageable);
    }

    @Override
    public Page<StudentMessage> getAllStudentMessagesByConversationId(Long conversationId, Pageable pageable) {
        return studentMessageRepository.findByConversationId(conversationId, pageable);
    }

    @Override
    public StudentMessage getStudentMessageByIdAndConversationId(Long conversationId, Long studentMessageId) {
        return studentMessageRepository.findByIdAndConversationId(studentMessageId, conversationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "StudentMessage not found with Id " + studentMessageId +
                                " and ConversationId " + conversationId));
    }


}
