package com.example.server.web.controller;

import com.example.server.service.UserService;
import com.example.server.web.dto.UserSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserApiController {
    private final UserService userService;


    @PostMapping("/signup")
    public void saveUser(@RequestBody UserSaveRequestDto userSaveRequestDto){
        userService.saveUser(userSaveRequestDto);
    }

    @GetMapping("/api/user")
    public void user(){

    }

    @GetMapping("/api/admin")
    public void admin(){}
}
