package com.amisoftdemo;

import com.amisoftdemo.entity.Authority;
import com.amisoftdemo.entity.Message;
import com.amisoftdemo.entity.User;
import com.amisoftdemo.repo.AuthorityRepository;
import com.amisoftdemo.repo.MessageRepository;
import com.amisoftdemo.repo.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.function.Function;

@Transactional
@Component
@Log4j2
public class Runner implements ApplicationRunner {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final AuthorityRepository authorityRepository;
    @Autowired
    private final MessageRepository messageRepository;
    @Autowired
    private final UserDetailsService userDetailsService;

    public Runner(UserRepository userRepository, AuthorityRepository authorityRepository, MessageRepository messageRepository, UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.messageRepository = messageRepository;
        this.userDetailsService = userDetailsService;
    }


    private void authenticate(String username){

        UserDetails user = this.userDetailsService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user,user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

    }


    private void attemptAccess(String adminUser, String regularUser, Long msgId, Function<Long, Message> fn) {

        authenticate(adminUser);
        log.info("Result for " + adminUser+ ":"+fn.apply(msgId));

        try {
            authenticate(regularUser);
            log.info("Result for job :" + fn.apply(msgId));
        }catch(Throwable e){
            log.error("opps ! could not obtain the result for job");
        }
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {


        Authority user = this.authorityRepository.save(new Authority("USER")),
                admin = this.authorityRepository.save(new Authority("ADMIN"));

        User amisoft = this.userRepository.save(new User("amisoft", "password1", admin, user));
        Message messageAmisoft = this.messageRepository.save(new Message("Hi Amisoft !", amisoft));

        User job = this.userRepository.save(new User("job", "password2", user));

        this.messageRepository.save(new Message("Hi 1", job));
        this.messageRepository.save(new Message("Hi 2", job));

        this.messageRepository.save(new Message("Hi 1", amisoft));



        log.info("Amisoft" +amisoft.toString());
        log.info("job" +job.toString());

        attemptAccess(amisoft.getEmail(),job.getEmail(),messageAmisoft.getId(), this.messageRepository::findByIdRolesAllowed);

        attemptAccess(amisoft.getEmail(),job.getEmail(),messageAmisoft.getId(), this.messageRepository::findByIdSecured);

        attemptAccess(amisoft.getEmail(),job.getEmail(),messageAmisoft.getId(), this.messageRepository::findByIdPreAuthorized);

        attemptAccess(amisoft.getEmail(),job.getEmail(),messageAmisoft.getId(), this.messageRepository::findByIdPostAuthorized);


        authenticate(job.getEmail());
        this.messageRepository.findMessagesFor(PageRequest.of(0, 5)).forEach(log::info);

        authenticate(amisoft.getEmail());
        this.messageRepository.findMessagesFor(PageRequest.of(0, 5)).forEach(log::info);

        log.info("audited: "
                + this.messageRepository.save(new Message("this is a test", job)));

    }
}
