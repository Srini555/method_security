package com.amisoftdemo.repo;

import com.amisoftdemo.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import javax.annotation.security.RolesAllowed;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {

    String QUERY = "select m from Message m where m.id = ?1";

    @Query(QUERY)
    @RolesAllowed("ROLE_ADMIN")
    Message findByIdRolesAllowed(Long id);


    @Query(QUERY)
    @Secured("ROLE_ADMIN")
    Message findByIdSecured(Long id);



    @Query(QUERY)
    @PreAuthorize("hasRole('ADMIN')")
    Message findByIdPreAuthorized(Long id);


    @Query(QUERY)
    @PostAuthorize("@authz.check(returnObject, principal?.usr ) ")
    Message findByIdPostAuthorized(Long id);

    @Query("select m from Message m where m.to.id = ?#{  principal?.usr?.id  }")
    Page<Message> findMessagesFor(Pageable pageable);


}
