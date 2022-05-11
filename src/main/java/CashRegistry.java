import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;


public class CashRegistry extends Thread {
    private UUID id;
    private Cashier cashier;
    private ReceiptManager receiptManager;
    private BlockingQueue<Client> queue;
    private double profit;
    private ProductRegistry soldItems;

    public CashRegistry(ReceiptManager receiptManager) {
        this.id = UUID.randomUUID();
        this.receiptManager = receiptManager;
        this.queue = new LinkedBlockingDeque<Client>();
        this.soldItems = new ProductRegistry();
        this.profit = 0.0;
    }

    public CashRegistry(ReceiptManager receiptManager, Cashier cashier) {
        this.id = UUID.randomUUID();
        this.receiptManager = receiptManager;
        this.cashier = cashier;
        this.soldItems = new ProductRegistry();
        this.profit = 0.0;
    }

    public UUID getID() {
        return this.id;
    }

    public double getProfit() {
        return this.profit;
    }

    public ProductRegistry getSoldItems() {
        return this.soldItems;
    }

    public synchronized void setCashier(Cashier cashier) throws IllegalArgumentException {
        if (cashier == null) {
            throw new IllegalArgumentException("Null cashier");
        }

        if (this.cashier != null) {
            return;
        }

        this.cashier = cashier;
        this.cashier.setBusy(true);
        System.out.printf("Cashier set %s\n", this.cashier);
    }

    public synchronized void freeCashRegistry() {
        if (this.cashier == null) {
            return;
        } 
        this.cashier.setBusy(false);
        this.cashier = null;
        System.out.println("Cash registry emptied");
    }

    public synchronized boolean hasCashier() {
        return this.cashier != null;
    }

    public synchronized ProductRegistry checkoutClient() throws Exception {
        Client client = this.queue.peek();        
        if (!this.hasCashier()) {
            throw new IllegalArgumentException("no cashier at this registry");
        }        

        double bill = this.cashier.getBill(client.getCart());
        if (client.getMoney() < bill) {
            throw new IllegalArgumentException("not enough money to pay bill");
        }
        
        client.pay(bill);
        this.profit += bill;
        Receipt receipt = this.receiptManager.createReceipt(this.cashier, client.getCart().getProductRegistry());
        this.receiptManager.saveReceipt(receipt);

        System.out.printf("\nCustomer %s checked-out \n", client);
        return client.getCart().getProductRegistry();
    }

    public void enqueueCustomer(Client client) {
        this.queue.add(client);
    }

    public void dequeueCustomer() {
        this.queue.remove();
    }

    public void run() {
        System.out.println("Registry started working");
        while (true) {
            while (!this.queue.isEmpty()) {
                if (this.cashier == null) {
                    continue;
                }
    
                try {
                    ProductRegistry sold = this.checkoutClient();
                    this.soldItems.addRegistry(sold);
                } catch (Exception  e) {
                    System.err.println("Something went wrong when checkouting client." + e);
                } finally {
                    this.dequeueCustomer();
                }
            }
        }
    }

    public String toString() {
        return String.format("Cash registry %s with cashier %s", this.id, this.cashier);
    }
}