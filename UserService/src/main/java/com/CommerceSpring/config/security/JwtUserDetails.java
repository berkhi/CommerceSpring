package com.CommerceSpring.config.security;

import com.CommerceSpring.RabbitMQ.Model.EmailAndPasswordModel;
import com.CommerceSpring.RabbitMQ.Model.UserRoleListModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtUserDetails implements UserDetailsService {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public UserDetails loadByTokenId(UUID authId){
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        List<String> rolesRabbit = getRolesRabbit(authId);

        rolesRabbit.forEach(roles -> grantedAuthorities.add(new SimpleGrantedAuthority(roles)));

        EmailAndPasswordModel emailAndPassword = getEmailAndPassword(authId);

        return User.builder().username(emailAndPassword.getEmail()).password("").authorities(grantedAuthorities).build(); //email ve password auth'dan çekilecek
    }

    public List<String> getRolesRabbit(UUID authId){
        UserRoleListModel userRoleListModel = (UserRoleListModel) rabbitTemplate.convertSendAndReceive("commerceSpringDirectExchange", "keyRolesByAuthId", authId);
        System.out.println(userRoleListModel + " rolelist");
        return userRoleListModel.getUserRoles();
    }

    public EmailAndPasswordModel getEmailAndPassword(UUID authId){
        return (EmailAndPasswordModel) rabbitTemplate.convertSendAndReceive("commerceSpringDirectExchange", "keyEmailAndPasswordFromAuth", authId);
    }
}
