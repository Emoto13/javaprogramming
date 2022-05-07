package src;

import java.util.Map;
import java.time.LocalDateTime;
import java.util.UUID;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


class Receipt {
    private final UUID id;
    private final LocalDateTime time;

    private final Cashier cashier;
    private final ProductRegistry products;

    public Receipt(Cashier cashier, ProductRegistry products) {
        this.id = UUID.randomUUID();
        this.time = LocalDateTime.now();
        this.cashier = cashier;
        this.products = products;
    }

    public void save() {
        String filename = new String(String.format("receipts/%s.txt", this.id));
        try {
            File file = new File(filename);
            file.createNewFile();
        } catch (IOException exception) {
            System.err.println("Failed to create file for receipt: " + exception);
        }

        try {
            FileWriter writer = new FileWriter(filename);
            writer.write(this.toString());
            writer.close();
        } catch (IOException exception) {
            System.err.println("Failed to write receipt to file" + exception);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Receipt: %s\n", this.id));
        sb.append(String.format("Time: %s\n", this.time));
        sb.append(String.format("Cashier: %s, %s\n", this.cashier.getID(), this.cashier.getName()));
        sb.append("Products: (id, name, quantity) \n");
        for (Map.Entry<Product, Integer> entry : this.products.getProducts().entrySet()) {  
            double total = entry.getValue() * entry.getKey().getSalePrice();
            sb.append(String.format("Product: %s, Quantity: %d, Total: %.2f\n", entry.getKey(), entry.getValue(), total));            
        }

        return sb.toString();
    }

}