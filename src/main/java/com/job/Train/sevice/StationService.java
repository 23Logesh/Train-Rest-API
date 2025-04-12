package com.job.Train.sevice;

import com.job.Train.dto.StationDto;

import java.util.List;

public interface StationService {

    StationDto addStation(StationDto station);

    String deleteStation(int stationId);

    List<StationDto> fetchByTrain(int trainNumber);

    StationDto updateStation(int stationNumber, StationDto stationDto);

    StationDto findSeatAndDistanceBetweenTheStation(String source, String destination);
}
