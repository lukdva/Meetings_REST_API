package com.lukdva.meetings.repositories;

import com.lukdva.meetings.models.Attendee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendeesRepository extends JpaRepository<Attendee, Long> {
}
