package com.resumebuilder.service;

import com.resumebuilder.dto.LoginDTO;
import com.resumebuilder.dto.RegisterDTO;

import java.util.Map;

public interface UserService {
    void register(RegisterDTO dto);
    Map<String, Object> login(LoginDTO dto);
    void logout();
}
