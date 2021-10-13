package it.bx.repository;

import it.bx.bean.FormUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormUserRepository extends JpaRepository<FormUser, Long> {
    FormUser findByUsername(String username);
}
