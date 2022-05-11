import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import src.main.java.CashRegistry;
import src.main.java.Cashier;
import src.main.java.ReceiptManager;
import src.main.java.Client;
import src.main.java.Product;
import src.main.java.ProductType;

public class TestCashRegistry {
    @Test
    public void testClientCheckoutsCorrectly() {
        CashRegistry cashRegistry = new CashRegistry(ReceiptManager.getInstance());
        Thread thread = new Thread(cashRegistry);
        thread.start();
    
        Cashier cashier = new Cashier("a", 1000.0);
        cashRegistry.setCashier(cashier);
        assertEquals(true, cashRegistry.hasCashier());
    
        double initialMoney = 10.0;
        Client client = new Client(initialMoney);
        Product product = new Product("name", 2.0, ProductType.FOOD, LocalDate.now().plusDays(2));
        client.addToCart(product, 2);

        try {
            cashRegistry.enqueueCustomer(client);
            Thread.sleep(600);
        } catch (Exception e) {}

        int expectedReceipts = 1;
        assertNotEquals(initialMoney, client.getMoney());
        assertEquals(expectedReceipts, ReceiptManager.getInstance().getReceiptCount());
    }

    @Test
    public void testSetCashier() {
        CashRegistry cashRegistry = new CashRegistry(ReceiptManager.getInstance());

        String expectedNullCashier = "Null cashier";
        Exception exceptionNullCashier = assertThrows(IllegalArgumentException.class, () -> {
            cashRegistry.setCashier(null);
        });
        assertEquals(expectedNullCashier, exceptionNullCashier.getMessage());
        
        Cashier cashier = new Cashier("a", 1000.0);
        cashRegistry.setCashier(cashier);
        assertEquals(true, cashRegistry.hasCashier());
    }
}
