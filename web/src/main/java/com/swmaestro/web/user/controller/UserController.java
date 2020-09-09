package com.swmaestro.web.user.controller;

import com.swmaestro.web.user.dto.UserDTO;
import com.swmaestro.web.user.service.MailService;
import com.swmaestro.web.user.service.ProfileService;
import com.swmaestro.web.user.service.SignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private SignService signService;

    private MailService mailService;

    private ProfileService profileService;

    @Autowired
    public UserController(SignService signService, MailService mailService, ProfileService profileService) {
        this.signService = signService;
        this.mailService = mailService;
        this.profileService = profileService;
    }

    @ApiOperation(value = "로그인")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "이메일", required = true),
            @ApiImplicitParam(name = "password", value = "비밀번호", required = true)
    })
    @PostMapping("/signin")
    public UserDTO.LoginResponse userLogin(@RequestBody UserDTO user, HttpServletResponse response) {
        // create jwt token
        UserDTO.LoginResponse resp = this.signService.singInService(user.getEmail(), user.getPassword());
        logger.info(resp.toString());
        // add to cookie
//        Cookie cookie = new Cookie("token", resp.getToken());
//        cookie.setMaxAge(60 * 60 * 24);
//        cookie.setSecure(true);
//
//        response.addCookie(cookie);
//        // return jwt token as json
        return resp;
    }

    @ApiOperation(value = "회원가입")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "닉네임", required = true)
    })
    @PostMapping("/signup")
    public UserDTO.GeneralResponse userSignUp(@RequestBody UserDTO userDTO) {
        UserDTO.GeneralResponse resp = this.signService.signUpService(userDTO);
        return resp;
    }

    @ApiOperation(value = "이메일 인증", notes = "회원가입 시 이메일 인증", hidden = true)
    @GetMapping("/auth")
    public UserDTO.GeneralResponse emailAuth(@RequestParam String token) {
        return null;
    }

    @ApiOperation(value = "패스워드 리셋")
    @ApiImplicitParam(name = "email", value = "이메일", required = true)
    @GetMapping("/password_reset")
    public UserDTO.GeneralResponse resetPassword(@RequestBody String email) {
        UserDTO.GeneralResponse resp = this.mailService.sendEmail(email);
        return resp;
    }

    @ApiOperation(value = "프로필 확인", notes = "JWT 자격 필요 - 프로필 확인")
    @GetMapping("/info/profile") //
    public UserDTO.ProfileResponse viewProfile(@RequestHeader("X-AUTH-TOKEN") String token) {
        return this.profileService.getInfo(token);
    }

    @ApiOperation(value = "프로필 수정", notes = "JWT 자격 필요 - 프로필 수정하기")
    @PostMapping("/info/profile/{username}")
    public UserDTO.GeneralResponse editProfile(
            @RequestHeader("X-AUTH-TOKEN") String token, @PathVariable String username, UserDTO editedInfo) {

        return this.profileService.editInfo(username, editedInfo);
    }
}
