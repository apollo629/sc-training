package com.inal.member.application.controller;

import com.inal.member.application.request.LoginRequest;
import com.inal.member.application.request.SignupRequest;
import org.springframework.http.ResponseEntity;

public interface AuthenticationController {

    ResponseEntity signin(LoginRequest loginRequest);

    ResponseEntity signup(SignupRequest signupRequest);
}
