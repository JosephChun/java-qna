package codesquad.codesquad.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private List<User> users = new ArrayList<>();

    @GetMapping("/join")
    public String join() {
        return "/user/form";
    }

    @PostMapping("/create")
    public String create(User user){
        System.out.println("user: " + user);
        users.add(user);
        return "redirect:/users/list";
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("users", users);
        return "list";
    }

    @GetMapping("/{userId}")
    public String profile(@PathVariable String userId ,Model model){
        for (User user:users) {
            if (user.getUserId().equals(userId)){
                model.addAttribute("user", user);
            }
        }
        return "profile";
    }

    @GetMapping("/{userId}/form")
    public String update(@PathVariable String userId, Model model) {
        for (User user:users) {
            if (user.getUserId().equals(userId)){
                model.addAttribute("users", users);
            }
        }
        return "/user/updateForm";
    }

    @PostMapping("/{userId}/update")
    public String update(@PathVariable String userId, User editor) {
        for (User user:users) {
            if (user.matchUser(userId)){
                user.updateInformation(editor, user.getPassword());
                break;
            }
        }
        return "redirect:/users/list";
    }
}
