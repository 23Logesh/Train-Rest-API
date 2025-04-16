package com.job.Train.controller;

import com.job.Train.dto.StationDto;
import com.job.Train.dto.TicketDto;
import com.job.Train.sevice.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Ticket")
@Slf4j
public class TicketController {

    @Autowired
    TicketService ticketService;

    @PostMapping("/ticketBooking")
    public ResponseEntity<TicketDto> ticketBooking(@RequestBody TicketDto ticketDto) {
        return new ResponseEntity<TicketDto>(ticketService.ticketBooking(ticketDto), HttpStatus.OK);
    }

    @PatchMapping("/deleteTicket/{ticketId}")
    public String cancelTicket(@PathVariable int ticketId) {
        return ticketService.cancelTicket(ticketId);
    }


    @GetMapping("/findSeatAndDistance/{trainNumber}/{source}/{destination}")
    public ResponseEntity<String> findSeatAndDistanceBetweenTheStation(int trainNumber, String source, String destination) {
        return new ResponseEntity<String>(ticketService.findSeatAndDistanceBetweenTheStation( trainNumber,source, destination), HttpStatus.OK);
    }

}
