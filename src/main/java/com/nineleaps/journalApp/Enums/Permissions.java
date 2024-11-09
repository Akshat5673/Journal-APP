package com.nineleaps.journalApp.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor

public enum Permissions {
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),

    USER_READ("user:read"),
    USER_UPDATE("user:update"),
    USER_CREATE("user:create"),
    USER_DELETE("user:delete"),

    GUEST_READ("guest:read");

    private final String permissions;
}
