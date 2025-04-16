package com.job.Train.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class TrainDto {

    private int trainNumber;

    private String trainName;

    private String source;

    private String destination;

    private LocalTime departureTime;

    private LocalTime arrivalTime;

    private int totalSleeperSeat;

    private int totalAcSeat;

    private int totalGeneralSeat;

    private double farePerKm;

    private int distance;

    private String currentStation = "Yet to Start";

}
