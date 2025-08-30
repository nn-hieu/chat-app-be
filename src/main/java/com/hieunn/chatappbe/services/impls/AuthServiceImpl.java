package com.hieunn.chatappbe.services.impls;

import com.hieunn.chatappbe.dtos.responses.LoginResponse;
import com.hieunn.chatappbe.dtos.responses.UserDTO;
import com.hieunn.chatappbe.entities.User;
import com.hieunn.chatappbe.mappers.UserMapper;
import com.hieunn.chatappbe.repositories.UserRepository;
import com.hieunn.chatappbe.services.AuthService;
import com.hieunn.chatappbe.utils.JwtUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    JwtUtil jwtUtil;
    UserRepository userRepository;
    UserMapper userMapper;

    @Override
    public LoginResponse login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong username or password"));

        if (!user.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong username or password");
        }

        UserDTO userDTO = userMapper.toUserDTO(user);
        userDTO.setIsOnline(true);

        String token = jwtUtil.generateToken(userDTO);

        return LoginResponse.builder()
                .token(token)
                .user(userDTO)
                .build();
    }
}
