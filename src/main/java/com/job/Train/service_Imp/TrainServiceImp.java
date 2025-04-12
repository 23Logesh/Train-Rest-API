package com.job.Train.service_Imp;

import com.job.Train.dto.StationDto;
import com.job.Train.dto.TrainDto;
import com.job.Train.entity.Train;
import com.job.Train.repository.StationRepository;
import com.job.Train.repository.TrainRepository;
import com.job.Train.sevice.TrainService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class TrainServiceImp implements TrainService {

    @Autowired
    private TrainRepository trainRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private StationRepository stationRepository;

    public TrainDto save(TrainDto trainDto) {
        log.info("Logger Entering in ServiceImpl SaveMethod ");
        if (trainDto.getCurrentStation() == null || trainDto.getCurrentStation().isBlank() || trainDto.getCurrentStation().equalsIgnoreCase("String")) {
            trainDto.setCurrentStation("Yet to Start");
        }
        Train train = trainRepository.save(convertDtoToEntity(trainDto));
        TrainDto train_dto = convertEntityToDto(train);
        log.info("Logger is Saved Save Method");
        return train_dto;
    }

    public TrainDto fetchById(int trainNumber) {
        Optional<Train> optional = trainRepository.findById(trainNumber);
        log.info("Logger Entering in ServiceImpl FetchByIdMethod ");
        if (optional.isPresent()) {
            TrainDto trainDto = convertEntityToDto(optional.get());
            log.info("Logger is Fetched Data From FetchById Method");
            return trainDto;
        } else {
            return null;
        }
    }

    public String delete(int trainNumber) {
        Train train = convertDtoToEntity(fetchById(trainNumber));
        log.info("Logger Entering in ServiceImpl DeleteMethod ");
        if (train != null) {
            trainRepository.delete(train);
            log.info("Logger is Deleted The Value");
            return "Data Deleted";
        } else {
            return "Train_No Is Present";
        }
    }

    public TrainDto update(int trainNumber, LocalTime departureTime) {
        Train train = convertDtoToEntity(fetchById(trainNumber));
        log.info("Logger Entering in ServiceImpl UpdateMethod ");
        if (train != null) {
            train.setDepartureTime(departureTime);
            Train Update = trainRepository.save(train);
            log.info("Logger is Updated the Value");
            return convertEntityToDto(Update);
        } else {
            return null;

        }
    }


    public TrainDto updateAll(int trainNumber, TrainDto newTrainDto) {
        TrainDto oldTrainDto = fetchById(trainNumber);
        log.info("Logger Entering in ServiceImpl UpdateAllMethod ");
        if (oldTrainDto != null) {
            oldTrainDto.setTrainName(newTrainDto.getTrainName());
            oldTrainDto.setSource(newTrainDto.getSource());
            oldTrainDto.setDestination(newTrainDto.getDestination());
            oldTrainDto.setDepartureTime(newTrainDto.getDepartureTime());
            oldTrainDto.setArrivalTime(newTrainDto.getArrivalTime());
            oldTrainDto.setDistance(newTrainDto.getDistance());
            oldTrainDto.setTotalAcSeat(newTrainDto.getTotalAcSeat());
            oldTrainDto.setTotalSleeperSeat(newTrainDto.getTotalSleeperSeat());
            oldTrainDto.setTotalGeneralSeat(newTrainDto.getTotalGeneralSeat());
            oldTrainDto.setCurrentStation(newTrainDto.getCurrentStation());
            oldTrainDto.setFarePerKm(newTrainDto.getFarePerKm());
            oldTrainDto = convertEntityToDto(trainRepository.save(convertDtoToEntity(oldTrainDto)));
            log.info("Logger is Updated UpdateAll Value");
            return oldTrainDto;
        } else {
            return null;
        }
    }

    public List<TrainDto> fetchAll() {
        log.info("Logger Entering in ServiceImpl FetchAllMethod ");
        List<TrainDto> data = trainRepository.findAll().stream().map(this::convertEntityToDto).toList();
        log.info("[FetchAll Method]Data's From Data_Base is {}", data.stream().filter(Objects::nonNull).count());
        return data;
    }

    @Override
    public String nextStation(int trainNumber) {

        TrainDto trainDto = fetchById(trainNumber);
        if (trainDto != null) {
            if (trainDto.getCurrentStation().equalsIgnoreCase("Train Departed")) {
                trainDto.setCurrentStation("Yet to Start");
                updateAll(trainNumber, trainDto);
                return "Train station updated to Yet to Start";
            }

            String nextStationName = findNextStation(trainNumber, trainDto.getCurrentStation());
            trainDto.setCurrentStation(nextStationName);
            updateAll(trainNumber, trainDto);
            return "Train station updated to " + nextStationName;
        }
        return "Train not found";
    }

    private String findNextStation(int trainNumber, String currentStation) {

        List<StationDto> stationDtoList = stationRepository.findByTrainTrainNumber(trainNumber).stream()
                .map(data -> {
                    StationDto stationDto = modelMapper.map(data, StationDto.class);
                    stationDto.setTrainDto(convertEntityToDto(data.getTrain()));
                    return stationDto;
                }).toList();
        ListIterator<StationDto> stationIterator = stationDtoList.listIterator();
        if (currentStation.equals("Yet to Start")) {
            return stationDtoList.getFirst().getStationName();
        }
        while (stationIterator.hasNext()) {
            if (stationIterator.next().getStationName().equals(currentStation) && stationIterator.hasNext()) {
                return stationIterator.next().getStationName();
            }

        }
        return "Train Departed";
    }


    public TrainDto convertEntityToDto(Train train) {
        return modelMapper.map(train, TrainDto.class);
    }

    public Train convertDtoToEntity(TrainDto trainDto) {

        return modelMapper.map(trainDto, Train.class);
    }
}
