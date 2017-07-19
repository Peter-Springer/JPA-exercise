package com.allstate.compozed.springplayground.lesson;

import com.allstate.compozed.springplayground.lesson.LessonRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import java.util.Random;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.verify;

/**
 * Created by localadmin on 7/19/17.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc

public class LessonControllerRealDBTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LessonRepository repository;

    @Transactional
    @Rollback
    @Test
    public  void listReturnsExistingLessons( ) throws Exception {

        final LessonModel lessonOne = new LessonModel();
        lessonOne.setTitle("SAIRA AND PETE TESTING REAL DB");

        repository.save(lessonOne);

        mockMvc.perform(get("/lessons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].id", is(lessonOne.getId().intValue())))
                .andExpect(jsonPath("$[0].title", is("SAIRA AND PETE TESTING REAL DB")));


    }

    @Transactional
    @Rollback
    @Test
    public void readReturnsSpecificRecordFromDatabase() throws Exception {

        final LessonModel lessonOne = new LessonModel();
        lessonOne.setTitle("SAIRA AND PETE TESTING REAL DB");

        repository.save(lessonOne);

        Long id = lessonOne.getId();

        mockMvc.perform(get("/lessons/{id}", id))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", is(lessonOne.getId().intValue())))
                .andExpect(jsonPath("$.title", is("SAIRA AND PETE TESTING REAL DB")));
    }


}
