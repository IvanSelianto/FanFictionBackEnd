package com.fanfiction.services;


import com.fanfiction.DTO.UserJwtDTO;
import com.fanfiction.models.ERole;
import com.fanfiction.models.Role;
import com.fanfiction.models.User;
import com.fanfiction.payload.request.EditNameRequest;
import com.fanfiction.payload.request.LoginRequest;
import com.fanfiction.payload.request.SignupRequest;
import com.fanfiction.payload.response.MessageResponse;
import com.fanfiction.repository.UserRepository;
import com.fanfiction.security.jwt.JwtUtils;
import com.fanfiction.security.securityservices.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private MailSender mailSender;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;

    public ResponseEntity<?> registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }
        saveUser(new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                signUpRequest.getPassword()));

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        if (roles.contains("ROLE_USER") || roles.contains("ROLE_ADMIN")) {
            return ResponseEntity.ok(new UserJwtDTO(jwtUtils.generateJwtTokenByAuthentication(authentication),
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    roles));
        }
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error: Please verify your email!"));

    }

    private void saveUser(User user) {
        user.setRoles(Collections.singleton(new Role(3, ERole.ROLE_UNDEFINED_USER)));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActivationCode(UUID.randomUUID().toString());
        userRepository.save(user);
        String message = "Hello, " + user.getUsername() + "!" + "\n" +
                "Welcome to Fanfiction. Please visit the next link: http://localhost:8080/api/auth/activate/" + user.getActivationCode();
        mailSender.send(user.getEmail(), "Activation code", message);

    }


    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);
        if (user == null) {
            return false;
        }
        user.getRoles().clear();
        user.getRoles().add(new Role(2, ERole.ROLE_USER));
        user.setActivationCode(null);
        userRepository.save(user);
        return true;
    }

    public ResponseEntity<?> editUsername(EditNameRequest editNameRequest) {
        if (userRepository.existsByUsername(editNameRequest.getEditedName())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }
        User changedUser = userRepository.findById(editNameRequest.getUserId()).get();
        changedUser.setUsername(editNameRequest.getEditedName());
        userRepository.save(changedUser);
        return ResponseEntity.ok(new UserJwtDTO(jwtUtils.generateJwtTokenByUsername(editNameRequest.getEditedName())));

    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(user);
    }

}
