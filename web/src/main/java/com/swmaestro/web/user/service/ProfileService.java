package com.swmaestro.web.user.service;

import com.swmaestro.web.auth.JwtProvider;
import com.swmaestro.web.ResponseStatus;
import com.swmaestro.web.user.domain.UserEntity;
import com.swmaestro.web.user.domain.UserRepository;
import com.swmaestro.web.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;

@Service
public class ProfileService {
    private final UserRepository userRepository;

    private final JwtProvider jwtProvider;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public ProfileService (UserRepository userRepository, JwtProvider jwtProvider, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO.ProfileResponse getInfo(String token) {
        String username = this.jwtProvider.getUsername(token);
        UserEntity user = this.userRepository.findByUsername(username);
        return UserDTO.ProfileResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }

    public UserDTO.GeneralResponse editInfo(String username, UserDTO editedInfo) {
        // logic
        UserEntity user = this.userRepository.findByUsername(username);
        try {
            // 바뀐 부분만 수정하는 것도 고려
            user.setEmail(editedInfo.getEmail());
            user.setUsername(editedInfo.getUsername());
            user.setPhone(editedInfo.getPhone());
            // encode
            user.setPassword(this.passwordEncoder.encode(editedInfo.getPassword()));

            // update
            this.userRepository.save(user);

        } catch (DataIntegrityViolationException | ConstraintViolationException de) {
            de.printStackTrace();
            String message = "";

            return UserDTO.GeneralResponse.builder().message(message).status(ResponseStatus.ERROR).build();
        } catch (Exception e) {
            e.printStackTrace();
            return UserDTO.GeneralResponse.builder().status(ResponseStatus.ERROR).build();
        }
        return UserDTO.GeneralResponse.builder().status(ResponseStatus.OK).build();
    }
}
