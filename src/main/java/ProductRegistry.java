import java.util.Map;
import java.util.HashMap;

public class ProductRegistry {
    private Map<Product, Integer> productsToQuantity;

    public ProductRegistry() {
        this.productsToQuantity = new HashMap<Product, Integer>();
    }

    public synchronized void remove(Product product, Integer quantity) throws IllegalArgumentException {
        if (!this.productsToQuantity.containsKey(product)) throw new IllegalArgumentException("No such product: ");
        if (this.productsToQuantity.get(product) < quantity) throw new IllegalArgumentException("Not enough of given product: ");;

        productsToQuantity.put(product, this.productsToQuantity.get(product) - quantity);
    }

    public synchronized Integer get(Product product) {
        if (!this.productsToQuantity.containsKey(product)) return -1;
        return this.productsToQuantity.get(product);
    }

    public synchronized boolean contains(Product product) {
        return this.productsToQuantity.containsKey(product);
    }

    public synchronized void update(Product product, Integer quantity) throws IllegalArgumentException {
        Integer available = this.productsToQuantity.get(product);
        if (quantity < 0 && available < Math.abs(quantity)) {
            throw new IllegalArgumentException("Not enough quantity in registry");
        }

        this.productsToQuantity.put(product, this.productsToQuantity.get(product) + quantity);
    }

    public synchronized double getValue() {
        double price = 0;

        // iterate objects in cart to get total price
        for (Map.Entry<Product, Integer> productToQuantity : this.productsToQuantity.entrySet()) {
            price += productToQuantity.getValue() * productToQuantity.getKey().getSalePrice();
        }

        return price;
    }

    public synchronized boolean containsExpired() {
        for (Map.Entry<Product, Integer> productToQuantity : this.productsToQuantity.entrySet()) {
            if (productToQuantity.getKey().isExpired()) {
                System.out.println(productToQuantity.getKey());
                return true;
            }
        }

        return false;
    }

    public synchronized Map<Product, Integer> getProducts() {
        return this.productsToQuantity;
    }

    public synchronized Product getProductByName(String name)  throws Exception {
        for (Map.Entry<Product, Integer> entry : this.productsToQuantity.entrySet()) {
            if (entry.getKey().getName().equals(name)) {
                return entry.getKey();
            }
        }
        
        throw new IllegalStateException("no product with that name was found");
    }

    public synchronized void add(Product product, Integer quantity) {
        if (product.isExpired()) {
            throw new IllegalArgumentException("Cannot add expired product: ");
        }

        if (!this.productsToQuantity.containsKey(product)) {
            this.productsToQuantity.put(product, 0);
        }

        this.productsToQuantity.put(product, this.productsToQuantity.get(product) + quantity);
    }

    public synchronized void addRegistry(ProductRegistry other, boolean onlyNotExpired) {
        for (Map.Entry<Product, Integer> productToQuantity : other.getProducts().entrySet()) {
            if (onlyNotExpired && productToQuantity.getKey().isExpired()) {
                continue;
            }
            this.add(productToQuantity.getKey(), productToQuantity.getValue());
        }
    }

    public synchronized void subtract(ProductRegistry other) {
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
        sb.append("Products\n");
        for (Map.Entry<Product, Integer> productToQuantity : this.productsToQuantity.entrySet()) {
            sb.append(String.format("Product: %s Quantity: %d\n", productToQuantity.getKey(), productToQuantity.getValue()));
        }
    
        return sb.toString();
    }
}
