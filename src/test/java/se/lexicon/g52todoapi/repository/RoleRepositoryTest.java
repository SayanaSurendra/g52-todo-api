package se.lexicon.g52todoapi.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import se.lexicon.g52todoapi.domain.entity.Role;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RoleRepositoryTest {

    Role role;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
      role=new Role("admin");
      roleRepository.save(role);
    }

    @Test
    void findByName() {
      assertNotNull(roleRepository.findByName(role.getName()).get());
      assertEquals("admin",roleRepository.findByName(role.getName()).get().getName());
    }


}