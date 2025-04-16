package com.job.Train.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;

@Entity
@Data
public class Train {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int trainNumber;

    @Column(length = 50, nullable = false, unique = true)
    private String trainName;

    @Column(length = 50, nullable = false)
    private String source;

    @Column(length = 50, nullable = false)
    private String destination;

    @Column(nullable = false)
    private LocalTime departureTime;

    @Column(nullable = false)
    private LocalTime arrivalTime;

    @Column(length = 3, nullable = false)
    private int totalSleeperSeat;

    @Column(length = 3, nullable = false)
    private int totalAcSeat;

    @Column(length = 3, nullable = false)
    private int totalGeneralSeat;

    @Column(length = 3, nullable = false)
    private double farePerKm;

    @Column(length = 5, nullable = false)
    private int distance;

    @Column(nullable = false)
    private String currentStation = "Yet to Start";


}