package id.ac.ui.cs.advprog.yomubackend.read_quiz.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.dto.AdminQuestionDto;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.dto.AdminQuizDto;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.exception.QuizNotFoundException;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.exception.TextNotFoundException;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.Question;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.Quiz;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.model.Text;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.repository.QuestionRepository;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.repository.QuizRepository;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.repository.TextRepository;
import id.ac.ui.cs.advprog.yomubackend.read_quiz.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
// import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminQuizController {

    @Autowired
    private TextRepository textRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizService quizService; // to produce QuizDto response

    private final ObjectMapper mapper = new ObjectMapper();

    @PostMapping("/texts/{textId}/quizzes")
    public ResponseEntity<?> createQuiz(@PathVariable Long textId, @RequestBody AdminQuizDto dto) {
        Text text = textRepository.findById(textId).orElseThrow(() -> new TextNotFoundException(textId));

        Quiz quiz = Quiz.builder()
                .title(dto.getTitle())
                .text(text)
                .build();

        List<Question> questions = new ArrayList<>();
        if (dto.getQuestions() != null) {
            for (AdminQuestionDto qd : dto.getQuestions()) {
                String optionsJson = null;
                if (qd.getOptions() != null) {
                    try {
                        optionsJson = mapper.writeValueAsString(qd.getOptions());
                    } catch (JsonProcessingException e) {
                        optionsJson = String.join(",", qd.getOptions());
                    }
                }
                Question q = Question.builder()
                        .quiz(quiz)
                        .kind(qd.getKind())
                        .questionText(qd.getQuestionText())
                        .options(optionsJson)
                        .correctAnswer(qd.getCorrectAnswer())
                        .build();
                questions.add(q);
            }
        }

        quiz.setQuestions(questions);
        quiz = quizRepository.save(quiz);

        return ResponseEntity.status(HttpStatus.CREATED).body(quizService.getQuizById(quiz.getId()));
    }

    @PutMapping("/quizzes/{id}")
    public ResponseEntity<?> updateQuiz(@PathVariable Long id, @RequestBody AdminQuizDto dto) {
        Quiz q = quizRepository.findById(id).orElseThrow(() -> new QuizNotFoundException(id));
        if (dto.getTitle() != null) q.setTitle(dto.getTitle());

        // replace question list if provided
        if (dto.getQuestions() != null) {
            // remove existing questions
            if (q.getQuestions() != null) {
                q.getQuestions().clear();
            }
            List<Question> newQs = new ArrayList<>();
            for (AdminQuestionDto qd : dto.getQuestions()) {
                String optionsJson = null;
                if (qd.getOptions() != null) {
                    try {
                        optionsJson = mapper.writeValueAsString(qd.getOptions());
                    } catch (JsonProcessingException e) {
                        optionsJson = String.join(",", qd.getOptions());
                    }
                }
                Question nq = Question.builder()
                        .quiz(q)
                        .kind(qd.getKind())
                        .questionText(qd.getQuestionText())
                        .options(optionsJson)
                        .correctAnswer(qd.getCorrectAnswer())
                        .build();
                newQs.add(nq);
            }
            q.setQuestions(newQs);
        }

        q = quizRepository.save(q);
        return ResponseEntity.ok(quizService.getQuizById(q.getId()));
    }

    @DeleteMapping("/quizzes/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
        if (!quizRepository.existsById(id)) {
            throw new QuizNotFoundException(id);
        }
        quizRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
