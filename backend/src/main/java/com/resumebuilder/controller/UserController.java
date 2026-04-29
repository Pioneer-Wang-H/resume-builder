package com.resumebuilder.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.resumebuilder.common.Result;
import com.resumebuilder.dto.LoginDTO;
import com.resumebuilder.dto.RegisterDTO;
import com.resumebuilder.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public Result<?> register(@Valid @RequestBody RegisterDTO dto) {
        userService.register(dto);
        return Result.ok();
    }

    @PostMapping("/login")
    public Result<?> login(@Valid @RequestBody LoginDTO dto) {
        return Result.ok(userService.login(dto));
    }

    @PostMapping("/logout")
    public Result<?> logout() {
        userService.logout();
        return Result.ok();
    }

    @GetMapping("/info")
    public Result<?> info() {
        long userId = StpUtil.getLoginIdAsLong();
        return Result.ok(Map.of("userId", userId));
    }
}
