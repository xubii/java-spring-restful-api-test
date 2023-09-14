package uk.co.huntersix.spring.rest.referencedata;

import org.springframework.stereotype.Service;
import uk.co.huntersix.spring.rest.dto.PersonDto;
import uk.co.huntersix.spring.rest.exception.PersonAlreadyExistException;
import uk.co.huntersix.spring.rest.exception.PersonNotFoundException;
import uk.co.huntersix.spring.rest.model.Person;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonDataService {
    public static final List<Person> PERSON_DATA = Arrays.asList(
        new Person("Mary", "Smith"),
        new Person("Brian", "Archer"),
        new Person("Collin", "Brown")
    );

    public Person findPerson(String lastName, String firstName) {
        return PERSON_DATA.stream()
            .filter(p -> p.getFirstName().equalsIgnoreCase(firstName)
                && p.getLastName().equalsIgnoreCase(lastName))
                .findFirst().orElseThrow(() -> new PersonNotFoundException("person not found"));
    }

    public List<Person> findPersonsByLastName(String lastName) {
        List<Person> persons = PERSON_DATA.stream()
                .filter(p -> p.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());

        if (persons.isEmpty()) {
            throw new PersonNotFoundException("person not found");
        }

        return persons;
    }

    public PersonDto addPerson(PersonDto personDto) {
        Person person = personDto.mapToPerson();

        if(PERSON_DATA.contains(person)) {
            throw new PersonAlreadyExistException("person already exist");
        }

        PERSON_DATA.add(person);
        return personDto.mapToPersonDto(person.getId());
    }
}
