package uk.co.huntersix.spring.rest.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.co.huntersix.spring.rest.dto.PersonDto;
import uk.co.huntersix.spring.rest.exception.PersonAlreadyExistException;
import uk.co.huntersix.spring.rest.exception.PersonNotFoundException;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonDataService personDataService;

    private ObjectMapper mapper;

    @Before
    public void beforeEach() {
        mapper = new ObjectMapper();
    }

    @Test
    public void shouldReturnPersonFromService() throws Exception {
        when(personDataService.findPerson(any(), any())).thenReturn(new Person("Mary", "Smith"));
        this.mockMvc.perform(get("/person/smith/mary"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").exists())
            .andExpect(jsonPath("firstName").value("Mary"))
            .andExpect(jsonPath("lastName").value("Smith"));
    }

    @Test
    public void shouldReturnExceptionWhenNoPersonFound() throws Exception {
        when(personDataService.findPerson(any(), any())).thenThrow(PersonNotFoundException.class);
        this.mockMvc.perform(get("/person/smith/mary"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnWithNoMatch() throws Exception {
        when(personDataService.findPersonsByLastName(any())).thenThrow(PersonNotFoundException.class);
        this.mockMvc.perform(get("/person/Baig"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnAPersonWithOneMatch() throws Exception {
        when(personDataService.findPersonsByLastName(any())).thenReturn(Arrays.asList(new Person("Mary", "Smith")));
        this.mockMvc.perform(get("/person/Smith"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id").exists())
                .andExpect(jsonPath("[0].firstName").value("Mary"))
                .andExpect(jsonPath("[0].lastName").value("Smith"))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void shouldReturnPersonWithMultipleMatch() throws Exception {
        when(personDataService.findPersonsByLastName(any())).thenReturn(Arrays.asList(new Person("Mary", "Smith"), new Person("Zohaib", "Smith")));
        this.mockMvc.perform(get("/person/Smith"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id").exists())
                .andExpect(jsonPath("[0].firstName").value("Mary"))
                .andExpect(jsonPath("[0].lastName").value("Smith"))
                .andExpect(jsonPath("[1].id").exists())
                .andExpect(jsonPath("[1].firstName").value("Zohaib"))
                .andExpect(jsonPath("[1].lastName").value("Smith"));
    }

    @Test
    public void shouldAddAPersonAndReturnWithSuccess() throws Exception {
        PersonDto personDto = new PersonDto("Zohaib", "Baig");
        when(personDataService.addPerson(any())).thenReturn(new PersonDto(1L, "Zohaib", "Baig"));

        this.mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(personDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("firstName").value("Zohaib"))
                .andExpect(jsonPath("lastName").value("Baig"));
    }

    @Test
    public void shouldReturnWithPersonAlreadyExistException() throws Exception {
        PersonDto personDto = new PersonDto("Zohaib", "Baig");
        when(personDataService.addPerson(any())).thenThrow(new PersonAlreadyExistException("Person already exist"));

        this.mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(personDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}