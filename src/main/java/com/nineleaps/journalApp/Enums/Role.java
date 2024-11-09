package com.nineleaps.journalApp.Enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.nineleaps.journalApp.Enums.Permissions.*;


@Getter
@RequiredArgsConstructor
public enum Role {

    NORMAL_USER(Set.of(
            USER_CREATE,
            USER_DELETE,
            USER_UPDATE,
            USER_READ)),

    ADMIN_USER(Set.of(
            ADMIN_READ,
            ADMIN_CREATE,
            ADMIN_DELETE,
            ADMIN_UPDATE,
            USER_CREATE,
            USER_DELETE,
            USER_UPDATE,
            USER_READ)),

    GUEST_USER(Collections.singleton(GUEST_READ));

    private final Set<Permissions> permissions;

    public List<SimpleGrantedAuthority> getAuthorities(){
        List<SimpleGrantedAuthority> authorities = new ArrayList<>(getPermissions().stream()
                .map(perm -> new SimpleGrantedAuthority(perm.name())).toList());

        authorities.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return authorities;
    }

}
