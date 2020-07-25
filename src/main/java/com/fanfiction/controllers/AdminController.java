package com.fanfiction.controllers;

import com.fanfiction.models.ERole;
import com.fanfiction.models.Role;
import com.fanfiction.models.User;
import com.fanfiction.DTO.UserJwtDTO;
import com.fanfiction.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fanfic")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/allusers")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        return adminService.getAllUsers();
    }

    @DeleteMapping("/deleteuser/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
    }

    @GetMapping("/blockuser/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void blockUser(@PathVariable Long userId) {
        adminService.setRole(userId, ERole.ROLE_UNDEFINED_USER);
    }

    @GetMapping("/setuserrole/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void unblockUser(@PathVariable Long userId) {
        adminService.setRole(userId, ERole.ROLE_USER);
    }

    @GetMapping("/setadminrole/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void setAdminRole(@PathVariable Long userId) {
        adminService.setRole(userId, ERole.ROLE_ADMIN);
    }

    @GetMapping("getuserbyid/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserJwtDTO getUserById(@PathVariable Long userId) {
        return adminService.getUserById(userId);
    }
}
