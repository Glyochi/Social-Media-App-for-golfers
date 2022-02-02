package com.example.roundtrip.Controller;


import com.example.roundtrip.Model.Score;
import com.example.roundtrip.Repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ScoreController<ScoreRepository> {

    @Autowired
    ScoreRepository scoreRepository;

    @GetMapping("score")
    public String getScore(){
        return "This is your score lmao";
    }

    @GetMapping("score/all")
    List<Score> GetAllScore(){
        return scoreRepository.findAll();
    }

    @PostMapping("score/post/{name}/{score}")
    Score PostScoreByPath(@PathVariable String name, @PathVariable int score){
        Score newScore = new Score();
        newScore.setName(name);
        newScore.setScore(score);
        scoreRepository.save(newScore);
        return newScore;
    }

    @PostMapping("score/post")
    Score PostScoreByPath(@RequestBody Score newScore){
        scoreRepository.save(newScore);
        return newScore;
    }


}
