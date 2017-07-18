package com.allstate.compozed.springplayground.lesson;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(LessonController.class)
public class LessonControllerTest {

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
}
