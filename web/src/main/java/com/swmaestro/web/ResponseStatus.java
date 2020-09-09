package com.swmaestro.web;

public enum ResponseStatus {
    OK("ok"),
    ERROR("error");

    private String status;

    private ResponseStatus(String status) {
        this.status = status;
    }

    private String getStatus() {
        return this.status;
    }
}
