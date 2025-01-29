package se.lexicon.g52todoapi.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import se.lexicon.g52todoapi.domain.entity.Role;
import se.lexicon.g52todoapi.domain.entity.User;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    User user;
    Role role;

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @BeforeEach
    void setUp() {
        role = new Role("admin");
        roleRepository.save(role);
     user = new User("test@gmail.com", "password");
     user.addRole(role);
     userRepository.save(user);
    }

    @Test
    void existsByEmail() {
        assertTrue(userRepository.existsByEmail("test@gmail.com"));
    }

    @Test
    void updateExpiredByEmail() {
        userRepository.updateExpiredByEmail("test@gmail.com",true);
        testEntityManager.flush(); //Flush the persistence context to immediately synchronize changes with the database
        testEntityManager.clear();//
         User expiredUser=userRepository.findById("test@gmail.com").get();
         assertTrue(expiredUser.isExpired());
    }

    @Test
    void updatePasswordByEmail() {
        userRepository.updatePasswordByEmail("test@gmail.com","1234");
        testEntityManager.flush();
        testEntityManager.clear();
        assertEquals("1234",userRepository.findById("test@gmail.com").get().getPassword());
    }


}