package com.job.Train.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int stationNumber;

    @Column(length = 50, nullable = false)
    private String stationName;

    @Column(length = 3, nullable = false)
    private int generalSeat;

    @Column(length = 3, nullable = false)
    private int sleeperSeat;

    @Column(length = 3, nullable = false)
    private int acSeat;

    @Column(length = 5, nullable = false)
    private int kiloMeter;

    @ManyToOne
    private Train train;
}
