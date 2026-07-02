package com.smartpark.backend.repository;

import com.smartpark.backend.model.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}