import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.UUID;

class Shop {
    private List<Cashier> cashiers;
    private List<CashRegistry> cashRegistries;
    private ProductRegistry inventory;
    private ReceiptManager receiptManager;

    private Cashier getFreeCashier() {
        for (Cashier cashier: this.cashiers) {
            if (!cashier.isBusy()) return cashier;
        }
        return null;
    }

    public Shop(ReceiptManager receiptManager) {
        this.receiptManager = receiptManager;    
        this.cashiers = new ArrayList<Cashier>();
        this.inventory = new ProductRegistry();
    }

    public double getSales() {
        double profit = 0.0;
        for (CashRegistry registry: this.cashRegistries) profit += registry.getProfit();
        return profit;
    }

    public double getDeliveryExpenses() {
        double expenses = 0.0;
        for (Map.Entry<Product, Integer> productToQuantity : this.inventory.getProducts().entrySet()) {
            expenses += productToQuantity.getKey().getDeliveryPrice() * productToQuantity.getValue();
        }
        for (Map.Entry<Product, Integer> productToQuantity : this.getSoldItems().getProducts().entrySet()) {
            expenses += productToQuantity.getKey().getDeliveryPrice() * productToQuantity.getValue();
        }
        return expenses;
    }

    public double getSalaryExpenses() {
        double expenses = 0.0;
        for (Cashier cashier : this.cashiers) expenses += cashier.getSalary();
        return expenses;
    }

    public double getExpenses() {
        return this.getSalaryExpenses() + this.getDeliveryExpenses();
    }
 
    public List<CashRegistry> getCashRegistries() {
        return this.cashRegistries;
    }

    public ProductRegistry getSoldItems() {
        ProductRegistry soldItems = new ProductRegistry();
        for (CashRegistry registry: this.cashRegistries) soldItems.addRegistry(registry.getSoldItems());
        return soldItems;
    }

    public ProductRegistry getInventory() {
        return this.inventory;
    }

    public void setInventory(ProductRegistry inventory) {
        this.inventory = inventory;
    }

    public void setCashiers(List<Cashier> cashiers) {
        this.cashiers = cashiers;
    }

    public void setCashRegistries(List<CashRegistry> registeries) {
        this.cashRegistries = registeries;
    }

    public void assignCashiersToRegistries() {
        int i = 0;
        for (CashRegistry r : this.cashRegistries) {
            if (r.hasCashier()) continue;
            r.setCashier(this.cashiers.get(i));
            i++;
        }
    }

    public boolean hasEnoughOfProduct(Product product, int quantity) {
        if (!this.inventory.contains(product)) return false;
        return this.inventory.get(product) >= quantity; 
    }

    public synchronized void startCashRegistries() {
        for (CashRegistry registry: this.cashRegistries) {
            Thread thread = new Thread(registry);
            thread.start();
        }
    }

    public synchronized CashRegistry getAvaialbleCashRegistry() {
        while (true) {
            int i = ThreadLocalRandom.current().nextInt(0, this.cashRegistries.size());
            if (this.cashRegistries.get(i).hasCashier()) {
                return this.cashRegistries.get(i);
            }
        }
    }
    
    public List<Receipt> getReceipts() {
        return this.receiptManager.getReceipts();
    }

    public int getReceiptCount() {
        return this.receiptManager.getReceiptCount();
    }


    public CashRegistry freeCashRegistry(UUID id) throws IllegalArgumentException {
        for (CashRegistry registry: this.cashRegistries) {
            if (registry.getID().equals(id)) {
                registry.setCashier(null);
                return registry;
            }
        }
        throw new IllegalArgumentException("No such registry");
    }

    public void restartInactiveRegistries() {
        for (CashRegistry registry: this.cashRegistries) {
            if (!registry.hasCashier()) {
                Cashier cashier = this.getFreeCashier();
                if (cashier != null) {
                    registry.setCashier(cashier);
                }
            }
        }
    }



}