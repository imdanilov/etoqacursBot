package ru.etoqacursBot.impl;

import ru.etoqacursBot.getLesson;

import java.util.HashMap;
import java.util.Map;

public class getLessonImpl implements getLesson {
    private final Map<String, String> mapLesson = new HashMap<>();

    public getLessonImpl(){
        mapLesson.put("Lesson 1", "https://www.youtube.com/watch?v=9NQ0Q8AzNho");
        mapLesson.put("Lesson 2", "https://www.youtube.com/watch?v=TtZuCIEqQdw&ab_channel=MaximPolishchuk");

    }

    public Map<String, String> getLesson(){
        return mapLesson;
    }
}
