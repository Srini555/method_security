package com.amisoftdemo.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;

import java.util.Optional;

@Configuration
public class BeanConfig {

    @Bean
    SecurityEvaluationContextExtension securityEvaluationContextExtension(){
        return new SecurityEvaluationContextExtension();
    }

    @Bean
    AuditorAware<String> auditor (){


            return() -> {
                SecurityContext context = SecurityContextHolder.getContext();
                Authentication authentication = context.getAuthentication();

                if(null != authentication){
                    return Optional.ofNullable(authentication.getName());
                }
                return Optional.empty();
            };
        }
    }

