package id.ac.ui.cs.advprog.yomubackend.read_quiz.service;

import id.ac.ui.cs.advprog.yomubackend.auth.entity.User;
import id.ac.ui.cs.advprog.yomubackend.event.EventPublisher;
import id.ac.ui.cs.advprog.yomubackend.event.QuizCompletedEvent;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.dto.*;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.exception.QuizNotFoundException;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.exception.QuizAttemptNotFoundException;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.mapper.QuestionMapper;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.*;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final GradingRepository gradingRepository;
    private final QuestionMapper questionMapper;
    private final EventPublisher eventPublisher;

    @Autowired
    public QuizServiceImpl(
            QuizRepository quizRepository,
            QuizAttemptRepository quizAttemptRepository,
            QuestionRepository questionRepository,
            AnswerRepository answerRepository,
            GradingRepository gradingRepository,
            QuestionMapper questionMapper,
            EventPublisher eventPublisher) {
        this.quizRepository = quizRepository;
        this.quizAttemptRepository = quizAttemptRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.gradingRepository = gradingRepository;
        this.questionMapper = questionMapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional(readOnly = true)
    public QuizDto getQuizById(Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new QuizNotFoundException(id));
        
        List<Question> questions = questionRepository.findByQuizId(id);
        List<QuestionDto> questionDtos = questions.stream()
                .map(questionMapper::toDto)
                .collect(Collectors.toList());
        
        return QuizDto.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .questions(questionDtos)
                .build();
    }

    @Override
    public QuizAttemptResultDto startQuiz(Long quizId, User user) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new QuizNotFoundException(quizId));
        
        boolean hasCompleted = quizAttemptRepository.existsByUserAndTextIdSubmitted(user, quiz.getText().getId());
        if (hasCompleted) {
            throw new IllegalStateException("You have already completed this quiz");
        }
        
        QuizAttempt attempt = QuizAttempt.builder()
                .user(user)
                .quiz(quiz)
                .startedAt(LocalDateTime.now())
                .build();
        
        attempt = quizAttemptRepository.save(attempt);
        
        return QuizAttemptResultDto.builder()
                .attemptId(attempt.getId())
                .score(null)
                .startedAt(attempt.getStartedAt())
                .submittedAt(null)
                .gradingResults(new ArrayList<>())
                .build();
    }

    @Override
    public QuizAttemptResultDto submitQuiz(Long attemptId, QuizSubmissionDto submission, User user) {
        QuizAttempt attempt = quizAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new QuizAttemptNotFoundException(attemptId));
        
        if (!attempt.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Unauthorized to submit this quiz attempt");
        }
        
        if (attempt.getSubmittedAt() != null) {
            throw new IllegalStateException("Quiz already submitted");
        }
        
        Quiz quiz = attempt.getQuiz();
        List<Question> questions = questionRepository.findByQuizId(quiz.getId());
        Map<Long, Question> questionMap = questions.stream()
                .collect(Collectors.toMap(Question::getId, q -> q));
        
        List<Answer> answers = new ArrayList<>();
        List<GradingResultDto> gradingResults = new ArrayList<>();
        int totalScore = 0;
        
        for (AnswerDto answerDto : submission.getAnswers()) {
            Question question = questionMap.get(answerDto.getQuestionId());
            if (question == null) {
                continue;
            }
            
            Answer answer = Answer.builder()
                    .quizAttempt(attempt)
                    .question(question)
                    .userAnswer(answerDto.getUserAnswer())
                    .build();
            answer = answerRepository.save(answer);
            answers.add(answer);
            
            boolean isCorrect = gradeAnswer(question, answerDto.getUserAnswer());
            int score = isCorrect ? 1 : 0;
            totalScore += score;
            
            Grading grading = Grading.builder()
                    .answer(answer)
                    .isCorrect(isCorrect)
                    .score(score)
                    .feedback(isCorrect ? "Correct!" : "Incorrect")
                    .build();
            gradingRepository.save(grading);
            
             gradingResults.add(GradingResultDto.builder()
                     .questionId(question.getId())
                     .isCorrect(isCorrect)
                     .score(score)
                     .feedback(isCorrect ? "Correct!" : "Incorrect")
                     .correctAnswer(question.getCorrectAnswer())
                     .userAnswer(answerDto.getUserAnswer())
                     .build());
        }
        
        attempt.setScore(totalScore);
        attempt.setSubmittedAt(LocalDateTime.now());
        quizAttemptRepository.save(attempt);
        
        QuizCompletedEvent event = QuizCompletedEvent.builder()
                .userId(user.getId())
                .textId(quiz.getText().getId())
                .quizId(quiz.getId())
                .attemptId(attempt.getId())
                .score(totalScore)
                .completedAt(attempt.getSubmittedAt())
                .build();
        eventPublisher.publishQuizCompleted(event);
        
        return QuizAttemptResultDto.builder()
                .attemptId(attempt.getId())
                .score(totalScore)
                .startedAt(attempt.getStartedAt())
                .submittedAt(attempt.getSubmittedAt())
                .gradingResults(gradingResults)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public QuizAttemptResultDto getAttemptResult(Long attemptId, User user) {
        QuizAttempt attempt = quizAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new QuizAttemptNotFoundException(attemptId));
        
        if (!attempt.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Unauthorized to view this quiz attempt");
        }
        
        List<Answer> answers = answerRepository.findByQuizAttempt(attempt);
        List<GradingResultDto> gradingResults = new ArrayList<>();
        
        for (Answer answer : answers) {
            Grading grading = gradingRepository.findByAnswer(answer).stream().findFirst().orElse(null);
            if (grading != null) {
                gradingResults.add(GradingResultDto.builder()
                        .questionId(answer.getQuestion().getId())
                        .isCorrect(grading.getIsCorrect())
                        .score(grading.getScore())
                        .feedback(grading.getFeedback())
                        .correctAnswer(answer.getQuestion().getCorrectAnswer())
                        .userAnswer(answer.getUserAnswer())
                        .build());
            }
        }
        
        return QuizAttemptResultDto.builder()
                .attemptId(attempt.getId())
                .score(attempt.getScore())
                .startedAt(attempt.getStartedAt())
                .submittedAt(attempt.getSubmittedAt())
                .gradingResults(gradingResults)
                .build();
    }

    private boolean gradeAnswer(Question question, String userAnswer) {
        String correctAnswer = question.getCorrectAnswer();
        if (correctAnswer == null || userAnswer == null) {
            return false;
        }
        
        userAnswer = userAnswer.trim();
        
        if ("multiple_choice".equals(question.getKind())) {
            correctAnswer = correctAnswer.trim();
            return correctAnswer.equalsIgnoreCase(userAnswer);
        } else if ("short_answer".equals(question.getKind())) {
            // Multiple acceptable answers separated by '/'
            String[] alternatives = correctAnswer.split("/");
            for (String alt : alternatives) {
                if (alt.trim().equalsIgnoreCase(userAnswer)) {
                    return true;
                }
            }
            return false;
        }
        
        return false;
    }
}
