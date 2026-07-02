package com.smartpark.backend;

import com.smartpark.backend.model.domain.ParkingSlot;
import com.smartpark.backend.repository.ParkingSlotRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SmartparkBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartparkBackendApplication.class, args);
	}

	// Este bloque de código se ejecuta automáticamente justo después de que el servidor arranca
	@Bean
	public CommandLineRunner initData(ParkingSlotRepository slotRepository) {
		return args -> {
			// Solo inserta los datos si la tabla está completamente vacía
			if (slotRepository.count() == 0) {
				System.out.println("⚙️ Base de datos vacía detectada. Insertando espacios por defecto...");

				// Zona de Autos
				slotRepository.save(new ParkingSlot(null, "A1", "DISPONIBLE", "AUTO"));
				slotRepository.save(new ParkingSlot(null, "A2", "DISPONIBLE", "AUTO"));
				slotRepository.save(new ParkingSlot(null, "A3", "DISPONIBLE", "AUTO"));
				slotRepository.save(new ParkingSlot(null, "A4", "DISPONIBLE", "AUTO"));

				// Zona de Motos
				slotRepository.save(new ParkingSlot(null, "M1", "DISPONIBLE", "MOTO"));
				slotRepository.save(new ParkingSlot(null, "M2", "DISPONIBLE", "MOTO"));
				slotRepository.save(new ParkingSlot(null, "M3", "DISPONIBLE", "MOTO"));
				slotRepository.save(new ParkingSlot(null, "M4", "DISPONIBLE", "MOTO"));

				System.out.println("8 espacios de estacionamiento creados exitosamente.");
			}
		};
	}
}