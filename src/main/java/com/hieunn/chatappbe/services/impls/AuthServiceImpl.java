package com.hieunn.chatappbe.services.impls;

import com.hieunn.chatappbe.dtos.responses.UserDTO;
import com.hieunn.chatappbe.entities.User;
import com.hieunn.chatappbe.mappers.UserMapper;
import com.hieunn.chatappbe.repositories.UserRepository;
import com.hieunn.chatappbe.services.AuthService;
import com.hieunn.chatappbe.utils.JwtUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    JwtUtil jwtUtil;
    UserRepository userRepository;
    UserMapper userMapper;

    @Override
    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        if (!user.getPassword().equals(password)) {
            throw new BadCredentialsException("Wrong password");
        }

        return jwtUtil.generateToken(userMapper.toUserDTO(user));
    }
}
