package com.fanfiction.services;

import com.fanfiction.models.*;
import com.fanfiction.DTO.UserJwtDTO;
import com.fanfiction.repository.*;
import com.fanfiction.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdminService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CompositionRepository compositionRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    ChapterRepository chapterRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long userId) {
        if (!(userRepository.findById(userId).get().getRoles().contains(roleRepository.findByName(ERole.ROLE_ADMIN).get()))) {
            List<Composition> compositionsByAuthorId = compositionRepository.getCompositionsByAuthorId(userId);
            compositionsByAuthorId.forEach(composition -> chapterRepository.deleteAllByComposition(composition));
            commentRepository.deleteAll(commentRepository.findAllByCommentAuthorId(userId));
            compositionRepository.deleteAll(compositionsByAuthorId);
            userRepository.deleteById(userId);
        }

    }

    public void setRole(Long userId, ERole role) {

        User user = userRepository.findById(userId).get();
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(role).get());
        user.setRoles(roles);
        userRepository.save(user);
    }


    public UserJwtDTO getUserById(Long userId) {
        User user = userRepository.findById(userId).get();
        return new UserJwtDTO(jwtUtils.generateJwtTokenByUsername(user.getUsername()),
                userId,
                user.getUsername(),
                user.getEmail(),
                new ArrayList<>(user.getRoles()).stream().map(role -> role.getName().name()).collect(Collectors.toList()));
    }


}
