package com.job.Train.controller;

import com.job.Train.dto.TrainDto;
import com.job.Train.sevice.TrainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/Train")
public class TrainController {

    @Autowired
    private TrainService trainService;

    @PostMapping("/save")
    public ResponseEntity<TrainDto> save(@RequestBody TrainDto trainDto) {
        log.info("Logger Entering in Controller SaveMethod ");
        return new ResponseEntity<TrainDto>(trainService.save(trainDto), HttpStatus.OK);
    }

    @GetMapping("/fetch/{trainNumber}")
    public ResponseEntity<TrainDto> fetchhById(@PathVariable int trainNumber) {
        log.info("Logger Entering in Controller FetchByIdMethod ");
        return new ResponseEntity<TrainDto>(trainService.fetchById(trainNumber), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public String delete(@RequestParam int trainNumber) {
        log.info("Logger Entering in Controller Delete_Method ");
        return trainService.delete(trainNumber);
    }

    @PatchMapping("/Update/{trainNumber}/{departureTime}")
    public ResponseEntity<TrainDto> update(@PathVariable int trainNumber, @PathVariable LocalTime departureTime) {
        log.info("Logger Entering in Controller UpdateMethod ");
        return new ResponseEntity<TrainDto>(trainService.update(trainNumber, departureTime), HttpStatus.OK);
    }

    @PutMapping("/updateAll/{trainNumber}")
    public ResponseEntity<TrainDto> updateAll(@PathVariable int trainNumber, @RequestBody TrainDto train) {
        log.info("Logger Entering in Controller UpdateAllMethod ");
        return new ResponseEntity<TrainDto>(trainService.updateAll(trainNumber, train), HttpStatus.OK);
    }

    @GetMapping("/fetchAll")
    public ResponseEntity<List<TrainDto>> fetchAll() {
        log.info("Logger Entering in Controller FetchAllMethod ");
        return new ResponseEntity<List<TrainDto>>(trainService.fetchAll(), HttpStatus.OK);
    }

    @GetMapping("/nextStation")
    public ResponseEntity<String> movingTrainForNextStation(@RequestParam int trainNumber) {
        return new ResponseEntity<>(trainService.nextStation(trainNumber), HttpStatus.OK);
    }

}
