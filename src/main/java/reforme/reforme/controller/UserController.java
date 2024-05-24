package reforme.reforme.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reforme.reforme.dto.ResponseBody;
import reforme.reforme.dto.UserDto;
import reforme.reforme.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @GetMapping("/signup")
    public ResponseBody<?> singupForm() {
        return new ResponseBody<>(HttpStatus.OK.value());
    }

//    @GetMapping("/signup/check")
//    public String signupCkeck() {
//
//    }

    @PostMapping("/signup")
    public ResponseBody<?> signup(@Valid @RequestBody UserDto userDto, BindingResult result) {

        if (result.hasErrors()) {
            return new ResponseBody<String>(HttpStatus.BAD_REQUEST.value());
        }

        userService.join(userDto);
        return new ResponseBody<>(HttpStatus.OK.value(), userDto.getUserId());
    }

    @GetMapping("/signin")
    public ResponseBody<?> signinForm() {
        return new ResponseBody<>(HttpStatus.OK.value());
    }
}
