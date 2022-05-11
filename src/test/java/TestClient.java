import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestClient {
 
    @Test
    public void testClientConstructor() {
        Client client = new Client(1.0);
        assertEquals(client.getMoney(), 1.0);
    }

    @Test
    public void testClientCart() {
        Client client = new Client(10);
        Product product = new Product("name", 2.0, ProductType.FOOD, LocalDate.now());
        client.addToCart(product, 1);
        assertEquals((int) client.getCart().getProductRegistry().get(product), 1);
    
    
        client.removeFromCart(product, 1);
        assertEquals((int) client.getCart().getProductRegistry().get(product), 0);
    }

    @Test
    public void testPay() {
        Client client = new Client(10.0);
        client.pay(10.0);
        assertEquals(0.0, client.getMoney());

        String expectedMessage = "Amount is negative or greater than available funds";
        Exception exceptionMoreThanAvailable = assertThrows(IllegalArgumentException.class, () -> {
            client.pay(10.0);
        });
        assertEquals(expectedMessage, exceptionMoreThanAvailable.getMessage());

        Exception exceptionNegativeAmount = assertThrows(IllegalArgumentException.class, () -> {
            client.pay(-1.0);
        });
        assertEquals(expectedMessage, exceptionNegativeAmount.getMessage());
    }

}
