import org.example.CargoDimension;
import org.example.DeliveryCalculator;
import org.example.DeliveryServiceLoad;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class UnitTests {
    @DataProvider
    public Object[][] minimalDeliveryCostData() {
        return new Object[][]{{1.0, CargoDimension.SMALL, false, DeliveryServiceLoad.LOW},
                {2.0, CargoDimension.SMALL, false, DeliveryServiceLoad.NORMAL},
                {1.0, CargoDimension.LARGE, false, DeliveryServiceLoad.LOW}
        };
    }

    @Test(dataProvider = "minimalDeliveryCostData", description = "Minimal delivery cost test",
            groups = {"smoke", "positive"})
    public void minimalDeliveryCostTest(double destinationDistance, CargoDimension cargoDimension, boolean isFragile,
                                        DeliveryServiceLoad deliveryServiceLoad) {
        DeliveryCalculator deliveryCalculator = new DeliveryCalculator(destinationDistance, cargoDimension, isFragile,
                deliveryServiceLoad);

        assertEquals(deliveryCalculator.getTotalDeliveryCost(), DeliveryCalculator.MINIMAL_DELIVERY_COST);
    }

    @DataProvider
    public Object[][] boundaryDeliveryCostData() {
        return new Object[][]{{0.01, CargoDimension.LARGE, true, DeliveryServiceLoad.VERY_HIGH, 880.0},
                {2.0, CargoDimension.SMALL, false, DeliveryServiceLoad.HIGH, 400.0},
                {2.01, CargoDimension.LARGE, false, DeliveryServiceLoad.INCREASED, 400.0},
                {10.0, CargoDimension.SMALL, true, DeliveryServiceLoad.NORMAL, 500.0},
                {10.01, CargoDimension.LARGE, true, DeliveryServiceLoad.LOW, 700.0},
                {30.0, CargoDimension.SMALL, true, DeliveryServiceLoad.VERY_HIGH, 960.0},
                {30.01, CargoDimension.LARGE, false, DeliveryServiceLoad.HIGH, 700.0},
                {20038.0, CargoDimension.SMALL, false, DeliveryServiceLoad.INCREASED, 480.0}
        };
    }

    @Test(dataProvider = "boundaryDeliveryCostData", description = "Boundary destination distance delivery cost test",
            groups = {"boundary", "positive"})
    public void boundaryDeliveryCostPositiveTest(double destinationDistance, CargoDimension cargoDimension,
                                                 boolean isFragile, DeliveryServiceLoad deliveryServiceLoad,
                                                 double expectedTotalDeliveryCost) {
        DeliveryCalculator deliveryCalculator = new DeliveryCalculator(destinationDistance, cargoDimension, isFragile,
                deliveryServiceLoad);

        assertEquals(deliveryCalculator.getTotalDeliveryCost(), expectedTotalDeliveryCost);
    }

    @Test(description = "Inability to deliver fragile cargo at more than 30 km distance test",
            groups = {"smoke", "negative"})
    public void fragileCargoAndLongDistanceTest() {
        final String expectedErrorMessage = "Fragile goods cannot be delivered over a distance of more than 30 km.";

        try {
            new DeliveryCalculator(30.01, CargoDimension.LARGE, true, DeliveryServiceLoad.HIGH);
            Assert.fail("IllegalArgumentException is expected");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), expectedErrorMessage);
        }
    }

    @DataProvider
    public Object[][] incorrectDestinationData() {
        return new Object[][]{{0.0, CargoDimension.LARGE, true, DeliveryServiceLoad.VERY_HIGH},
                {-1.0, CargoDimension.SMALL, false, DeliveryServiceLoad.HIGH},
                {20038.01, CargoDimension.LARGE, false, DeliveryServiceLoad.INCREASED}
        };
    }

    @Test(dataProvider = "incorrectDestinationData", description = "Incorrect destination distance delivery cost test",
            groups = {"negative"})
    public void incorrectDestinationDistanceTest(double destinationDistance, CargoDimension cargoDimension,
                                                 boolean isFragile, DeliveryServiceLoad deliveryServiceLoad) {
        final String expectedErrorMessage = "Incorrect destination distance";

        try {
            new DeliveryCalculator(destinationDistance, cargoDimension, isFragile, deliveryServiceLoad);
            Assert.fail("IllegalArgumentException is expected");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), expectedErrorMessage);
        }
    }

    @Test(description = "Null cargo dimension value test", groups = {"negative"})
    public void nullCargoDimensionValueTest() {
        final String expectedErrorMessage = "Cargo dimension cannot be null";

        try {
            new DeliveryCalculator(5, null, true, DeliveryServiceLoad.LOW);
            Assert.fail("NullPointerException is expected");
        } catch (NullPointerException e) {
            assertEquals(e.getMessage(), expectedErrorMessage);
        }
    }

    @Test(description = "Null delivery service load value test", groups = {"negative"})
    public void nullDeliveryServiceLoadValueTest() {
        final String expectedErrorMessage = "Delivery service load cannot be null";

        try {
            new DeliveryCalculator(5, CargoDimension.SMALL, true, null);
            Assert.fail("NullPointerException is expected");
        } catch (NullPointerException e) {
            assertEquals(e.getMessage(), expectedErrorMessage);
        }
    }
}
