package com.job.Train.sevice;

import com.job.Train.dto.TrainDto;
import com.job.Train.entity.Train;

import java.time.LocalTime;
import java.util.List;

public interface TrainService {

    TrainDto save(TrainDto trainDto);

    TrainDto fetchById(int trainNumber);

    String delete(int trainNumber);

    TrainDto update(int trainNumber, LocalTime departureTime);

    TrainDto updateAll(int trainNumber, TrainDto train);

    List<TrainDto> fetchAll();

    String nextStation(int trainNumber);

    TrainDto convertEntityToDto(Train train);

    Train convertDtoToEntity(TrainDto trainDto);


}
