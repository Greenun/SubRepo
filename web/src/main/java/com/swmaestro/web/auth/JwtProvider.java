package com.swmaestro.web.auth;

import com.swmaestro.web.user.domain.UserRepository;
import com.swmaestro.web.user.service.SignService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtProvider {
    @Value("${spring.jwt.secret}")
    private String secretKey;

    @Value("${jwt.email.key:email-key}")
    private String emailKey;

    private long expireTime = 1000L  * 60 * 60; // 1 hour

    private UserRepository userRepository;

    @PostConstruct
    protected void init() {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    @Autowired
    public JwtProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    protected JwtParser setSign() {
        return Jwts.parser().setSigningKey(this.secretKey);
    }

    public String createToken(String username) {
        // multiple ? - username, email
        // user role ? - student, lecturer
        Claims claims = Jwts.claims().setSubject(username);
        // add role
        claims.put("role", UserRoles.USER); // add student role

        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + this.expireTime))
                .signWith(SignatureAlgorithm.HS256, this.secretKey)
                .compact();
    }

    public String getEmailAuthToken(String username) {
        Claims claims = Jwts.claims().setSubject(username);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + (1000L * 10)))
                .signWith(SignatureAlgorithm.HS256, this.emailKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        // ???
        UserDetails user = this.userRepository.findByUsername(this.getUsername(token));
        return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser()
                .setSigningKey(this.secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // parse token from http header
    public String parseToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    public boolean validationCheck(String token) {
        try {
            Jws<Claims> claims = this.setSign().parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            // logging parse error
            return false;
        }
    }
}
