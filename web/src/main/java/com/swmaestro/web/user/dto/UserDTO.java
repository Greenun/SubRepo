package com.swmaestro.web.user.dto;

import com.swmaestro.web.ResponseStatus;
import com.swmaestro.web.user.domain.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Builder
@Getter
@ToString
public class UserDTO {

    @NotNull
    @Size(min = 1, max = 10)
    private String username;

    // 8 ~ 20 --> need to be encoded
    // @Pattern(regexp = "[a-zA-Z0-9!@#$%^&*_]{8,20}$")
    @NotNull
    private String password;

    @Email
    @Size(min = 1, max = 30)
    @NotNull
    private String email;

    @Pattern(regexp = "[0-9]{3}-[0-9]{4}-[0-9]{4}")
    private String phone;

//    private String encoded;

    // to entity method
    public UserEntity toEntity() {
        return UserEntity.builder()
                .username(username)
                .email(email)
                .password(password)
                .build();
    }

//    public void setEncoded(String encoded) {
//        this.encoded = encoded;
//    }

    @AllArgsConstructor
    @Builder
    @Getter
    public static class LoginResponse {
        private String token;

        @Builder.Default
        private ResponseStatus status = ResponseStatus.OK;
    }

    @AllArgsConstructor
    @Builder
    @Getter
    public static class GeneralResponse {

        private String message;

        @Builder.Default
        private ResponseStatus status = ResponseStatus.OK;
    }

    @AllArgsConstructor
    @Builder
    @Getter
    public static class ProfileResponse {
        @Builder.Default
        private ResponseStatus status = ResponseStatus.OK;

        private String username;

        private String email;

        private String phone;
    }

}
