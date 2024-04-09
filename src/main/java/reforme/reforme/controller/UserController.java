package reforme.reforme.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import reforme.reforme.dto.UserDto;
import reforme.reforme.entity.User;
import reforme.reforme.service.UserService;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public String singupForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "user/signupForm";
    }

//    @GetMapping("/signup/check")
//    public String signupCkeck() {
//
//    }

    @PostMapping("/signup")
    public String signup(@Valid UserDto userDto, BindingResult result) {
        if (result.hasErrors()) {
            return "user/signupForm";
        }

        User user = new User();
        user.setId(userDto.getId());
        user.setPassword(userDto.getPassword());
        user.setNickname(userDto.getNickname());

        userService.join(user);
        return "redirect:/";
    }
}
