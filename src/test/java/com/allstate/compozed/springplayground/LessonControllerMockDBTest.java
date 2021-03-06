package com.allstate.compozed.springplayground.lesson;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(LessonController.class)
public class LessonControllerMockDBTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LessonRepository repository;

    @Test
    public void createDelegatesToRepository() throws Exception {
        //Setup
        when(repository.save(any(LessonModel.class))).then(returnsFirstArg());

        final MockHttpServletRequestBuilder post = post("/lessons")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Mock me another one!\"}");

        //Exercise
        final ResultActions resultActions = mockMvc.perform(post);

        //Assert
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Mock me another one!")));

        verify(repository).save(any(LessonModel.class));
    }

    @Test
    public void listOneItemFromRepository() throws Exception {
        //Setup
        Long id = new Random().nextLong();
        LessonModel lesson = new LessonModel();
        lesson.setId(id);
        lesson.setTitle("How to list mocked items");

        when(repository.findAll()).thenReturn(Collections.singletonList(lesson));

        //Exercise
        final MockHttpServletRequestBuilder request = get("/lessons")
                .contentType(MediaType.APPLICATION_JSON);

        //Assert
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", equalTo(id)))
                .andExpect(jsonPath("$[0].title", equalTo("How to list mocked items")));

        verify(repository).findAll();
    }

    // redundant test below (example of how to create a list)

    @Test
    public void listTwoItemsFromRepository() throws Exception {
        //Setup
        Long id = new Random().nextLong();
        LessonModel lesson = new LessonModel();
        lesson.setId(id);
        lesson.setTitle("How to list mocked items");

        Long idTwo = new Random().nextLong();
        LessonModel lessonTwo = new LessonModel();
        lessonTwo.setId(idTwo);
        lessonTwo.setTitle("Proof another one works");

        //List.push lesson, lessonTwo
        ArrayList<LessonModel> lessons = new ArrayList<>();

        lessons.add(lesson);
        lessons.add(lessonTwo);

        when(repository.findAll()).thenReturn(lessons);

        //Exercise
        final MockHttpServletRequestBuilder request = get("/lessons")
                .contentType(MediaType.APPLICATION_JSON);

        //Assert
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", equalTo(id)))
                .andExpect(jsonPath("$[0].title", equalTo("How to list mocked items")))
                .andExpect(jsonPath("$[1].id", equalTo(idTwo)))
                .andExpect(jsonPath("$[1].title", equalTo("Proof another one works")));


        verify(repository).findAll();
    }
    @Test
    public void readReturnsSingleItem( ) throws Exception {

        Long id = new Random().nextLong();
        ResponseEntity<LessonModel> lesson = new ResponseEntity<>(new LessonModel(), HttpStatus.OK);
        lesson.getBody().setId(id);
        lesson.getBody().setTitle("List single item");

        when(repository.findOne(id)).thenReturn(lesson.getBody());

        //Exercise
        final MockHttpServletRequestBuilder request = get("/lessons/{id}", id);

        //Assert
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(id)))
                .andExpect(jsonPath("$.title", equalTo("List single item")));

        verify(repository).findOne(id);
    }

    @Test
    public void updateUpdatesSingleItem( ) throws Exception {

        Long id = new Random().nextLong();
        LessonModel lesson = new LessonModel();
        lesson.setId(id);
        lesson.setTitle("List single item");

        when(repository.save(any(LessonModel.class))).then(returnsFirstArg());
        when(repository.findOne(id)).thenReturn(lesson);

        final MockHttpServletRequestBuilder request = put("/lessons/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"PUT!!\"}");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", equalTo(id)))
                .andExpect(jsonPath("$.title", equalTo("PUT!!")));

        verify(repository).save(any(LessonModel.class));

    }

    @Test
    public void deleteRemovesSingleRecord() throws Exception {

        Long id = new Random().nextLong();
        LessonModel lesson = new LessonModel();
        lesson.setId(id);
        lesson.setTitle("Delete this record");

        when(repository.save(any(LessonModel.class))).then(returnsFirstArg());
        when(repository.findOne(id)).thenReturn(lesson);

        final MockHttpServletRequestBuilder request = delete("/lessons/{id}", id);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print());

        verify(repository).delete(id);

    }

    @Test public void readSends4XXStatusWhenIdNotFound() throws Exception {

        final MockHttpServletRequestBuilder request = get("/lessons/3");

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

}
