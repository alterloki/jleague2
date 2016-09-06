package ru.jleague13.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.jleague13.entity.User;
import ru.jleague13.repository.UserDao;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ashevenkov 20.03.16 17:14.
 */
@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    private Log log = LogFactory.getLog(CustomUserDetailsService.class);

    @Autowired
    private UserDao userService;

    @Transactional(readOnly=true)
    public UserDetails loadUserByUsername(String ssoId)
            throws UsernameNotFoundException {
        log.info("Finding user by ssoid = " + ssoId);
        User user = userService.getUserByLogin(ssoId);
        log.info("User = " + user);
        if(user == null){
            throw new UsernameNotFoundException("Username not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(),
                true, true, true, true, getGrantedAuthorities(user));
    }


    private List<GrantedAuthority> getGrantedAuthorities(User user){
        log.info("Get granted authorities!");
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        if(user.isAdmin()) {
            log.info(user.getLogin() + " is Admin");
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return authorities;
    }

}