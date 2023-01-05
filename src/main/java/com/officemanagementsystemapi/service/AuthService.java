package com.officemanagementsystemapi.service;

import com.officemanagementsystemapi.json.request.LoginRequest;
import com.officemanagementsystemapi.json.request.SignupRequest;
import com.officemanagementsystemapi.json.response.JwtResponse;
import com.officemanagementsystemapi.json.response.MessageResponse;

public interface AuthService {

    JwtResponse signIn(LoginRequest loginRequest);
    MessageResponse registerUser(SignupRequest signUpRequest);
}
