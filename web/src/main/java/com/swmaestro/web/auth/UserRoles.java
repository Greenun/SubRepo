package com.swmaestro.web.auth;

public enum UserRoles {
    USER("student"),
    ADMIN("admin");

    private String role;

    private UserRoles(String role) {
        this.role = role;
    }

    public String getRole() {
        return this.role;
    }
}
