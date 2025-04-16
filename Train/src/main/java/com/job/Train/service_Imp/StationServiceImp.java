package com.job.Train.service_Imp;

import com.job.Train.dto.StationDto;
import com.job.Train.dto.TrainDto;
import com.job.Train.entity.Station;
import com.job.Train.entity.Train;
import com.job.Train.repository.StationRepository;
import com.job.Train.sevice.StationService;
import com.job.Train.sevice.TrainService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationServiceImp implements StationService {

    @Autowired
    StationRepository stationRepository;

    @Autowired
    TrainService trainService;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public StationDto addStation(StationDto station) {

        if (station != null) {

            station = convertEntityToDto(stationRepository.save(convertDtoToEntity(station)));
            updateTrainDistanceAndSeates(station);
            return station;

        }
        return null;

    }

    private void updateTrainDistanceAndSeates(StationDto stationDto) {


        TrainDto trainDto = trainService.fetchById(stationDto.getTrainDto().getTrainNumber());

        trainDto.setDistance(0);
        trainDto.setTotalAcSeat(0);
        trainDto.setTotalGeneralSeat(0);
        trainDto.setTotalSleeperSeat(0);
        stationRepository.findByTrainTrainNumber(stationDto.getTrainDto().getTrainNumber()).stream().map(this::convertEntityToDto).forEach(stationDto2 -> {
            trainDto.setDistance(trainDto.getDistance() + stationDto2.getKiloMeter());
            trainDto.setTotalAcSeat(trainDto.getTotalAcSeat() + stationDto2.getAcSeat());
            trainDto.setTotalGeneralSeat(trainDto.getTotalGeneralSeat() + stationDto2.getGeneralSeat());
            trainDto.setTotalSleeperSeat(trainDto.getTotalSleeperSeat() + stationDto2.getSleeperSeat());
        });

        trainService.updateAll(trainDto.getTrainNumber(), trainDto);
    }

    @Override
    public String deleteStation(int stationNumber) {
        StationDto stationDto = convertEntityToDto(stationRepository.findById(stationNumber).orElse(null));
        if (stationDto != null) {
            stationRepository.deleteById(stationNumber);
            updateTrainDistanceAndSeates(stationDto);
            return "Station Deleted";
        }
        return "station not Deleted";
    }

    @Override
    public List<StationDto> fetchByTrain(int trainNumber) {
        return stationRepository.findByTrainTrainNumber(trainNumber).stream().map(this::convertEntityToDto).toList();
    }

    public StationDto updateStation(int stationNumber, StationDto UpdateStationDto) {
        StationDto stationDto1 = convertEntityToDto(stationRepository.findById(stationNumber).orElse(null));
        stationDto1.setStationName(UpdateStationDto.getStationName());
        stationDto1.setAcSeat(UpdateStationDto.getAcSeat());
        stationDto1.setGeneralSeat(UpdateStationDto.getGeneralSeat());
        stationDto1.setSleeperSeat(UpdateStationDto.getSleeperSeat());
        stationDto1.setKiloMeter(UpdateStationDto.getKiloMeter());
        return addStation(stationDto1);
    }

    public StationDto convertEntityToDto(Station station) {
        if (station != null) {
            TrainDto trainDto = trainService.convertEntityToDto(station.getTrain());
            StationDto stationDto = modelMapper.map(station, StationDto.class);
            stationDto.setTrainDto(trainDto);
            return stationDto;
        }
        return null;
    }

    public Station convertDtoToEntity(StationDto stationDto) {
        if (stationDto != null) {
            Train train = trainService.convertDtoToEntity(stationDto.getTrainDto());
            Station station = modelMapper.map(stationDto, Station.class);
            station.setTrain(train);
            return station;
        }
        return null;
    }


}
