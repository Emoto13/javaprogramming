package src;

import java.time.LocalDate;
import java.util.UUID;

enum ProductType {
    FOOD,
    NONFOOD
}

class Product {
    private final UUID id;
    private final String name;
    private final double deliveryPrice; // BigDecimal?
    private final ProductType type;
    private final LocalDate exipirationDate;


    public Product(String name, double deliveryPrice, ProductType type, LocalDate exipirationDate) {
        // add validation
        this.id = UUID.randomUUID();
        this.name = name;
        this.deliveryPrice = deliveryPrice;
        this.type = type;
        this.exipirationDate = exipirationDate;
    }
    
    public double getDeliveryPrice() {
        return this.deliveryPrice;
    }

    public double getSalePrice() {
        double p = SalePriceCalculator.getSalePrice(this);
        return p;
    }

    public LocalDate getExpirationDate() {
        return this.exipirationDate;
    }

    public ProductType getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public UUID getID() {
        return this.id;
    }

    public boolean isExpired() {
        return !this.exipirationDate.isAfter(LocalDate.now());
    }

    public String toString() {
        return String.format("%s, %s, %.2f", this.id.toString(), this.name, this.getSalePrice());
    }
}