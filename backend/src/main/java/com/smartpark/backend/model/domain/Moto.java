package com.smartpark.backend.model.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("MOTO")
@NoArgsConstructor
public class Moto extends Vehiculo {
    // Aquí puedes agregar atributos específicos de las motos en el futuro (ej. cilindraje)
}