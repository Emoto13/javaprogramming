package src;

import java.util.UUID;

class Client {
    private final Cart cart;
    private double money;
    private UUID id;

    public Client(double money) { 
        this.cart = new Cart();
        this.id = UUID.randomUUID();
        this.money = money;
    }

    public Cart getCart() { 
        return this.cart; 
    }

    public void addToCart(Product product) {
        this.cart.add(product);
    }

    public void addToCart(Product product, int quantity) {
        this.cart.add(product, quantity);
    }

    public void removeFromCart(Product product, int quantity) {
        this.cart.remove(product, quantity);
    }

    public double getMoney() { return this.money; }

    public void pay(double amount) throws IllegalArgumentException {
        if (amount < 0 || amount > this.money) { throw new IllegalArgumentException("amount is negative or greater than available funds"); }
        this.money -= amount;
    }

    public UUID getID() {
        return this.id;
    }

    public String toString() {
        return String.format("Client %s", this.id);
    }
}