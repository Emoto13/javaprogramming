public class Cart {
    private ProductRegistry products;

    public Cart() {
        this.products = new ProductRegistry();
    }

    public boolean add(Product product)  {
        if (this.products.contains(product)) {
            this.products.update(product, 1);
        } else {
            this.products.add(product, 1);
        }

        return true;
    }

    public void add(Product product, int quantity)  {
        if (this.products.contains(product)) {
            this.products.update(product, quantity);
        } else {
            this.products.add(product, quantity);
        }
    }

    public void remove(Product product, int quantity) throws IllegalArgumentException {
        if (!this.products.contains(product)) {
            throw new IllegalArgumentException("No such product");
        } 

        if (this.products.get(product) < quantity) {
            throw new IllegalArgumentException("Not enough product in the cart to remove");
        }

        this.products.update(product, -quantity);
    }

    public double calculatePrice() {
        return this.products.getValue();
    }

    public boolean containsExpired() {
        return this.products.containsExpired();
    }

    public ProductRegistry getProductRegistry() {
        return this.products;
    }
}