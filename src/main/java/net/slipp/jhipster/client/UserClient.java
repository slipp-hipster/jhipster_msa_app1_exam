package net.slipp.jhipster.client;

import com.codahale.metrics.annotation.Timed;
import net.slipp.jhipster.domain.Owner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Created by hkpking on 2017. 10. 27..
 */
@AuthorizedFeignClient(name = "uaa")
public interface UserClient {

    @Timed
    @GetMapping("/api/users")
    List<Owner> getAllUsers();

    @Timed
    @GetMapping("/api/users/{login}")
    Owner getUser(@PathVariable("login") String login);

}
