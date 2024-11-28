package com.example.Baesh.Controller;

import com.example.Baesh.DTO.Oauth.IdTokenRequestDTO;
import com.example.Baesh.Service.UserServcie;
import com.google.common.net.HttpHeaders;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/oauth")
public class LoginController {

    @Autowired
    UserServcie userServcie;

    @PostMapping("/login")
    public ResponseEntity LoginWithGoogleOauth2(@RequestBody IdTokenRequestDTO requestBody, HttpServletResponse response) {
        String authToken = userServcie.loginOAuthGoogle(requestBody);
        final ResponseCookie cookie = ResponseCookie.from("AUTH-TOKEN", authToken)
                .httpOnly(true)
                .maxAge(7 * 24 * 3600)
                .path("/")
                .secure(false)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public String logout(HttpServletResponse response){
        Cookie cookie = new Cookie("AUTH-TOKEN",null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setSecure(false);
        response.addCookie(cookie);
        return "로그 아웃";
    }
}
