package id.ac.ui.cs.advprog.yomubackend.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EventPublisher {

    private final Logger logger;
    private final RestTemplate restTemplate;
    private final String eventEndpoint;

    @Autowired
    public EventPublisher(
            @Value("${yomu.event.endpoint:http://localhost:8080/api/events/quiz-completed}") String eventEndpoint) {
        this.logger = LoggerFactory.getLogger(EventPublisher.class);
        this.restTemplate = new RestTemplate();
        this.eventEndpoint = eventEndpoint;
    }

    @Async
    public void publishQuizCompleted(QuizCompletedEvent event) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<QuizCompletedEvent> request = new HttpEntity<>(event, headers);
            
            restTemplate.postForObject(eventEndpoint, request, String.class);
            logger.info("Published quiz_completed event for attemptId: {}", event.getAttemptId());
        } catch (Exception e) {
            logger.warn("Failed to publish quiz_completed event: {}", e.getMessage());
        }
    }
}