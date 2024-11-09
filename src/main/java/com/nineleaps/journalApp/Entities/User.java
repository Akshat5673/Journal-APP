package com.nineleaps.journalApp.Entities;

import com.nineleaps.journalApp.Enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
@Builder
public class User implements UserDetails{

    @Id
    private String id;
    @Indexed(unique = true)
    @NotBlank
    private String name;
    @NotBlank
    private String password;
    private String email;
    private boolean isSentimentAnalysisOpted;
    @DBRef
    List<JournalEntry> journalEntries = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getUsername() {
        return this.name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
