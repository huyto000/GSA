package com.example.GemSkillAssessment.config.security;

import com.example.GemSkillAssessment.dao.UserRepository;
import com.example.GemSkillAssessment.enumerted.EError;
import com.example.GemSkillAssessment.error.NotFoundException;
import com.example.GemSkillAssessment.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new NotFoundException(EError.USER_NOT_FOUND.getLabel());
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        if (user.isAdmin()) grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        else grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), user.isEnabled(), true, true, true, grantedAuthorities
        );
    }
}
