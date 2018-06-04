package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class QuestionController {
    
    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/questions/form")
    public String form(HttpSession session) {
        if (!HttpSessionUtils.isLoginUser(session)) {
            return "/users/loginForm";
        }

        return "qna/form";
    }

    @PostMapping("/questions")
    public String createQuestion(String title, String contents, HttpSession session){
        if (!HttpSessionUtils.isLoginUser(session)) {
            return "/users/loginForm";
        }

        User sessionUser = HttpSessionUtils.getUserFromSession(session);
        Question newQuestion = new Question(sessionUser, title, contents);
        questionRepository.save(newQuestion);
        return "redirect:/";
    }

    @GetMapping("/")
    public String getBackToIndex(Model model){
        model.addAttribute("questions", questionRepository.findAll());
        return "index";
    }

    @GetMapping("/questions/{id}")
    public String getBackToIndex(@PathVariable Long id, Model model) {
        model.addAttribute("question", questionRepository.findById(id).get());
        return "show";
    }

    @GetMapping("/questions/{id}/form")
    public String updateForm(@PathVariable Long id, Model model, HttpSession session) {
        if (!HttpSessionUtils.isLoginUser(session)) {
            return "/users/loginForm";
        }

        User loginUser = HttpSessionUtils.getUserFromSession(session);
        Question question = questionRepository.findById(id).get();
        if (!question.isSameWriter(loginUser)) {
            return "/users/loginForm";
        }

        model.addAttribute("question", question);
        return "/qna/updateForm";
    }

    @PutMapping("/questions/{id}")
    public String update(@PathVariable Long id, String title, String contents, HttpSession session) {
        if (!HttpSessionUtils.isLoginUser(session)) {
            return "/users/loginForm";
        }

        User loginUser = HttpSessionUtils.getUserFromSession(session);
        Question question = questionRepository.findById(id).get();
        if (!question.isSameWriter(loginUser)) {
            return "/users/loginForm";
        }
        question.update(title, contents);
        questionRepository.save(question);
        return String.format("redirect:/questions/%d", id);
    }

    @DeleteMapping("/questions/{id}")
    public String delete(@PathVariable Long id, HttpSession session) {
        if (!HttpSessionUtils.isLoginUser(session)) {
            return "/users/loginForm";
        }

        User loginUser = HttpSessionUtils.getUserFromSession(session);
        Question question = questionRepository.findById(id).get();
        if (!question.isSameWriter(loginUser)) {
            return "/users/loginForm";
        }

        questionRepository.deleteById(id);
        return "redirect:/";
    }
}

