package src.test.resources;

import src.Client;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestClient {
 
    @Test
    public void testClientConstructor() {
        Client client = new Client(1.0);
        assertEquals(client.getMoney(), 1.0);
    }
    
}
