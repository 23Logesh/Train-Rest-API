package com.job.Train.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ticketId;

    @Column(length = 50, nullable = false)
    private String passengerName;

    @Column(length = 3, nullable = false)
    private int age;

    @Column(length = 15, nullable = false)
    private String seatType;

    @Column(length = 25, nullable = false)
    private String status;

    @Column(nullable = false, length = 25)
    private String source;

    @Column(nullable = false, length = 25)
    private String destination;

    @Column(length = 11, nullable = false, unique = true)
    private long phoneNumber;

    @Column(nullable = false)
    private double totalFare;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trains_Number")
    private Train train;

}
