package reforme.reforme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import reforme.reforme.dto.ResponseBody;
import reforme.reforme.util.JwtTokenUtil;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/signin")
    public ResponseEntity<ResponseBody<String>> authenticateUser(
            @RequestParam("userId") String userId,
            @RequestParam("password") String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userId, password)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtTokenUtil.generateToken(userId);

            return ResponseEntity.ok(new ResponseBody<>(HttpStatus.OK.value(), token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseBody<>(HttpStatus.UNAUTHORIZED.value(), "로그인 실패"));
        }
    }

    @GetMapping("/api/check-session")
    public ResponseEntity<ResponseBody<String>> checkSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            return ResponseEntity.ok(new ResponseBody<>(HttpStatus.OK.value(), "세션 유효"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseBody<>(HttpStatus.UNAUTHORIZED.value(), "세션 무효"));
        }
    }
}