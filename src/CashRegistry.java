package src;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;


class CashRegistry extends Thread {
    private UUID id;
    private Cashier cashier;
    private ReceiptManager receiptManager;
    private BlockingQueue<Client> queue;


    public CashRegistry(ReceiptManager receiptManager) {
        this.id = UUID.randomUUID();
        this.receiptManager = receiptManager;
        this.queue = new LinkedBlockingDeque<Client>();
    }

    public CashRegistry(ReceiptManager receiptManager, Cashier cashier) {
        this.id = UUID.randomUUID();
        this.receiptManager = receiptManager;
        this.cashier = cashier;
    }

    public UUID getID() {
        return this.id;
    }

    public synchronized void setCashier(Cashier cashier) {
        if (cashier == null) { 
            this.cashier.setBusy(false);
            this.cashier = null;
            System.out.println("Cash registry emptied");
            return;
        }

        if (this.cashier != null) {
            return;
        }

        this.cashier = cashier;
        this.cashier.setBusy(true);
        System.out.printf("Cashier set %s\n", this.cashier);
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
        Receipt receipt = this.receiptManager.createReceipt(this.cashier, client.getCart().getProductRegistry());
        this.receiptManager.saveReceipt(receipt);

        System.out.printf("\nCustomer %s checked-out \n", client);
        return client.getCart().getProductRegistry();
    }

    public synchronized void enqueueCustomer(Client client) throws Exception {
        this.queue.add(client);
    }

    public synchronized void dequeueCustomer() {
        this.queue.remove();
    }

    public void run() {
        System.out.println("Registry started working");
        while (true) {
            if (this.cashier == null) {
                continue;
            }

            while (!this.queue.isEmpty()) {
                try {
                    this.checkoutClient();
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