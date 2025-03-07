package org.example;

public enum DeliveryServiceLoad {
    VERY_HIGH(1.6),
    HIGH(1.4),
    INCREASED(1.2),
    NORMAL(1.0),
    LOW(1.0);

    private final double deliveryCoefficient;

    DeliveryServiceLoad(double deliveryCoefficient) {
        this.deliveryCoefficient = deliveryCoefficient;
    }

    public double getDeliveryCoefficient() {
        return deliveryCoefficient;
    }
}
