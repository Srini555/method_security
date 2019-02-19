package com.amisoftdemo.config;

import com.amisoftdemo.entity.User;
import com.amisoftdemo.repo.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserRepositoryUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;

    public UserRepositoryUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User usr = this.userRepository.findByEmail(username);

        if(null != usr){

            return new UserUserDetails(usr);

        }else
            throw new UsernameNotFoundException("could not find " + username + " ! ");

    }
}
