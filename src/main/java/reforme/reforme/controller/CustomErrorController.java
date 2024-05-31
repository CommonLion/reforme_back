package reforme.reforme.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        // 로깅 또는 다른 처리를 추가할 수 있습니다.
        return "error"; // error.html을 렌더링
    }

}