package se.lexicon.g52todoapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.lexicon.g52todoapi.domain.dto.PersonDTOForm;
import se.lexicon.g52todoapi.domain.dto.PersonDTOView;
import se.lexicon.g52todoapi.service.PersonService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {
    //Todo: Implement Controller

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public ResponseEntity<PersonDTOView> doCreate(@RequestBody PersonDTOForm personDTOForm) {
         PersonDTOView personDTOView=personService.create(personDTOForm);
         return ResponseEntity.status(HttpStatus.CREATED).body(personDTOView);

     }


     @GetMapping("/{id}")
    public ResponseEntity<PersonDTOView> dofindById(@PathVariable long id) {
        PersonDTOView personDTOView=personService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(personDTOView);
     }

     @GetMapping
    public ResponseEntity<List<PersonDTOView>> dofindAll() {
        List<PersonDTOView> personDTOViews=personService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(personDTOViews);
     }

     @PutMapping
    public ResponseEntity<PersonDTOView> doUpdate(@RequestBody PersonDTOForm personDTOForm) {
        PersonDTOView personDTOView=personService.update(personDTOForm);
        return ResponseEntity.status(HttpStatus.OK).body(personDTOView);
     }


     @DeleteMapping
    public ResponseEntity<Void> doDelete(@RequestParam("id") long id) {
        personService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
     }

}
