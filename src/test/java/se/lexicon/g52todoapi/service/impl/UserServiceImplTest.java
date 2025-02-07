package se.lexicon.g52todoapi.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import se.lexicon.g52todoapi.domain.dto.RoleDTOForm;
import se.lexicon.g52todoapi.domain.dto.UserDTOForm;
import se.lexicon.g52todoapi.domain.dto.UserDTOView;
import se.lexicon.g52todoapi.domain.entity.Role;
import se.lexicon.g52todoapi.domain.entity.User;
import se.lexicon.g52todoapi.repository.RoleRepository;
import se.lexicon.g52todoapi.repository.UserRepository;
import se.lexicon.g52todoapi.util.CustomPasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private CustomPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void register() {
        //set the  mock data
        UserDTOForm userDTOForm=new UserDTOForm("test@gmail.com","12356", Set.of(new RoleDTOForm(1L,"ADMIN")));
        Role role=new Role(1L,"ADMIN");
        Set<Role> roles=Set.of(role);

        //mock repos
        when(userRepository.existsByEmail("test@gmail.com")).thenReturn(false);

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role)); // Role 1 exists
        when(passwordEncoder.encode("12356")).thenReturn("encodedPassword");

        User user =User.builder().email("test@gmail.com").password("encodedPassword").roles(roles).build();

        when(userRepository.save(user)).thenReturn(user);

        // Call the method
        UserDTOView userDTOView = userService.register(userDTOForm);

        assertNotNull(userDTOView);
        assertEquals("test@gmail.com", userDTOView.email());
    }

    @Test
    void authorizeUser() {
    }

    @Test
    void getByEmail() {

        String email="sayana@gmail.com";
        Role role=new Role(1L,"ADMIN");
        Set<Role> roles=Set.of(role);
        User user=User.builder().email(email).password("encodedPassword").roles(roles).build();

        when(userRepository.findById("sayana@gmail.com")).thenReturn(Optional.of(user));

        UserDTOView userDTOView=userService.getByEmail(email);

        assertNotNull(userDTOView);
        assertEquals("sayana@gmail.com", userDTOView.email());
    }

    @Test
    void disableByEmail() {
        String email="sayana@gmail.com";

        //mock repository
        when(userRepository.existsByEmail(email)).thenReturn(true);

        userService.disableByEmail(email);

        verify(userRepository).existsByEmail(email);
        verify(userRepository).updateExpiredByEmail(email,true);
    }

    @Test
    void enableByEmail() {
        String email="sayana@gmail.com";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        userService.enableByEmail(email);

        verify(userRepository).existsByEmail(email);
        verify(userRepository).updateExpiredByEmail(email,false);
    }

    // Todo: Implement Mock Tests (EXTRA)

}