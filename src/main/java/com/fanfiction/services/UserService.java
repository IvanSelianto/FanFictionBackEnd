package com.fanfiction.services;


import com.fanfiction.DTO.UserJwtDTO;
import com.fanfiction.models.ERole;
import com.fanfiction.models.Role;
import com.fanfiction.models.User;
import com.fanfiction.DTO.UserDTO;
import com.fanfiction.payload.request.LoginRequest;
import com.fanfiction.payload.request.SignupRequest;
import com.fanfiction.payload.response.MessageResponse;
import com.fanfiction.repository.RoleRepository;
import com.fanfiction.repository.UserRepository;
import com.fanfiction.security.jwt.JwtUtils;
import com.fanfiction.security.securityservices.UserDetailsImpl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.vk.api.sdk.client.Lang;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import com.vk.api.sdk.objects.users.UserXtrCounters;
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

import java.io.IOException;
import java.util.*;
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
    @Autowired
    private RoleRepository roleRepository;
    private final HttpTransport TRANSPORT = new NetHttpTransport();
    private final JacksonFactory JSON_FACTORY = new JacksonFactory();

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

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(ERole.ROLE_UNDEFINED_USER).get());
        user.setRoles(roles);


        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActivationCode(UUID.randomUUID().toString());
        userRepository.save(user);
        String message = "Hello, " + user.getUsername() + "!" + "\n" +
                "Welcome to Fanfiction. Please visit the next link: https://fanfictionback.herokuapp.com/api/auth/activate/" + user.getActivationCode();
        mailSender.send(user.getEmail(), "Activation code", message);

    }


    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);
        if (user == null) {
            return false;
        }
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(ERole.ROLE_UNDEFINED_USER).get());
        user.setRoles(roles);
        user.setActivationCode(null);
        userRepository.save(user);
        return true;
    }

    public ResponseEntity<?> editUsername(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getEditedName())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }
        User changedUser = userRepository.findById(userDTO.getUserId()).get();
        changedUser.setUsername(userDTO.getEditedName());
        userRepository.save(changedUser);
        return ResponseEntity.ok(new UserJwtDTO(jwtUtils.generateJwtTokenByUsername(userDTO.getEditedName())));

    }

    public ResponseEntity<UserJwtDTO> authVk(String vkCode) throws ClientException, ApiException {
        TransportClient transportClient = new HttpTransportClient();
        VkApiClient vk = new VkApiClient(transportClient);
        UserAuthResponse authResponse = vk.oauth()
                .userAuthorizationCodeFlow(7547605, "Z6R21r7OHIFj1uewDnvL", "https://fanfictionang.herokuapp.com/vk/auth", vkCode)
                .execute();
        UserXtrCounters authUser = vk.users()
                .get(new UserActor(authResponse.getUserId(), authResponse.getAccessToken()))
                .fields()
                .lang(Lang.EN)
                .execute().get(0);
        String authUsername = authUser.getFirstName() + " " + authUser.getLastName();
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(ERole.ROLE_USER).get());
        return oauthUser(authUsername, authResponse.getEmail(), roles);
    }

    public ResponseEntity<UserJwtDTO> authGoogle(String googleCode) throws IOException {
        GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(TRANSPORT, JSON_FACTORY,
                "968324244976-63q9m9n4c267ps7gamlopfct7v45qner.apps.googleusercontent.com"
                , "qelBn7c0E_nUEOzConvw-YP8"
                , "4/" + googleCode, "https://fanfictionang.herokuapp.com/google/auth").execute();
        GoogleIdToken idToken = tokenResponse.parseIdToken();
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(ERole.ROLE_USER).get());
        return oauthUser(
                idToken.getPayload().get("given_name") + " " + idToken.getPayload().get("family_name"),
                idToken.getPayload().getEmail(),
                roles
        );


    }


    private ResponseEntity<UserJwtDTO> oauthUser(String authUsername, String email, Set<Role> roles) {
        if (!userRepository.existsByUsername(authUsername)) {
            User user = new User(authUsername, email, bCryptPasswordEncoder.encode("auth"));
            user.setRoles(roles);
            User savedUser = userRepository.save(user);
            return ResponseEntity.ok(new UserJwtDTO(jwtUtils.generateJwtTokenByUsername(authUsername),
                    savedUser.getId(),
                    authUsername,
                    user.getEmail(),
                    roles.stream().map(role -> role.getName().toString()).collect(Collectors.toList()))
            );
        }
        User user = userRepository.findByUsername(authUsername).get();
        return ResponseEntity.ok(new UserJwtDTO(jwtUtils.generateJwtTokenByUsername(user.getUsername()),
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles().stream().map(role -> role.getName().toString()).collect(Collectors.toList())
        ));
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(user);
    }

}
