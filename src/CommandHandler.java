package src;

import java.io.InputStreamReader;  
import java.io.BufferedReader;  
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class CommandHandler {
    private final BufferedReader reader;
    private final ShopBuilder shopBuilder;
    private Shop shop;
    private Map<UUID, Client> clients;

    public CommandHandler(ShopBuilder shopBuilder) {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.shopBuilder  = shopBuilder;
        this.clients = new HashMap<UUID, Client>();
        this.shop = null;
    }    

    public List<Cashier> readCashiers() {
        int amount = 0;
        System.out.print("Please enter amount of cashiers in the shop: ");
        while (amount <= 0) {
            try {
                amount = Integer.parseInt(this.reader.readLine());
            } catch (Exception ex) { System.out.println("Please enter a valid quantity"); }
            if (amount <= 0) System.out.println("Please enter a valid quantity");
        }

        List<Cashier> cashiers = this.shopBuilder.createCashiers(amount);
        System.out.printf("Successfully created %d cashiers\n", amount);
        return cashiers;
    }

    public List<CashRegistry> readCashRegistries() {
        int amount = 0;
        System.out.print("Please enter amount of cashier registries in the shop: ");
        while (amount <= 0) {
            try {
                amount = Integer.parseInt(this.reader.readLine());
            } catch (Exception ex) { System.out.println("Please enter a valid quantity"); }
            if (amount <= 0) System.out.println("Please enter a valid quantity");
        }

        System.out.printf("Successfully created %d cashier registries\n", amount);
        List<CashRegistry> cashRegistries = this.shopBuilder.createCashRegistries(amount);
        return cashRegistries;
    }

    public Map<UUID, Client> readClients() {
        int amount = 0;
        System.out.print("Please enter amount of cashier registries in the shop: ");
        while (amount <= 0) {
            try {
                amount = Integer.parseInt(this.reader.readLine());
            } catch (Exception ex) { System.out.println("Please enter a valid quantity"); }
            if (amount <= 0) System.out.println("Please enter a valid quantity");
        }


        Map<UUID, Client> clients = new HashMap<UUID, Client>();
        for (int i = 0; i < amount; i++) {
            double money = (double) ThreadLocalRandom.current().nextInt(101, 1001);
            Client client = this.shopBuilder.createClient(money);
            clients.put(client.getID(), client);
        }

        return clients;
    }

    public Client readClient() {
        double amount = 0;
        System.out.print("Please enter amount of money which the client has: ");
        while (amount <= 0) {
            try {
                amount = Double.parseDouble(this.reader.readLine());
            } catch (Exception ex) { System.out.println("Please enter a valid quantity"); }
            if (amount <= 0) System.out.println("Please enter a valid quantity");
        }


        Client client = this.shopBuilder.createClient(amount);
        System.out.printf("Created client with ID: %s\n", client.getID());
        return client;
    }

    public void addToCart(UUID id, Product product, int quantity) throws Exception {
        if (!this.clients.containsKey(id)) {
            throw new IllegalStateException("No such client");
        }

        if (!this.shop.hasEnoughOfProduct(product, quantity)) {
            throw new IllegalArgumentException("Not enough product in storage");
        }

        Client client = this.clients.get(id);
        client.addToCart(product, quantity);
        this.shop.getInventory().subtract(client.getCart().getProductRegistry());
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public synchronized void run() {
        while (true) {
            System.out.print("Enter command: ");
            String input;
            try {
                input = this.reader.readLine();
            } catch (Exception e) {
                System.out.println(e);
                continue;
            }

            String[] line = input.split(", ");
            switch (line[0]) {
                case "create-client": 
                    Client client = this.readClient();
                    this.clients.put(client.getID(), client);
                    continue;
                case "client-buy":
                    if (line.length < 4) {
                        System.out.println("Not enough arguments for the command client-buy");
                        continue;
                    }

                    try  {
                        System.out.println(line[2]);
                        Product product = this.shop.getInventory().getProductByName(line[2]);
                        int quantity = Integer.parseInt(line[3]);
                        this.addToCart(UUID.fromString(line[1]), product, quantity);
                        System.out.printf("Added to cart: %s %d\n", product.getName(), quantity);
                    } catch (Exception e) {
                        System.out.printf("Something went wrong: %s\n", e);
                    }
                    continue;
                case "show-inventory":
                    System.out.println(this.shop.getInventory());
                    continue;
                case "client-checkout":
                    if (line.length < 2) throw new IllegalArgumentException("Not enough arguments for the command client-checkout");
                    CashRegistry registry = this.shop.getAvaialbleCashRegistry();
                    try {
                        UUID id = UUID.fromString(line[1]);
                        if (!this.clients.containsKey(id)) throw new IllegalArgumentException("No such client");
                
                        Client customer = this.clients.get(id);
                        registry.enqueueCustomer(customer);
                    } catch (Exception e) {
                        System.err.println("Couldn't serve customer" + e);
                    }
                    continue;
                }

        }

    }


}
