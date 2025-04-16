package com.job.Train.dto;

import lombok.Data;

@Data
public class TicketDto {

    private int ticketId;

    private String passengerName;

    private int age;

    private String seatType;

    private String status;

    private String source;

    private String destination;

    private long phoneNumber;

    private double totalFare;

    private TrainDto trainDto;
}
