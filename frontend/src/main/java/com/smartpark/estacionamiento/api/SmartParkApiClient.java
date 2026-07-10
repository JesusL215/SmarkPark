package com.smartpark.estacionamiento.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smartpark.estacionamiento.model.domain.ParkingSlot;
import com.smartpark.estacionamiento.model.domain.Ticket;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class SmartParkApiClient {

    private static final String BASE_URL = "https://smarkpark.onrender.com/api";
    private final HttpClient httpClient;
    private final Gson gson;

    public SmartParkApiClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    public List<ParkingSlot> obtenerTodosLosSlots() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/slots"))
                .GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return gson.fromJson(response.body(), new TypeToken<List<ParkingSlot>>(){}.getType());
        }
        throw new Exception("Error al obtener slots: " + response.statusCode());
    }

    public Ticket registrarEntrada(String placa, String tipoVehiculo, Long slotId) throws Exception {
        // Creamos la petición POST con los parámetros en la URL (como los espera el backend)
        String url = String.format("%s/tickets/entrada?placa=%s&tipoVehiculo=%s&slotId=%d",
                BASE_URL, placa, tipoVehiculo, slotId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.noBody()) // En este caso los datos van en la URL
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return gson.fromJson(response.body(), Ticket.class);
        }
        throw new Exception("Error al registrar entrada: " + response.body());
    }

    public Ticket registrarSalida(Long ticketId, boolean conLavado) throws Exception {
        String url = String.format("%s/tickets/salida/%d?conLavado=%b", BASE_URL, ticketId, conLavado);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return gson.fromJson(response.body(), Ticket.class);
        }
        throw new Exception("Error al registrar salida: " + response.body());
    }
}