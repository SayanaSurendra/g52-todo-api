package se.lexicon.g52todoapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.lexicon.g52todoapi.domain.dto.PersonDTOForm;
import se.lexicon.g52todoapi.domain.dto.PersonDTOView;
import se.lexicon.g52todoapi.domain.dto.UserDTOForm;
import se.lexicon.g52todoapi.domain.entity.Person;
import se.lexicon.g52todoapi.domain.entity.User;
import se.lexicon.g52todoapi.exception.DataNotFoundException;
import se.lexicon.g52todoapi.repository.PersonRepository;
import se.lexicon.g52todoapi.repository.UserRepository;
import se.lexicon.g52todoapi.service.PersonService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final UserRepository userRepository;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository, UserRepository userRepository) {
        this.personRepository = personRepository;
        this.userRepository = userRepository;
    }

    @Override
    public PersonDTOView create(PersonDTOForm personDTOForm) {
        User user=null;
        if(userRepository.existsByEmail(personDTOForm.user().email())){
             user=userRepository.findById(personDTOForm.user().email()).get();
        };
        Person person = Person.builder().name(personDTOForm.name()).user(user).build();
        person = personRepository.save(person);
        return PersonDTOView.builder()
                .id(person.getId())
                .name(person.getName())
                .build();
    }

    @Override
    public PersonDTOView findById(Long id) {
        Person person = personRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Person id is not valid."));
        return PersonDTOView.builder().id(person.getId()).name(person.getName()).build();
    }

    @Override
    public List<PersonDTOView> findAll() {
        return personRepository.findAll().stream()
                .map(person -> PersonDTOView.builder()
                        .id(person.getId())
                        .name(person.getName())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PersonDTOView update(PersonDTOForm personDTOForm) {
        Person person = personRepository.findById(personDTOForm.id()).orElseThrow(() -> new DataNotFoundException("Person Id is not valid."));
        person.setName(personDTOForm.name());
        return PersonDTOView.builder().id(person.getId()).name(person.getName()).build();
    }

    @Override
    public void delete(Long id) {
        personRepository.deleteById(id);
    }

}
