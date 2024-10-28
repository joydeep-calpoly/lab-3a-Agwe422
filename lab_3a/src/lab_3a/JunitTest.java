package lab_3a;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalTime;

class JunitTest {

    private MovieTicketPriceCalculator calculator;

    @BeforeEach
    void setUp() {
        // Setup: Matinee time 10:00 to 14:00, child max age 12, senior min age 65
        calculator = new MovieTicketPriceCalculator(
                LocalTime.of(10, 0),
                LocalTime.of(14, 0),
                12,
                65
        );
    }

    @Test
    void testConstructorWithInvalidTimes() {
        assertThrows(IllegalArgumentException.class, () -> 
            new MovieTicketPriceCalculator(LocalTime.of(14, 0), LocalTime.of(10, 0), 12, 65)
        );
    }

    @Test
    void testConstructorWithNullTimes() {
        assertThrows(NullPointerException.class, () -> 
            new MovieTicketPriceCalculator(null, LocalTime.of(14, 0), 12, 65)
        );

        assertThrows(NullPointerException.class, () -> 
            new MovieTicketPriceCalculator(LocalTime.of(10, 0), null, 12, 65)
        );
    }

    @Test
    void testComputeDiscount() {
        // Senior discount
        assertEquals(400, calculator.computeDiscount(65));
        assertEquals(400, calculator.computeDiscount(70));

        // Child discount
        assertEquals(300, calculator.computeDiscount(12));
        assertEquals(300, calculator.computeDiscount(5));

        // No discount
        assertEquals(0, calculator.computeDiscount(30));
    }

    @Test
    void testComputePrice_MatineeWithDiscount() {
        // Matinee pricing for a child
        int price = calculator.computePrice(LocalTime.of(11, 0), 10);
        assertEquals(2400 - 300, price);

        // Matinee pricing for a senior
        price = calculator.computePrice(LocalTime.of(12, 0), 70);
        assertEquals(2400 - 400, price);
    }

    @Test
    void testComputePrice_MatineeWithoutDiscount() {
        // Matinee pricing with no discount
        int price = calculator.computePrice(LocalTime.of(11, 0), 30);
        assertEquals(2400, price);
    }

    @Test
    void testComputePrice_StandardWithDiscount() {
        // Standard pricing for a child
        int price = calculator.computePrice(LocalTime.of(15, 0), 10);
        assertEquals(2700 - 300, price);

        // Standard pricing for a senior
        price = calculator.computePrice(LocalTime.of(16, 0), 70);
        assertEquals(2700 - 400, price);
    }

    @Test
    void testComputePrice_StandardWithoutDiscount() {
        // Standard pricing with no discount
        int price = calculator.computePrice(LocalTime.of(15, 0), 30);
        assertEquals(2700, price);
    }

    @Test
    void testComputePrice_EdgeCases() {
        // Exactly at the start of matinee time
        int price = calculator.computePrice(LocalTime.of(10, 0), 30);
        assertEquals(2400, price);

        // Exactly at the end of matinee time (should be standard price)
        price = calculator.computePrice(LocalTime.of(14, 0), 30);
        assertEquals(2700, price);
    }
}