package org.example.projecttraining.Repository;

import org.example.projecttraining.Model.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertRepository extends JpaRepository<Concert, Integer> {
}
