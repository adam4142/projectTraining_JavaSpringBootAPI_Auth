package org.example.projecttraining.Service;

import org.example.projecttraining.Model.Concert;
import org.example.projecttraining.Repository.ConcertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class concertService {

    @Autowired
    private ConcertRepository concertRepository;

    public Optional<Concert> updateConcert(Integer id, Concert updatedData) {
        return concertRepository.findById(id).map(existingConcert -> {
            existingConcert.setConcertName(updatedData.getConcertName());
            existingConcert.setDate(updatedData.getDate());
            existingConcert.setAvailTicket(updatedData.getAvailTicket());
            existingConcert.setPrice(updatedData.getPrice());
            existingConcert.setVenue(updatedData.getVenue());

            return concertRepository.save(existingConcert);
        });
    }
}

