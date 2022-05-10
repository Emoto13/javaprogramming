package src.main.java;

import java.util.UUID;

public class PurchaseInformation {
    private final UUID clientID;    
    private final String productName;
    private final int quantity;

    public PurchaseInformation(UUID clientID, String productName, int quantity) {
        this.clientID = clientID;
        this.productName = productName;
        this.quantity = quantity;
    }
    
    public UUID getClientID() {
        return this.clientID;
    }

    public String getProductName() {
        return this.productName;
    }

    public int getQuantity() {
        return this.quantity;
    }
 }
