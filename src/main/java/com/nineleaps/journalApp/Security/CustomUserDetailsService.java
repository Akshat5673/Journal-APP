package com.nineleaps.journalApp.Security;


import com.nineleaps.journalApp.Exceptions.ResourceNotFoundException;
import com.nineleaps.journalApp.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // loading user db by username
        return userRepository.findByName(username)
                .orElseThrow(()-> new ResourceNotFoundException("User","name",username));
    }
}
