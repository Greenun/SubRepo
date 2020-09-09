package com.swmaestro.web.user.domain;

import com.swmaestro.web.auth.UserRoles;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Table(name="user_information")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Builder
@Setter
@Getter
public class UserEntity implements UserDetails {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email", unique = true)
    @Email
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "created_time")
    @CreationTimestamp
    private LocalDateTime createdTime;

//    @ElementCollection(fetch = FetchType.EAGER)
//    @Builder.Default
//    private List<UserRoles> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        // temp
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // temp
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // temp
        return true;
    }

    @Override
    public boolean isEnabled() {
        // temp
        return true;
    }

}
