package com.smartpark.estacionamiento.controller;

import com.smartpark.estacionamiento.api.SmartParkApiClient;
import com.smartpark.estacionamiento.model.domain.ParkingSlot;
import com.smartpark.estacionamiento.model.domain.Ticket;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.stream.Collectors;

public class MainDashboardController {

    @FXML private TextField placaTextField, placaSalidaTextField;
    @FXML private ComboBox<String> tipoVehiculoComboBox, slotComboBox;
    @FXML private CheckBox lavadoCheckBox;
    @FXML private Label statusLabel;
    @FXML private GridPane parkingGrid;

    // ¡NUEVO: Nuestro único puente de comunicación con el Backend!
    private SmartParkApiClient apiClient;

    // Guardamos los slots en memoria para acceder a sus IDs rápidamente
    private List<ParkingSlot> slotsActuales;

    @FXML
    public void initialize() {
        // 1. Inicializamos el cliente HTTP (¡Adiós a la base de datos local!)
        this.apiClient = new SmartParkApiClient();

        tipoVehiculoComboBox.getItems().addAll("AUTO", "MOTO"); // En mayúscula como en el backend

        // 2. Cargamos los datos iniciales
        cargarDatosDesdeBackend();

        tipoVehiculoComboBox.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, newVal) -> filtrarSlotsDisponibles(newVal)
        );
    }

    private void cargarDatosDesdeBackend() {
        try {
            // Hacemos una petición GET a http://localhost:8080/api/slots
            slotsActuales = apiClient.obtenerTodosLosSlots();
            actualizarMapaVisual();
            filtrarSlotsDisponibles(tipoVehiculoComboBox.getValue());
            statusLabel.setText("Conectado al servidor Spring Boot. Mapa actualizado.");
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Conexión", "No se pudo conectar al servidor: " + e.getMessage());
        }
    }

    private void actualizarMapaVisual() {
        parkingGrid.getChildren().clear();

        int col = 0;
        int row = 0;

        for (ParkingSlot slot : slotsActuales) {
            Button btn = new Button(slot.getNumero() + "\n" + slot.getTipoVehiculoPermitido());
            btn.setPrefSize(80, 60);
            btn.getStyleClass().add("slot-button");

            // Comparamos con el estado que devuelve Spring Boot
            if ("DISPONIBLE".equals(slot.getEstado())) {
                btn.getStyleClass().add("slot-free");
            } else {
                btn.getStyleClass().add("slot-occupied");
            }

            btn.setOnAction(e -> {
                if ("DISPONIBLE".equals(slot.getEstado())) {
                    tipoVehiculoComboBox.setValue(slot.getTipoVehiculoPermitido());
                    slotComboBox.setValue(slot.getNumero());
                }
            });

            parkingGrid.add(btn, col, row);
            col++;
            if (col > 3) { col = 0; row++; }
        }
    }

    private void filtrarSlotsDisponibles(String tipo) {
        if (tipo == null || slotsActuales == null) {
            slotComboBox.getItems().clear();
            slotComboBox.setDisable(true);
            return;
        }

        List<String> slotsFiltrados = slotsActuales.stream()
                .filter(s -> s.getTipoVehiculoPermitido().equalsIgnoreCase(tipo) && "DISPONIBLE".equals(s.getEstado()))
                .map(ParkingSlot::getNumero)
                .collect(Collectors.toList());

        slotComboBox.getItems().setAll(slotsFiltrados);
        slotComboBox.setDisable(false);
    }

    @FXML
    private void handleRegistrarEntrada() {
        try {
            String placa = placaTextField.getText();
            String tipo = tipoVehiculoComboBox.getValue();
            String slotNum = slotComboBox.getValue();

            if(placa.isEmpty() || tipo == null || slotNum == null) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "Complete todos los campos."); return;
            }

            // Buscamos el ID del slot seleccionado
            Long slotId = slotsActuales.stream()
                    .filter(s -> s.getNumero().equals(slotNum))
                    .findFirst()
                    .orElseThrow(() -> new Exception("Slot no válido"))
                    .getId();

            // Petición POST al Backend
            apiClient.registrarEntrada(placa, tipo, slotId);

            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Entrada registrada en el servidor.");

            placaTextField.clear();
            tipoVehiculoComboBox.getSelectionModel().clearSelection();

            // Refrescamos el mapa haciendo otra petición GET
            cargarDatosDesdeBackend();

        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de API", e.getMessage());
        }
    }

    @FXML
    private void handleRegistrarSalida() {
        try {
            String placa = placaSalidaTextField.getText();
            if(placa.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Alerta", "Ingrese una placa");
                return;
            }

            // *Nota para tu equipo*: Por ahora necesitamos el ID del ticket para la salida.
            // Como reto, deben crear un endpoint en el backend que busque el ticket por placa (GET /api/tickets/buscar?placa=XYZ).
            // Para probar que la conexión funciona, vamos a simular que el ID es 1 por ahora.
            Long ticketIdSimulado = 1L;

            // Petición POST al Backend
            Ticket pagado = apiClient.registrarSalida(ticketIdSimulado, lavadoCheckBox.isSelected());

            mostrarAlerta(Alert.AlertType.INFORMATION, "Salida Registrada", "Total a pagar: S/" + pagado.getCostoTotal());

            placaSalidaTextField.clear();
            lavadoCheckBox.setSelected(false);

            // Refrescamos el mapa
            cargarDatosDesdeBackend();

        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de API", e.getMessage());
        }
    }

    @FXML
    private void handleDeshacer() {
        mostrarAlerta(Alert.AlertType.INFORMATION, "Próximamente", "Esta función será migrada a la nube en el Sprint 2.");
    }

    @FXML
    private void handleVerReporte() {
        mostrarAlerta(Alert.AlertType.INFORMATION, "Próximamente", "Los reportes se generarán en PDF desde el servidor en el Sprint 2.");
    }

    @FXML
    private void handleVerHistorial() {
        mostrarAlerta(Alert.AlertType.INFORMATION, "Próximamente", "El historial se consumirá desde la base de datos en la nube en el Sprint 2.");
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
        // Usamos runLater por si la alerta se llama desde un hilo secundario (petición HTTP)
        Platform.runLater(() -> {
            Alert alerta = new Alert(tipo);
            alerta.setTitle(titulo);
            alerta.setHeaderText(null);
            alerta.setContentText(contenido);
            alerta.showAndWait();
        });
    }
}