package reforme.reforme.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/{path:[^\\.]*}") // 이 정규식은 마침표를 포함하지 않는 모든 경로에 일치합니다.
    public String home() {
        return "index";
    }


}
