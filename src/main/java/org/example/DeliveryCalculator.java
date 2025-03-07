package org.example;

import java.util.Objects;

public class DeliveryCalculator {
    public static final double MINIMAL_DELIVERY_COST = 400.0;
    private final double destinationDistance;
    private final CargoDimension cargoDimension;
    private final boolean isFragile;
    private final DeliveryServiceLoad deliveryServiceLoad;
    private double totalDeliveryCost;

    public DeliveryCalculator(double destinationDistance, CargoDimension cargoDimension,
                              boolean isFragile, DeliveryServiceLoad deliveryServiceLoad) {
        if (destinationDistance <= 0 || destinationDistance > 20038) {
            throw new IllegalArgumentException("Incorrect destination distance");
        }
        if (isFragile && destinationDistance > 30) {
            throw new IllegalArgumentException("Fragile goods cannot be delivered over a distance of more than 30 km.");
        }
        this.destinationDistance = destinationDistance;
        this.cargoDimension = Objects.requireNonNull(cargoDimension, "Cargo dimension cannot be null");
        this.isFragile = isFragile;
        this.deliveryServiceLoad = Objects.requireNonNull(deliveryServiceLoad, "Delivery service load cannot be null");
        calculateTotalDeliveryCost();
    }

    public double getTotalDeliveryCost() {
        return totalDeliveryCost;
    }

    public double getDestinationDistance() {
        return destinationDistance;
    }

    public CargoDimension getCargoDimension() {
        return cargoDimension;
    }

    public boolean isFragile() {
        return isFragile;
    }

    public DeliveryServiceLoad getDeliveryServiceLoad() {
        return deliveryServiceLoad;
    }

    private void calculateTotalDeliveryCost() {
        totalDeliveryCost = calculateDestinationDistanceCost();
        totalDeliveryCost += (cargoDimension == CargoDimension.LARGE) ? 200.0 : 100.0;
        if (isFragile) {
            totalDeliveryCost += 300.0;
        }
        totalDeliveryCost *= deliveryServiceLoad.getDeliveryCoefficient();
        if (totalDeliveryCost < MINIMAL_DELIVERY_COST) {
            totalDeliveryCost = MINIMAL_DELIVERY_COST;
        }
    }

    private double calculateDestinationDistanceCost() {
        double destinationDistanceCost = 0;
        if (destinationDistance <= 2) {
            destinationDistanceCost += 50.0;
        } else if (destinationDistance <= 10) {
            destinationDistanceCost += 100.0;
        } else if (destinationDistance <= 30) {
            destinationDistanceCost += 200.0;
        } else {
            destinationDistanceCost += 300.0;
        }
        return destinationDistanceCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeliveryCalculator that = (DeliveryCalculator) o;

        if (Double.compare(that.destinationDistance, destinationDistance) != 0) return false;
        if (isFragile != that.isFragile) return false;
        if (cargoDimension != that.cargoDimension) return false;
        return deliveryServiceLoad == that.deliveryServiceLoad;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(destinationDistance);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + cargoDimension.hashCode();
        result = 31 * result + (isFragile ? 1 : 0);
        result = 31 * result + deliveryServiceLoad.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Following your request:\n" + "Destination distance = " + destinationDistance + " km" +
                ",\ncargo dimension = " + cargoDimension +
                ",\nis fragile = " + isFragile +
                ",\ndelivery service load = " + deliveryServiceLoad +
                "\nThe total delivery cost is: " + totalDeliveryCost + " rubles.";
    }
}
