package src;

import java.util.List;
import java.util.ArrayList;

public class ReceiptManager {
    private static ReceiptManager singleInstance = null;
    private List<Receipt> receipts;

    private ReceiptManager() {
        this.receipts = new ArrayList<Receipt>();
    }

    public static ReceiptManager getInstance() {
        if (singleInstance == null) {
             singleInstance = new ReceiptManager();
        }
        
        return singleInstance;
    }

    public Receipt createReceipt(Cashier cashier, ProductRegistry products) {
        return new Receipt(cashier, products);
    }

    public void saveReceipt(Receipt receipt) {
        receipt.save();
        this.receipts.add(receipt);
    }

    public int getReceiptCount() {
        return this.receipts.size();
    }
}
