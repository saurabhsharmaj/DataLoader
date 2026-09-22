package com.ebit.loader.controller;

import com.ebit.loader.model.UserRequest;
import com.ebit.loader.service.IgniteService;
import org.apache.ignite.client.IgniteClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class HomeController {



    @Autowired
    IgniteService igniteService;

    @PostMapping("/{id}")
    public ResponseEntity<String> save(
            @PathVariable Integer id,
            @RequestBody UserRequest request) {

        igniteService.saveUser(request);
        return ResponseEntity.ok("User saved");
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRequest> get(
            @PathVariable Integer id) {
        return ResponseEntity.ok(igniteService.getUser(id));
    }

    @PostMapping("/stream")
    public ResponseEntity<String> streamUsers(
            @RequestBody List<UserRequest> users) {

        igniteService.streamUsers(users);

        return ResponseEntity.ok(
                "Streamed " + users.size() + " users"
        );
    }

    @GetMapping("/stream")
    public ResponseEntity<List<UserRequest>> getUsers() {
        return ResponseEntity.ok(igniteService.getStreamUsers());
    }
}