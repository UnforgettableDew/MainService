package com.unforgettable.securitypart.service;

import com.unforgettable.securitypart.entity.UserEntity;
import com.unforgettable.securitypart.model.SecurityUser;
import com.unforgettable.securitypart.repository.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserDetailsService implements UserDetailsService {
    private final ApplicationUserRepository applicationUserRepository;

    @Autowired
    public ApplicationUserDetailsService(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return SecurityUser.parseEntityUser(applicationUserRepository.findByUsername(username));
//                .orElseThrow(() -> new UsernameNotFoundException("User with username=" + username + " not found")));
    }

    public UserEntity getUserByUsername(String username){
        return applicationUserRepository.findByUsername(username);
//                .orElseThrow(() -> new UsernameNotFoundException("User with username=" + username + " not found"));
    }

    public void saveUser(UserEntity userEntity){
        applicationUserRepository.save(userEntity);
    }
}
