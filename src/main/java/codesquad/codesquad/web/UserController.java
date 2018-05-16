package codesquad.codesquad.web;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    private List<User> users = new ArrayList<>();
    @PostMapping("/user/create")
    public String create(User user){
        System.out.println("user: " + user);
        users.add(user);
        return "redirect:/list";
    }

    @GetMapping("list")
    public String list(Model model) {
        model.addAttribute("users", users);
        return "list";
    }

    @GetMapping("/user/{userId}/profile")
    public String profile(@PathVariable String userId ,Model model){
        for (User user:users) {
            if (user.getUserId().equals(userId)){
                model.addAttribute("user", user);
            }
        }
        return "profile";
    }

}
