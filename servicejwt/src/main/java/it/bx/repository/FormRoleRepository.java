package it.bx.repository;

import it.bx.bean.FormRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormRoleRepository extends JpaRepository<FormRole, Long> {
    FormRole findByName(String name);
}
