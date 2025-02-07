package se.lexicon.g52todoapi.domain.dto;

import se.lexicon.g52todoapi.domain.entity.User;

public record PersonDTOForm(Long id, String name,UserDTOForm user) {
}