package src.test.java;

import org.junit.jupiter.api.Test;

import src.main.java.Client;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestClient {
 
    @Test
    public void testClientConstructor() {
        Client client = new Client(1.0);
        assertEquals(client.getMoney(), 1.0);
    }
    
}
