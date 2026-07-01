package com.smartpark.backend.repository;

import com.smartpark.backend.model.domain.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    // Spring crea automáticamente la consulta SQL con solo nombrar el método así:
    Optional<Vehiculo> findByPlaca(String placa);
}