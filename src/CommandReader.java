package src;

import java.io.InputStreamReader;  
import java.io.BufferedReader;  
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.io.IOException;


public class CommandReader {
    private final BufferedReader reader;
    private final ShopBuilder shopBuilder;

    public CommandReader(ShopBuilder shopBuilder) {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.shopBuilder  = shopBuilder;
    }

    private int readInt() {
        int amount = 0;
        while (amount <= 0) {
            try {
                amount = Integer.parseInt(this.reader.readLine());
            } catch (Exception ex) { 
                System.out.print("Please enter a valid quantity: "); 
                continue;
            }
            if (amount <= 0) System.out.print("Please enter a valid quantity: ");
        }
        return amount;
    }

    private double readDouble() {
        double amount = 0;
        while (amount <= 0) {
            try {
                amount = Double.parseDouble(this.reader.readLine());
            } catch (Exception ex) {
                System.out.print("Please enter a valid quantity: "); 
                continue;
            }
            if (amount <= 0) System.out.print("Please enter a valid quantity: ");
        }
        return amount;
    }

    public UUID readUUID() {
        UUID id;
        while (true) {
            try {
                id = UUID.fromString(this.reader.readLine());
                return id;
            } catch (Exception ex) { 
                System.out.print("Please enter a valid id: "); 
            }
        }
    }


    public List<Cashier> readCashiers() {
        System.out.print("Enter number of cashiers: ");
        int amount = this.readInt();
        List<Cashier> cashiers = this.shopBuilder.createCashiers(amount);
        System.out.printf("Successfully created %d cashiers\n", amount);
        return cashiers;
    }

    public List<CashRegistry> readCashRegistries() {
        System.out.print("Enter number of cash registries: ");
        int amount = this.readInt();
        System.out.printf("Successfully created %d cashier registries\n", amount);
        List<CashRegistry> cashRegistries = this.shopBuilder.createCashRegistries(amount);
        return cashRegistries;
    }

    public Map<UUID, Client> readClients() {
        int amount = this.readInt();

        Map<UUID, Client> clients = new HashMap<UUID, Client>();
        for (int i = 0; i < amount; i++) {
            double money = (double) ThreadLocalRandom.current().nextInt(101, 1001);
            Client client = this.shopBuilder.createClient(money);
            clients.put(client.getID(), client);
        }

        return clients;
    }

    public Client readClient() {
        System.out.print("Enter client's amout of money: ");
        double amount = this.readDouble();
        Client client = this.shopBuilder.createClient(amount);
        System.out.printf("Created client with ID: %s\n", client.getID());
        return client;
    }

    public PurchaseInformation readPurchase() {
        while (true) {
            try {
                System.out.print("Enter client id: ");
                UUID clientID = this.readUUID();

                System.out.print("Enter product name: ");
                String productName = this.reader.readLine();

                System.out.print("Enter amount: ");
                int amount = this.readInt();

                return new PurchaseInformation(clientID, productName, amount);
            } catch (IOException e) {
                System.err.printf("Something went wrong when reading from command line: %s\n", e.getMessage());
            }
        }
    }

    public String readCommand() {
        System.out.print("Enter command: ");
        String input = "";
        try {
            input = this.reader.readLine();
        } catch (Exception e) {
            System.out.println(e);
        }

        return input;
    }

}
