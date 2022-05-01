package src;

class Cart {
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

    public boolean add(Product product, int quantity)  {
        if (this.products.contains(product)) {
            this.products.update(product, quantity);
        } else {
            this.products.add(product, quantity);
        }

        return true;
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