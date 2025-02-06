package se.lexicon.g52todoapi.service.impl;

import org.springframework.stereotype.Service;
import se.lexicon.g52todoapi.domain.dto.RoleDTOView;
import se.lexicon.g52todoapi.domain.dto.UserDTOForm;
import se.lexicon.g52todoapi.domain.dto.UserDTOView;
import se.lexicon.g52todoapi.domain.entity.Role;
import se.lexicon.g52todoapi.domain.entity.User;
import se.lexicon.g52todoapi.repository.RoleRepository;
import se.lexicon.g52todoapi.repository.UserRepository;
import se.lexicon.g52todoapi.service.UserService;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    //Todo: Add the required dependencies

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDTOView register(UserDTOForm userDTOForm) {
        //Todo: Implement the method
       if(userRepository.existsByEmail(userDTOForm.getEmail())) throw new RuntimeException("Email aleeady available"); // TODO: Validate that there is an email address and not null

        Set<Role> roles = userDTOForm.getRoles()
                .stream()
                .map((roleDtoForm) ->
                        roleRepository.findById(roleDtoForm.getId())
                                .orElseThrow(() -> new RuntimeException("Role is not Valid"))
                )
                .collect(Collectors.toSet());


        User user = User.builder()
                .email(userDTOForm.getEmail())
                .password(userDTOForm.getPassword())
                .roles(roles)
                .build();


        User savedUser = userRepository.save(user);


        Set<RoleDTOView> roleDTOViews =
                savedUser.getRoles().stream()
                        .map(
                                role -> RoleDTOView.builder()
                                        .id(role.getId())
                                        .name(role.getName())
                                        .build()
                        )
                        .collect(Collectors.toSet());

        UserDTOView userDTOView = UserDTOView.builder()
                .email(user.getEmail())
                .roles(roleDTOViews)
                .build();


        return userDTOView;
    }

    @Override
    public UserDTOView getByEmail(String email) {
        //Todo: Implement the method

        User user = userRepository.findById(email).orElseThrow(() -> new RuntimeException("Email  no exist"));


        Set<RoleDTOView> roleDTOViews =
                user.getRoles().stream()
                        .map(
                                role -> RoleDTOView.builder()
                                        .id(role.getId())
                                        .name(role.getName())
                                        .build()
                        )
                        .collect(Collectors.toSet());


        UserDTOView userDTOView= UserDTOView.builder().email(user.getEmail()).roles(roleDTOViews).build();
        return userDTOView;
    }

    @Override
    public void disableByEmail(String email) {
        //Todo: Implement the method

      userRepository.updateExpiredByEmail(email, true);

    }

    @Override
    public void enableByEmail(String email) {
        //Todo: Implement the method
      userRepository.updateExpiredByEmail(email, false);
    }
}
