import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;


public class Cashier {
    private final UUID id;
    private final String name;
    private double salary;
    private final AtomicBoolean isBusy;

    public Cashier(String name, double salary) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.salary = salary;
        this.isBusy = new AtomicBoolean(false);
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

    public boolean isBusy() {
        return this.isBusy.get();
    }

    public void setBusy(boolean busy) {
        this.isBusy.set(busy);
    } 

    public String toString() {
        return String.format("Cashier (id, name): %s, %s", this.id, this.name);
    }
}