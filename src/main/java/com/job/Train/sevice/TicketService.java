package com.job.Train.sevice;

import com.job.Train.dto.TicketDto;


public interface TicketService {

    TicketDto ticketBooking(TicketDto ticketDto);

    String cancelTicket(int ticketId);

    void updateTicketStatus(TicketDto ticketDto);
}
