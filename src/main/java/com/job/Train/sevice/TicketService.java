package com.job.Train.sevice;

import com.job.Train.dto.StationDto;
import com.job.Train.dto.TicketDto;


public interface TicketService {

    TicketDto ticketBooking(TicketDto ticketDto);

    String cancelTicket(int ticketId);

    void updateTicketStatus(TicketDto ticketDto);

    String findSeatAndDistanceBetweenTheStation(int trainNumber,String source, String destination);
}
