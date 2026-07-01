package com.smartpark.backend.model.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "parking_slots")
public class ParkingSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 10)
    private String numero; // Ejemplo: "A1", "B2"

    @Column(nullable = false, length = 20)
    private String estado; // "DISPONIBLE", "OCUPADO", "MANTENIMIENTO"

    @Column(name = "tipo_permitido", length = 20)
    private String tipoVehiculoPermitido; // "AUTO", "MOTO", "AMBOS"

}