package com.officemanagementsystemapi.json.request;

import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest extends LoginRequest{

    private String username;
    private Set<String> roles;
}
