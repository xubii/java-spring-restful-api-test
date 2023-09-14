package uk.co.huntersix.spring.rest.dto;

import uk.co.huntersix.spring.rest.model.Person;

import java.io.Serializable;

public class PersonDto implements Serializable {

    private Long id;
    private String firstName;
    private String lastName;

    private PersonDto() {
        // empty
    }

    public PersonDto(Long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public PersonDto(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person mapToPerson() {
        return new Person(this.firstName, this.lastName);
    }

    public PersonDto mapToPersonDto(Long id) {
        return new PersonDto(id, this.firstName, this.lastName);
    }

}
