package com.job.Train.controller;

import com.job.Train.dto.StationDto;
import com.job.Train.sevice.StationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Station")
@Slf4j
public class StationController {

    @Autowired
    StationService stationService;

    @PostMapping("/addStation")
    public ResponseEntity<StationDto> addStation(@RequestBody StationDto station) {
        return new ResponseEntity<>(stationService.addStation(station), HttpStatus.OK);
    }

    @DeleteMapping("/deleteStation/{stationId}")
    public String deleteStation(@PathVariable int stationId) {
        return stationService.deleteStation(stationId);
    }

    @GetMapping("/findSeatAndDistance/{source}/{destination}")
    public StationDto findSeatAndDistanceBetweenTheStation(String source, String destination) {
        return new ResponseEntity<StationDto>(stationService.findSeatAndDistanceBetweenTheStation(source, destination), HttpStatus.OK);
    }
}
