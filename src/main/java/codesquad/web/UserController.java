package codesquad.web;

import codesquad.domain.User;
import codesquad.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/loginForm")
    public String loginForm() {
        return "/user/login";
    }

    @PostMapping("/login")
    public String login(String userId, String password, HttpSession session) {


        User user = userRepository.findByUserId(userId);

        if (user == null) {
            return "redirect:/users/loginForm";
        }

        if (!password.equals(user.getPassword())) {
            return "redirect:/users/loginForm";
        }

        session.setAttribute("sessionedUser", user);

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.removeAttribute("sessionedUser");

        return "redirect:/";
    }

    @GetMapping("")
    public String home() {
        return "index";
    }

    @GetMapping("/form")
    public String join() {
        return "/user/form";
    }

    @PostMapping("/create")
    public String create(User user){
        userRepository.save(user);
        return "redirect:/users/list";
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "list";
    }

    @GetMapping("/{id}")
    public String profile(@PathVariable Long id ,Model model){
        User user = userRepository.findById(id).get();
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/{id}/form")
    public String updateForm(@PathVariable Long id, Model model, HttpSession session) {
        Object tempUser = session.getAttribute("sessionedUser");
        if (tempUser == null) {
            return "redirect:/users/loginForm";
        }

        User sessionedUser = (User)tempUser;
        if (!id.equals(sessionedUser.getId())) {
            throw new IllegalStateException("You can't update the other user");
        }

        model.addAttribute("users", userRepository.findById(id).get());
        return "/user/updateForm";
    }

    @PutMapping("/{id}/update")
    public String update(@PathVariable Long id, User editor, HttpSession session) {
        Object tempUser = session.getAttribute("sessionedUser");
        if (tempUser == null) {
            return "redirect:/users/loginForm";
        }

        User sessionedUser = (User)tempUser;
        if (!id.equals(sessionedUser.getId())) {
            throw new IllegalStateException("You can't update the other user");
        }

        User user = userRepository.findById(id).get();
        if (user.matchUser(user.getUserId())){
            user.updateInformation(editor, user.getPassword());
                userRepository.save(user);
        }
        return "redirect:/users/list";
    }
}
