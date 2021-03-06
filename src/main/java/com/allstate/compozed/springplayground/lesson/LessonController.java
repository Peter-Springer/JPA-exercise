package com.allstate.compozed.springplayground.lesson;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lessons")
public class LessonController {

    private final LessonRepository repository;

    LessonController(final LessonRepository repository) {
        this.repository = repository;
    }

    @GetMapping("")
    public Iterable<LessonModel> list() {
        return repository.findAll();
    }

    @PostMapping("")
    public LessonModel create(@RequestBody final LessonModel lesson) {
        return this.repository.save(lesson);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonModel> read(@PathVariable Long id) {

        if (this.repository.findOne(id) == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(this.repository.findOne(id), HttpStatus.MULTI_STATUS.OK);
    }

    @PutMapping("/{id}")
    public LessonModel update(@PathVariable Long id, @RequestBody LessonModel lesson) {
        LessonModel uniqueLesson = repository.findOne(id);
        uniqueLesson.setTitle(lesson.getTitle());
        System.out.println(lesson);
        return repository.save(uniqueLesson);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.delete(id);
    }

}