import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestProductRegistry {
    @Test
    public void testUpdate() {
        ProductRegistry registry = new ProductRegistry();
        Product product = new Product("name", 2.0, ProductType.FOOD, LocalDate.now().plusDays(1));
        registry.add(product, 0);

        String expectedMessage = "Not enough quantity in registry";
        Exception exceptionMoreThanAvailable = assertThrows(IllegalArgumentException.class, () -> {
            registry.update(product, -1);
        });
        assertEquals(expectedMessage, exceptionMoreThanAvailable.getMessage());

        registry.update(product, 1);
        assertEquals(1, (int) registry.get(product));
    }


    @Test
    public void testSubtract() {
        ProductRegistry registry1 = new ProductRegistry();
        ProductRegistry registry2 = new ProductRegistry();

        Product product = new Product("name", 2.0, ProductType.FOOD, LocalDate.now().plusDays(1));
        registry1.add(product, 1);
        registry2.add(product, 1);
        registry1.subtract(registry2);
        assertEquals(0, (int) registry1.get(product));
        assertEquals(1, (int) registry2.get(product));
    }


    @Test
    public void testAdd() {
        ProductRegistry registry = new ProductRegistry();
        Product product = new Product("name", 2.0, ProductType.FOOD, LocalDate.now().plusDays(1));

        registry.add(product, 1);
        assertEquals(1, (int) registry.get(product));

        registry.add(product, 1);
        assertEquals(2, (int) registry.get(product));
    }
}
