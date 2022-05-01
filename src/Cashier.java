package src;


import java.util.UUID;

class Cashier {
    private final UUID id;
    private final String name;
    private double salary;    

    public Cashier(String name, double salary) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.salary = salary;
    }

    public double getBill(Cart cart) throws Exception {
        if (cart.containsExpired()) throw new IllegalStateException("cart contains expired products");
        return cart.calculatePrice();
    }

    public String getName() {
        return this.name;
    }

    public UUID getID() {
        return this.id;
    }

    public double getSalary() {
        return this.salary;
    }
}