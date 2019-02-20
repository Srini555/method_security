package com.amisoftdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;


@EnableJpaAuditing
@EnableGlobalMethodSecurity(

		prePostEnabled = true,
		jsr250Enabled = true,
		securedEnabled = true
)

@SpringBootApplication
public class MethodSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(MethodSecurityApplication.class, args);
	}

}
