package dev.hugofaria.erudioapi.service;

import dev.hugofaria.erudioapi.exception.ResourceNotFoundException;
import dev.hugofaria.erudioapi.model.Person;
import dev.hugofaria.erudioapi.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class PersonService {

    final PersonRepository repository;

    private final Logger logger = Logger.getLogger(PersonService.class.getName());

    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }

    public List<Person> findAll() {

        logger.info("Finding all people!");

        return repository.findAll();
    }

    public Person findById(UUID id) {

        logger.info("Finding one person!");

        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
    }

    public Person create(Person person) {

        logger.info("Creating one person!");

        return repository.save(person);
    }

    public Person update(Person person) {

        logger.info("Updating one person!");

        var entity = repository.findById(person.getPersonId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        return repository.save(person);
    }

    public void delete(UUID id) {

        logger.info("Deleting one person!");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        repository.delete(entity);
    }
}