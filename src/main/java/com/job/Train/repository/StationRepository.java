package com.job.Train.repository;

import com.job.Train.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StationRepository extends JpaRepository<Station, Integer> {
    List<Station> findByTrainTrainNumber(int trainNumber);
}
