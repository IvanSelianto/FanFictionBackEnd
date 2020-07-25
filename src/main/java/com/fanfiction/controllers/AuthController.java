package com.fanfiction.controllers;

import com.fanfiction.DTO.UserJwtDTO;
import com.fanfiction.payload.request.LoginRequest;
import com.fanfiction.payload.request.SignupRequest;
import com.fanfiction.repository.RoleRepository;
import com.fanfiction.repository.UserRepository;
import com.fanfiction.security.jwt.JwtUtils;
import com.fanfiction.services.UserService;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;


@RestController
@RequestMapping("/api/auth")

public class AuthController {

    @Autowired
    private UserService userService;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return userService.authenticateUser(loginRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return userService.registerUser(signUpRequest);

    }

    @GetMapping("/activate/{code}")
    public void activate(@PathVariable String code, HttpServletResponse response) throws IOException {
        userService.activateUser(code);
        response.sendRedirect("https://fanfictionang.herokuapp.com/");
    }


    @GetMapping("/authvk/{vkcode}")
    public ResponseEntity<UserJwtDTO> authVk(@PathVariable String vkcode) throws ClientException, ApiException {
        return userService.authVk(vkcode);
    }

    @GetMapping("/authgoogle/{googlecode}")
    public ResponseEntity<UserJwtDTO> authGoogle(@PathVariable String googlecode) throws IOException {
        return userService.authGoogle(googlecode);
    }


}
