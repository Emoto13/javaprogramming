package src;

import java.util.Map;
import java.util.HashMap;

public class ProductRegistry {
    private Map<Product, Integer> productsToQuantity;


    public ProductRegistry() {
        this.productsToQuantity = new HashMap<Product, Integer>();
    }

    public void add(Product product, Integer quantity) {
        productsToQuantity.put(product, quantity);
    }

    public boolean contains(Product product) {
        return this.productsToQuantity.containsKey(product);
    }

    public void update(Product product, Integer quantity) throws IllegalArgumentException {
        Integer available = this.productsToQuantity.get(product);
        if (available < quantity) {
            throw new IllegalArgumentException("not enough quantity in registry");
        }

        this.productsToQuantity.put(product, this.productsToQuantity.get(product) + quantity);
    }

    public double getValue() {
        double price = 0;

        // iterate objects in cart to get total price
        for (Map.Entry<Product, Integer> productToQuantity : this.productsToQuantity.entrySet()) {
            price += productToQuantity.getValue() * productToQuantity.getKey().getSalePrice();
        }

        return price;
    }

    public int getQuantity(Product product) {
        if (!this.productsToQuantity.containsKey(product)) return -1;
        return this.productsToQuantity.get(product);
    }

    public boolean containsExpired() {
        for (Map.Entry<Product, Integer> productToQuantity : this.productsToQuantity.entrySet()) {
            if (productToQuantity.getKey().isExpired()) return true;
        }

        return false;
    }

    public Map<Product, Integer> getProducts() {
        return this.productsToQuantity;
    }

    public Product getProductByName(String name)  throws Exception {
        for (Map.Entry<Product, Integer> entry : this.productsToQuantity.entrySet()) {
            if (entry.getKey().getName().equals(name)) {
                return entry.getKey();
            }
        }
        
        throw new IllegalStateException("no product with that name was found");
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Product catalog\n");
        for (Map.Entry<Product, Integer> productToQuantity : this.productsToQuantity.entrySet()) {
            sb.append(String.format("Product: %s Quantity: %d\n", productToQuantity.getKey(), productToQuantity.getValue()));
        }
    
        return sb.toString();
    }

    public void subtract(ProductRegistry other) {
        for (Map.Entry<Product, Integer> productToQuantity : this.productsToQuantity.entrySet()) {
            Product product = productToQuantity.getKey();
            if (other.contains(product)) {
                int newQuantity = productToQuantity.getValue() - other.getQuantity(product);
                this.productsToQuantity.put(product, newQuantity);
            }
        }
    }
}
