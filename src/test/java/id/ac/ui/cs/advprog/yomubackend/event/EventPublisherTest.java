package id.ac.ui.cs.advprog.yomubackend.event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventPublisherTest {

    @Mock
    private RestTemplate mockRestTemplate;

    @Mock
    private Logger mockLogger;

    private EventPublisher createEventPublisherWithInjectedMocks(String endpoint) throws Exception {
        EventPublisher publisher = new EventPublisher(endpoint);
        Field restTemplateField = EventPublisher.class.getDeclaredField("restTemplate");
        restTemplateField.setAccessible(true);
        restTemplateField.set(publisher, mockRestTemplate);
        Field loggerField = EventPublisher.class.getDeclaredField("logger");
        loggerField.setAccessible(true);
        loggerField.set(publisher, mockLogger);
        return publisher;
    }

    @Test
    void testPublishQuizCompleted_Success() throws Exception {
        String endpoint = "http://test:8080/api/events/quiz-completed";
        EventPublisher publisher = createEventPublisherWithInjectedMocks(endpoint);

        QuizCompletedEvent event = QuizCompletedEvent.builder()
                .userId(1L)
                .textId(10L)
                .quizId(100L)
                .attemptId(1000L)
                .score(5)
                .completedAt(LocalDateTime.now())
                .build();

        when(mockRestTemplate.postForObject(eq(endpoint), any(HttpEntity.class), eq(String.class)))
                .thenReturn(null);

        publisher.publishQuizCompleted(event);

        verify(mockRestTemplate, times(1)).postForObject(eq(endpoint), any(HttpEntity.class), eq(String.class));
        verify(mockLogger).info(eq("Published quiz_completed event for attemptId: {}"), eq(1000L));
    }

    @Test
    void testPublishQuizCompleted_RestTemplateException() throws Exception {
        String endpoint = "http://test:8080/api/events/quiz-completed";
        EventPublisher publisher = createEventPublisherWithInjectedMocks(endpoint);

        QuizCompletedEvent event = QuizCompletedEvent.builder()
                .userId(1L)
                .textId(10L)
                .quizId(100L)
                .attemptId(1000L)
                .score(5)
                .completedAt(LocalDateTime.now())
                .build();

        when(mockRestTemplate.postForObject(any(String.class), any(HttpEntity.class), eq(String.class)))
                .thenThrow(new RuntimeException("Connection refused"));

        publisher.publishQuizCompleted(event);

        verify(mockRestTemplate).postForObject(eq(endpoint), any(HttpEntity.class), eq(String.class));
        verify(mockLogger).warn(eq("Failed to publish quiz_completed event: {}"), anyString());
    }

    @Test
    void testPublishQuizCompleted_CustomEndpoint() throws Exception {
        String customEndpoint = "http://custom-server:9000/events";
        EventPublisher publisher = createEventPublisherWithInjectedMocks(customEndpoint);

        QuizCompletedEvent event = QuizCompletedEvent.builder()
                .userId(1L).textId(10L).quizId(100L).attemptId(1000L).score(5)
                .completedAt(LocalDateTime.now())
                .build();

        when(mockRestTemplate.postForObject(any(), any(HttpEntity.class), eq(String.class))).thenReturn(null);

        publisher.publishQuizCompleted(event);

        verify(mockRestTemplate).postForObject(eq(customEndpoint), any(HttpEntity.class), eq(String.class));
    }
}
