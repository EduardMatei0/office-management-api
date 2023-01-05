package com.officemanagementsystemapi.service.impl;

import com.officemanagementsystemapi.configuration.security.JwtUtils;
import com.officemanagementsystemapi.configuration.security.UserDetailsImpl;
import com.officemanagementsystemapi.json.request.LoginRequest;
import com.officemanagementsystemapi.json.request.SignupRequest;
import com.officemanagementsystemapi.json.response.JwtResponse;
import com.officemanagementsystemapi.json.response.MessageResponse;
import com.officemanagementsystemapi.model.EnumRoles;
import com.officemanagementsystemapi.model.Roles;
import com.officemanagementsystemapi.model.Users;
import com.officemanagementsystemapi.repository.RolesRepository;
import com.officemanagementsystemapi.repository.UsersRepository;
import com.officemanagementsystemapi.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UsersRepository userRepository;
    private final RolesRepository roleRepository;
    private final PasswordEncoder encoder;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           JwtUtils jwtUtils,
                           UsersRepository userRepository,
                           RolesRepository roleRepository,
                           PasswordEncoder encoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    @Override
    public JwtResponse signIn(LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

    return new JwtResponse(jwt,
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getEmail(),
            roles);
    }

    @Override
    public MessageResponse registerUser(SignupRequest signUpRequest) {


        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        // Create new user's account
        Users user = new Users(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Roles> roles = new HashSet<>();

        if (strRoles == null) {
            Roles userRole = roleRepository.findFirstByName(EnumRoles.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            Set<String> allRoles = roleRepository.getAllRoles();
            strRoles.forEach(role -> {
                if (!allRoles.contains(role)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Role %s doesn't exists", role));
            });
            roles.addAll(roleRepository.findAllByNameIn(strRoles
                    .stream()
                    .map(EnumRoles::valueOf)
                    .collect(Collectors.toList())));
        }

        user.setRoles(roles);
        userRepository.save(user);

        return new MessageResponse("User registered successfully");
    }
}
