package com.allstate.compozed.springplayground.lesson;

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
        return this.repository.findAll();
    }

    @PostMapping("")
    public LessonModel create(@RequestBody final LessonModel lesson) {
        return this.repository.save(lesson);
    }

}