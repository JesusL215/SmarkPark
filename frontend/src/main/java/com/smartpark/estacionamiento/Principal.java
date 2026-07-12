package com.smartpark.estacionamiento;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Principal extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // 1. Cambiamos la vista inicial al Login
            String fxmlPath = "/com/smartpark/estacionamiento/view/Login.fxml";
            URL fxmlUrl = getClass().getResource(fxmlPath);

            if (fxmlUrl == null) {
                throw new IOException("No se pudo encontrar el archivo FXML: " + fxmlPath);
            }

            Parent root = FXMLLoader.load(fxmlUrl);

            // 2. Ajustamos el tamaño de la ventana (400x450 es ideal para un login)
            Scene scene = new Scene(root, 400, 450);

            // 3. Cambiamos el título
            primaryStage.setTitle("SmartPark - Iniciar Sesión");
            primaryStage.setScene(scene);

            // 4. (Opcional) Evitar que el usuario agrande la ventana de login
            primaryStage.setResizable(false);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}