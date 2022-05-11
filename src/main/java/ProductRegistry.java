import java.util.Map;
import java.util.HashMap;

public class ProductRegistry {
    private Map<Product, Integer> productsToQuantity;


    public ProductRegistry() {
        this.productsToQuantity = new HashMap<Product, Integer>();
    }

    public void remove(Product product, Integer quantity) {
        productsToQuantity.put(product, quantity);
    }

    public Integer get(Product product) {
        if (!this.productsToQuantity.containsKey(product)) return -1;
        return this.productsToQuantity.get(product);
    }

    public boolean contains(Product product) {
        return this.productsToQuantity.containsKey(product);
    }

    public void update(Product product, Integer quantity) throws IllegalArgumentException {
        Integer available = this.productsToQuantity.get(product);
        if (quantity < 0 && available < Math.abs(quantity)) {
            throw new IllegalArgumentException("Not enough quantity in registry");
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

    public boolean containsExpired() {
        for (Map.Entry<Product, Integer> productToQuantity : this.productsToQuantity.entrySet()) {
            if (productToQuantity.getKey().isExpired()) {
                System.out.println(productToQuantity.getKey());
                return true;
            }
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

    public void add(Product product, Integer quantity) {
        if (!this.productsToQuantity.containsKey(product)) {
            this.productsToQuantity.put(product, 0);
        }

        this.productsToQuantity.put(product, this.productsToQuantity.get(product) + quantity);
    }

    public void subtract(ProductRegistry other) {
        for (Map.Entry<Product, Integer> productToQuantity : this.productsToQuantity.entrySet()) {
            Product product = productToQuantity.getKey();
            if (other.contains(product)) {
                int newQuantity = productToQuantity.getValue() - other.get(product);
                this.productsToQuantity.put(product, newQuantity);
            }
        }
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Product catalog\n");
        for (Map.Entry<Product, Integer> productToQuantity : this.productsToQuantity.entrySet()) {
            sb.append(String.format("Product: %s Quantity: %d\n", productToQuantity.getKey(), productToQuantity.getValue()));
        }
    
        return sb.toString();
    }
}
