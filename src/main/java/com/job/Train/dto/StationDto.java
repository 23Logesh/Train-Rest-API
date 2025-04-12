package com.job.Train.dto;

import lombok.Data;

@Data
public class StationDto {

    private int stationNumber;

    private String stationName;

    private int generalSeat;

    private int sleeperSeat;

    private int acSeat;

    private int kiloMeter;

    private TrainDto trainDto;

}
