package com.smartpark.backend.pattern.Decorator;

public class ServiceDecorator {
    protected IParkingCost parkingCost;

    public ServiceDecorator(IParkingCost parkingCost) {
        this.parkingCost = parkingCost;
    }

    @Override
    public double getCosto() {
        return parkingCost.getCosto();
    }

    @Override
    public String getDescripcion() {
        return parkingCost.getDescripcion();
    }
}
