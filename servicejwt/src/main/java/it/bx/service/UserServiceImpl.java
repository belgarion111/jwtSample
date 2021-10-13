package it.bx.service;


import it.bx.bean.FormRole;
import it.bx.bean.FormUser;
import it.bx.repository.FormUserRepository;
import it.bx.repository.FormRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService , UserDetailsService {

    private final FormUserRepository formUserRepository;
    private final FormRoleRepository formRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public FormUser saveUser(FormUser user) {
        log.info("Save username {}" , user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return formUserRepository.save(user);
    }

    @Override
    public FormRole saveRole(FormRole role) {
        log.info("Save role {}" , role.getName());
        return formRoleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String role, String username) {
        log.info("Adding role {} to the user {}" , role , username);
        var formUser = formUserRepository.findByUsername(username);
        var formRole = formRoleRepository.findByName(role);
        formUser.getRoles().add(formRole);
    }

    @Override
    public FormUser getFormUser(String username) {
        log.info("Find username {}" , username);
        return formUserRepository.findByUsername(username);
    }

    @Override
    public ImmutableList<FormUser> getUsers() {
        return Lists.immutable.fromStream(formUserRepository.findAll().stream());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = formUserRepository.findByUsername(username);
        if(user == null){
            log.error("User {} not found" , username);
            throw  new UsernameNotFoundException("User not found");
        }
        log.info("Username {} found" , username);
        var roles = new ArrayList<GrantedAuthority>();
        user.getRoles().forEach(e->roles.add(new SimpleGrantedAuthority(e.getName())));
        return new User(user.getUsername() , user.getPassword() , roles);
    }
}
