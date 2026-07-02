package com.smartpark.backend.controller;

import com.smartpark.backend.model.domain.ParkingSlot;
import com.smartpark.backend.repository.ParkingSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/slots")
@RequiredArgsConstructor
public class SlotController {

    private final ParkingSlotRepository parkingSlotRepository;

    @GetMapping
    public List<ParkingSlot> obtenerTodosLosSlots() {
        return parkingSlotRepository.findAll();
    }
}