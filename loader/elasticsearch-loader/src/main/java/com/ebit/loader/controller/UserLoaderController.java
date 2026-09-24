package com.ebit.loader.controller;

import com.ebit.loader.loader.UserLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loader")
public class UserLoaderController {

    private final UserLoader userLoader;

    public UserLoaderController(UserLoader userLoader) {
        this.userLoader = userLoader;
    }

    @GetMapping("/elasticsearch/users")
    public ResponseEntity<String> loadUsers() {
        userLoader.loadUsers();

        return ResponseEntity.ok("User loading completed successfully");
    }
}
