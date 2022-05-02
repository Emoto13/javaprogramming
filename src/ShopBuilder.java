package src;

import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;


public class ShopBuilder {  
    private final ReceiptManager receiptManager;

    public ShopBuilder(ReceiptManager receiptManager) {
        this.receiptManager = receiptManager;
    }

    public List<CashRegistry> createCashRegistries(int quantity) {
        List<CashRegistry> cashRegistries = new ArrayList<CashRegistry>();
        for (int i = 0; i < quantity; i++) {
            cashRegistries.add(new CashRegistry(receiptManager));
        }   

        return cashRegistries;
    }     

    public List<Cashier> createCashiers(int quantity) {
        List<Cashier> cashiers = new ArrayList<Cashier>();
        for (int i = 0; i < quantity; i++) {
            cashiers.add(new Cashier(String.format("Worker %d", i), 1000.0*quantity));
        }

        return cashiers;
    }

    public Client createClient(double money) {
        return new Client(money);
    }

    public ProductRegistry createRegistry() {
        int numberOfProducts = ThreadLocalRandom.current().nextInt(1, 11);
        ProductRegistry productRegistry = new ProductRegistry();
        for (int i = 0; i < numberOfProducts; i++) {
            ProductType type = i % 2 == 0 ? ProductType.FOOD : ProductType.NONFOOD;
            double price = (double) ThreadLocalRandom.current().nextInt(1, 11);
            LocalDate expirationDate = LocalDate.now().plusDays(i+1);
            Product product = new Product(String.format("Product%d", i), price, type, expirationDate);
            int quantity = ThreadLocalRandom.current().nextInt(1, 6);
            
            productRegistry.add(product, quantity);
        }
        System.out.println("Created product registry");
        return productRegistry;
    }
}
