package it.bx.service;

import it.bx.bean.FormRole;
import it.bx.bean.FormUser;
import org.eclipse.collections.api.list.ImmutableList;

public interface UserService {

    FormUser saveUser(FormUser user);
    FormRole saveRole(FormRole role);
    void addRoleToUser(String role , String username);
    FormUser getFormUser(String username);
    ImmutableList<FormUser> getUsers();
}
