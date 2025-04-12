package com.job.Train.service_Imp;

import com.job.Train.dto.StationDto;
import com.job.Train.dto.TicketDto;
import com.job.Train.dto.TrainDto;
import com.job.Train.entity.Ticket;
import com.job.Train.entity.Train;
import com.job.Train.repository.TicketRepository;
import com.job.Train.repository.TrainRepository;
import com.job.Train.sevice.StationService;
import com.job.Train.sevice.TicketService;
import com.job.Train.sevice.TrainService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TicketServiceImp implements TicketService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    TrainService trainService;

    @Autowired
    TrainRepository trainRepository;

    @Autowired
    StationService stationService;

    public TicketDto ticketBooking(TicketDto ticketDto) {
        Optional<Train> optionalTrain = trainRepository.findById(ticketDto.getTrainDto().getTrainNumber());
        if (optionalTrain.isPresent()) {
            Train train = optionalTrain.get();
            List<StationDto> stationList = stationService.fetchByTrain(train.getTrainNumber());
            StationDto sourceStation = null, destinationStation = null;
            boolean inRoute = false;
            List<StationDto> routeStations = new ArrayList<>();

            for (StationDto station : stationList) {
                if (station.getStationName().equalsIgnoreCase(ticketDto.getSource()) && !ticketDto.getSource().equals(station.getTrainDto().getCurrentStation())) {
                    inRoute = true;
                    sourceStation = station;
                }
                if (inRoute) {
                    routeStations.add(station);
                }
                if (station.getStationName().equalsIgnoreCase(ticketDto.getDestination())) {
                    destinationStation = station;
                    break;
                }
            }
            if (sourceStation == null || destinationStation == null) {
                throw new RuntimeException("Invalid Source or Destination");
            }
            TicketDto ticketDto1 = ticketDto;
            boolean seatsAvailable = routeStations.stream().allMatch(station -> {
                return switch (ticketDto1.getSeatType().toLowerCase()) {
                    case "sleeperseat" -> station.getSleeperSeat() > 0;
                    case "acseat" -> station.getAcSeat() > 0;
                    case "generalseat" -> station.getGeneralSeat() > 0;
                    default -> false;
                };
            });
            StationDto removedStationDto = routeStations.removeLast();
            if (seatsAvailable) {
                for (StationDto stationDto : routeStations) {
                    switch (ticketDto.getSeatType().toLowerCase()) {
                        case "sleeperseat":
                            stationDto.setSleeperSeat(stationDto.getSleeperSeat() - 1);
                            stationService.updateStation(stationDto.getStationNumber(), stationDto);

                            break;
                        case "acseat":
                            stationDto.setAcSeat(stationDto.getAcSeat() - 1);
                            stationService.updateStation(stationDto.getStationNumber(), stationDto);

                            break;
                        case "generalseat":
                            stationDto.setGeneralSeat(stationDto.getGeneralSeat() - 1);
                            stationService.updateStation(stationDto.getStationNumber(), stationDto);

                            break;
                    }
                    stationService.updateStation(stationDto.getStationNumber(), stationDto);
                }

                routeStations.add(removedStationDto);

                ticketDto = fareCalculation(routeStations, ticketDto);
                ticketDto.setStatus("Confirmed");
                ticketDto.setTrainDto(modelMapper.map(train, TrainDto.class));
                Ticket ticket = convertDtoToEntity(ticketDto);
                return convertEntityToDto(ticketRepository.save(ticket));
            } else {
                return isWaitingList(ticketDto);
            }
        }
        return null;
    }

    public TicketDto isWaitingList(TicketDto ticketDto) {
        ticketDto.setStatus("WaitingList");
        Ticket ticket = convertDtoToEntity(ticketDto);
        return convertEntityToDto(ticketRepository.save(ticket));
    }

    public String cancelTicket(int ticketId) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);

        if (optionalTicket.isPresent()) {
            TicketDto ticketDto = convertEntityToDto(optionalTicket.get());
            List<StationDto> stationList = stationService.fetchByTrain(ticketDto.getTrainDto().getTrainNumber());
            StationDto sourceStation = null, destinationStation = null;
            boolean inRoute = false;
            List<StationDto> routeStations = new ArrayList<>();

            for (StationDto station : stationList) {
                if (station.getStationName().equalsIgnoreCase(ticketDto.getSource())) {
                    inRoute = true;
                    sourceStation = station;
                }
                if (inRoute) {
                    routeStations.add(station);
                }
                if (station.getStationName().equalsIgnoreCase(ticketDto.getDestination())) {
                    destinationStation = station;
                    break;
                }
            }
            if (ticketDto.getSeatType().equalsIgnoreCase("sleeperSeat")) {
                if (ticketDto.getStatus().equalsIgnoreCase("Confirmed")) {
                    Ticket ticket = convertDtoToEntity(ticketDto);
                    updateTicketStatus(convertEntityToDto(ticket));
                    ticketRepository.save(ticket);
                    routeStations.removeLast();
                    routeStations.stream().peek(stationDto -> stationDto.setSleeperSeat(stationDto.getSleeperSeat() + 1)).forEach((stationDto) -> stationService.updateStation(stationDto.getStationNumber(), stationDto));

                } else if (ticketDto.getStatus().equalsIgnoreCase("WaitingList")) {
                    Ticket ticket = convertDtoToEntity(ticketDto);
                    updateTicketStatus(convertEntityToDto(ticket));
                    return "Ticket is Already in WaitingList It Canceled Successfully";
                }
            } else if (ticketDto.getSeatType().equalsIgnoreCase("generalSeat")) {
                if (ticketDto.getStatus().equalsIgnoreCase("Confirmed")) {
                    Ticket ticket = convertDtoToEntity(ticketDto);
                    updateTicketStatus(convertEntityToDto(ticket));
                    routeStations.removeLast();
                    routeStations.stream().peek(stationDto -> stationDto.setGeneralSeat(stationDto.getGeneralSeat() + 1)).forEach((stationDto) -> stationService.updateStation(stationDto.getStationNumber(), stationDto));

                } else if (ticketDto.getStatus().equalsIgnoreCase("WaitingList")) {
                    Ticket ticket = convertDtoToEntity(ticketDto);
                    updateTicketStatus(convertEntityToDto(ticket));
                    return "Ticket is Already in WaitingList It Canceled Successfully";
                }
            } else if (ticketDto.getSeatType().equalsIgnoreCase("acSeat")) {
                if (ticketDto.getStatus().equalsIgnoreCase("Confirmed")) {
                    Ticket ticket = convertDtoToEntity(ticketDto);
                    updateTicketStatus(convertEntityToDto(ticket));
                    routeStations.removeLast();
                    routeStations.stream().peek(stationDto -> stationDto.setAcSeat(stationDto.getAcSeat() + 1)).forEach((stationDto) -> stationService.updateStation(stationDto.getStationNumber(), stationDto));

                } else if (ticketDto.getStatus().equalsIgnoreCase("WaitingList")) {
                    Ticket ticket = convertDtoToEntity(ticketDto);
                    updateTicketStatus(convertEntityToDto(ticket));
                    return "Ticket is Already in WaitingList It Canceled Successfully";
                }
            }
            updateTicketStatus(ticketDto);
        }
        waitingListTicketUpdate();
        return "Confirmed Ticket Is Canceled";
    }

    public void waitingListTicketUpdate() {
        List<TicketDto> tickets = ticketRepository.findByStatus("WaitingList").stream().map(this::convertEntityToDto).toList();
        if (!tickets.isEmpty())
            tickets.forEach(this::ticketBooking);
    }

    public void updateTicketStatus(TicketDto newTicketDto) {
        TicketDto oldTicketDto = findById(newTicketDto.getTicketId());
        if (oldTicketDto != null) {
            oldTicketDto.setStatus("TicketCanceled");
            ticketRepository.save(convertDtoToEntity(oldTicketDto));
        }
    }

    public TicketDto findById(int ticketId) {
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);
        return ticket.map(this::convertEntityToDto).orElse(null);
    }

    public TicketDto fareCalculation(List<StationDto> stationDtoList, TicketDto ticketDto) {
        double farePerKm = trainService.fetchById(ticketDto.getTrainDto().getTrainNumber()).getFarePerKm();
        double updatetotalSleeperSeatFare, updatedGeneralSeat, updateAcSeat;
        for (StationDto stationDto : stationDtoList) {
            switch (ticketDto.getSeatType().toUpperCase()) {
                case "SLEEPERSEAT": {
                    if (ticketDto.getAge() > 12 && ticketDto.getAge() < 60) {
                        updatetotalSleeperSeatFare = (farePerKm * 1.5 * stationDto.getKiloMeter()) + ticketDto.getTotalFare();
                        ticketDto.setTotalFare(updatetotalSleeperSeatFare);
                    } else if (ticketDto.getAge() <= 12) {
                        updatetotalSleeperSeatFare = (farePerKm * 0.3 * stationDto.getKiloMeter()) + ticketDto.getTotalFare();
                        ticketDto.setTotalFare(updatetotalSleeperSeatFare);
                    } else {
                        updatetotalSleeperSeatFare = (farePerKm * 1.2 * stationDto.getKiloMeter()) + ticketDto.getTotalFare();
                        ticketDto.setTotalFare(updatetotalSleeperSeatFare);
                    }
                    break;
                }
                case "ACSEAT": {
                    if (ticketDto.getAge() > 12 && ticketDto.getAge() < 60) {
                        updateAcSeat = (farePerKm * 2 * stationDto.getKiloMeter()) + ticketDto.getTotalFare();
                        ticketDto.setTotalFare(updateAcSeat);
                    } else if (ticketDto.getAge() <= 12) {
                        updateAcSeat = (farePerKm * 0.5 * stationDto.getKiloMeter()) + ticketDto.getTotalFare();
                        ticketDto.setTotalFare(updateAcSeat);
                    } else {
                        updateAcSeat = (farePerKm * 1.5 * stationDto.getKiloMeter()) + ticketDto.getTotalFare();
                        ticketDto.setTotalFare(updateAcSeat);
                    }
                    break;
                }
                default: {
                    if (ticketDto.getAge() > 12 && ticketDto.getAge() < 60) {
                        updatedGeneralSeat = (farePerKm * 1.2 * stationDto.getKiloMeter()) + ticketDto.getTotalFare();
                        ticketDto.setTotalFare(updatedGeneralSeat);
                    } else if (ticketDto.getAge() <= 12) {
                        updatedGeneralSeat = (farePerKm * 0.3 * stationDto.getKiloMeter()) + ticketDto.getTotalFare();
                        ticketDto.setTotalFare(updatedGeneralSeat);
                    } else {
                        updatedGeneralSeat = (farePerKm * 0.5 * stationDto.getKiloMeter()) + ticketDto.getTotalFare();
                        ticketDto.setTotalFare(updatedGeneralSeat);
                    }
                    break;
                }
            }
        }
        return ticketDto;
    }


    public TicketDto convertEntityToDto(Ticket ticket) {
        TicketDto ticketDto = modelMapper.map(ticket, TicketDto.class);
        ticketDto.setTrainDto(modelMapper.map(ticket.getTrain(), TrainDto.class));
        return ticketDto;
    }

    public Ticket convertDtoToEntity(TicketDto ticketDto) {
        Ticket ticket1 = modelMapper.map(ticketDto, Ticket.class);
        ticket1.setTrain(modelMapper.map(ticket1.getTrain(), Train.class));
        return ticket1;
    }
}







