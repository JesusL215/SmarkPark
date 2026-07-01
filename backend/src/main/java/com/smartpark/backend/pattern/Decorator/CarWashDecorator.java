package com.smartpark.backend.pattern.Decorator;

public class CarWashDecorator {
    private static final double COSTO_LAVADO = 20.0;

    public CarWashDecorator(IParkingCost parkingCost) {
        super(parkingCost);
    }

    @Override
    public double getCosto() {
        return super.getCosto() + COSTO_LAVADO;
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + Lavado de Auto";
    }
}
