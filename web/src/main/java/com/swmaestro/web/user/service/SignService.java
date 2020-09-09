package com.swmaestro.web.user.service;

import com.swmaestro.web.auth.JwtProvider;
import com.swmaestro.web.ResponseStatus;
import com.swmaestro.web.user.dto.UserDTO;
import com.swmaestro.web.user.domain.UserEntity;
import com.swmaestro.web.user.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;

@Service
public class SignService implements UserDetailsService {

    private final UserRepository userRepository;

    private final JwtProvider jwtProvider;

    private final MailService mailService;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public SignService(UserRepository userRepository, JwtProvider jwtProvider,
                       MailService mailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDetails loadUserByUsername(String username) {
        UserEntity user = this.userRepository.findByUsername(username);
        return user;
    }

    public UserDTO.GeneralResponse signUpService(UserDTO userDTO) {
        try {
            UserEntity user = UserEntity.builder()
                    .email(userDTO.getEmail())
                    .password(this.passwordEncoder.encode(userDTO.getPassword()))
                    .username(userDTO.getUsername())
                    .phone(userDTO.getPhone())
                    .build();

            this.userRepository.save(user);
            return UserDTO.GeneralResponse.builder().status(ResponseStatus.OK).build();
        } catch (ConstraintViolationException | DataIntegrityViolationException ce) {
            // if constraint exception occurred (username, email)
            ce.printStackTrace(); // temp
            String message = "";
            UserEntity u =
                    this.userRepository.findUserEntityByEmailOrUsername(userDTO.getEmail(), userDTO.getUsername());
            if (u.getEmail().equals(userDTO.getEmail())) {
                message = "Email Duplicated";
            } else if (u.getUsername().equals(userDTO.getUsername())) {
                message = "Username Duplicated";
            }
            return UserDTO.GeneralResponse.builder()
                    .message(message)
                    .status(ResponseStatus.ERROR).build();
        } catch (Exception e) {
            // handle
            e.printStackTrace();
            return UserDTO.GeneralResponse.builder().status(ResponseStatus.ERROR).build();
        }

    }

    public UserDTO.LoginResponse singInService(String email, String password) {
        // email search
        UserEntity user = this.userRepository.findByEmail(email);
        if (user == null) {
            return UserDTO.LoginResponse.builder().status(ResponseStatus.ERROR).build(); // temp
        }
        // password check (with encoded)
        if (!this.matchesPassword(password, user.getPassword())) {
            return UserDTO.LoginResponse.builder().status(ResponseStatus.ERROR).build();
        }
        // create token
        String token = this.jwtProvider.createToken(user.getUsername());
        // return response
        return UserDTO.LoginResponse.builder().token(token).build();
    }

    public String getUsername(String token) {
        return this.jwtProvider.getUsername(token);
    }

    public UserDTO.GeneralResponse resetPassword(String email) {
        // reset password logic
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            // temp
            return UserDTO.GeneralResponse.builder().status(ResponseStatus.ERROR).build();
        }
        // send email for new password
        this.mailService.sendEmail(email);

        return UserDTO.GeneralResponse.builder().status(ResponseStatus.OK).build();
    }

    private boolean matchesPassword(String userPassword, String encodedPassword) {
        if (this.passwordEncoder.matches(userPassword, encodedPassword)) {
            return true;
        }
        else return false;
    }
}
