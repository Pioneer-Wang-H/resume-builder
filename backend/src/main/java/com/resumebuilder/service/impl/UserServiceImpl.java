package com.resumebuilder.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.resumebuilder.dto.LoginDTO;
import com.resumebuilder.dto.RegisterDTO;
import com.resumebuilder.entity.User;
import com.resumebuilder.mapper.UserMapper;
import com.resumebuilder.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public void register(RegisterDTO dto) {
        long count = userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, dto.getUsername()));
        if (count > 0) {
            throw new RuntimeException("用户名已存在");
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(BCrypt.hashpw(dto.getPassword()));
        user.setNickname(dto.getNickname() != null ? dto.getNickname() : dto.getUsername());
        user.setEmail(dto.getEmail());
        userMapper.insert(user);
    }

    @Override
    public Map<String, Object> login(LoginDTO dto) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, dto.getUsername()));
        if (user == null || !BCrypt.checkpw(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        StpUtil.login(user.getId());
        Map<String, Object> result = new HashMap<>();
        result.put("token", StpUtil.getTokenValue());
        result.put("user", Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "nickname", user.getNickname(),
                "email", user.getEmail()
        ));
        return result;
    }

    @Override
    public void logout() {
        StpUtil.logout();
    }
}
