package com.example04.service;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserEntityService userEntityService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userEntityService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("UserName: %s not found", username)));
    }

    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        return userEntityService.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Id: " + id + " not found"));
    }
}
