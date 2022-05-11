import java.util.List;
import java.util.ArrayList;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;

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
        try {
            Files.createDirectories(Paths.get("./receipts/"));
        } catch (IOException ioe) {
            System.err.println("Couldn't create directory: "+ ioe);
        }
        
        receipt.save();
        this.receipts.add(receipt);
    }

    public int getReceiptCount() {
        return this.receipts.size();
    }

    public List<Receipt> getReceipts() {
        return this.receipts;
    }
}
