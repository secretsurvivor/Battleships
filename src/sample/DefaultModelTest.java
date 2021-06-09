package sample;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DefaultModelTest {
    private DefaultModel model;

    @BeforeEach
    public void setUp() {
        model = new DefaultModel(new int[]{5, 4, 3, 2, 2});
    }
    // Check Position; true and false
    // Check Ship size sum
    // Check Int Position converter

    @Test
    public void testShipPositionSum() { // Test Ship position sum
        assertEquals(16, model.getShipPositions(), "Should return correct sum of ship sizes");
    }

    @Test
    public void testIntPos() {
        assertEquals(44, DefaultModel.getIntPos("E4"), "Should return correct converted value");
    }


}
