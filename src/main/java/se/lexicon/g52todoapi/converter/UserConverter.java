package se.lexicon.g52todoapi.converter;

import se.lexicon.g52todoapi.domain.dto.RoleDTOView;
import se.lexicon.g52todoapi.domain.dto.UserDTOForm;
import se.lexicon.g52todoapi.domain.dto.UserDTOView;
import se.lexicon.g52todoapi.domain.entity.Role;
import se.lexicon.g52todoapi.domain.entity.User;

import java.util.Set;

public interface UserConverter {

    UserDTOView toUserDto(User entity);

    User toEntity(UserDTOForm dto);


}
