import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

public class CommandHandler {
    private Shop shop;
    private Map<UUID, Client> clients;
    private final CommandReader commandReader;

    public CommandHandler(CommandReader commandReader) {
        this.commandReader = commandReader;
        this.clients = new HashMap<UUID, Client>();
        this.shop = null;
    }    

    public void addToCart(UUID id, Product product, int quantity) throws Exception {
        if (!this.clients.containsKey(id)) {
            throw new IllegalStateException("No such client");
        } else if (!this.shop.hasEnoughOfProduct(product, quantity)) {
            throw new IllegalArgumentException("Not enough product in storage");
        }

        Client client = this.clients.get(id);
        client.addToCart(product, quantity);
        this.shop.getInventory().remove(product, quantity);
    }

    public void removeFromCart(UUID id, Product product, int quantity) throws Exception {
        if (!this.clients.containsKey(id)) {
            throw new IllegalStateException("No such client");
        }

        Client client = this.clients.get(id);
        client.removeFromCart(product, quantity);
        this.shop.getInventory().add(product, quantity);
    }


    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public void run() {
        while (true) {
            String input = this.commandReader.readCommand();
            String[] line = input.split(", ");
            if (line.length == 0) continue;


            String command = line[0];
            if (command.equals("create-client")) {
                    Client client = this.commandReader.readClient();
                    this.clients.put(client.getID(), client);
            } else if (command.equals("client-buy")) {
                try  {
                    PurchaseInformation purchaseInformation = this.commandReader.readPurchase();
                    Product product = this.shop.getInventory().getProductByName(purchaseInformation.getProductName());

                    this.addToCart(purchaseInformation.getClientID(), product, purchaseInformation.getQuantity());
                    System.out.printf("Added to cart: %s %d\n", purchaseInformation.getProductName(), purchaseInformation.getQuantity());
                } catch (Exception e) {
                    System.out.printf("Something went wrong: %s\n", e);
                }
            } else if (command.equals("client-checkout")) {         
                try {
                    System.out.print("Enter client's id: ");
                    UUID id = this.commandReader.readUUID();
                    Client customer = this.clients.get(id);
                    CashRegistry registry = this.shop.getAvaialbleCashRegistry();
                    registry.enqueueCustomer(customer);
                } catch (Exception e) {
                    System.err.println("Couldn't serve customer" + e);
                }

            } else if (command.equals("remove-from-cart")) {
                try  {
                    PurchaseInformation purchaseInformation = this.commandReader.readPurchase();
                    Product product = this.shop.getInventory().getProductByName(purchaseInformation.getProductName());
    
                    this.removeFromCart(purchaseInformation.getClientID(), product, purchaseInformation.getQuantity());
                    System.out.printf("Removed from cart: %s %d\n", purchaseInformation.getProductName(), purchaseInformation.getQuantity());
    
                } catch (Exception e) {
                    System.out.printf("Something went wrong: %s\n", e);
                }
            } else if (command.equals("free-cash-registry")) {
                try {
                    System.out.print("Enter registry's id: ");
                    UUID id = this.commandReader.readUUID();
                    this.shop.freeCashRegistry(id);
                } catch (Exception e) {
                    System.err.println("Couldn't free registry: " + e.getMessage());
                }
            } else if (command.equals("restart-empty-registries")) {
                this.shop.restartInactiveRegistries();
            } else if (command.equals("show-inventory")) {
                System.out.print(this.shop.getInventory());
            } else if (command.equals("show-sold")) {
                System.out.print(this.shop.getSoldItems());
            } else if (command.equals("show-cash-registries")) {         
                for (CashRegistry c: this.shop.getCashRegistries()) {
                    System.out.println(c);
                }
            }  else if (command.equals("show-sales")) {         
                System.out.println(this.shop.getSales());
            } else if (command.equals("show-expenses")) {         
                System.out.println(this.shop.getExpenses());
            } else if (command.equals("show-salary-expenses")) {         
                System.out.println(this.shop.getSalaryExpenses());
            } else if (command.equals("show-delivery-expenses")) {         
                System.out.println(this.shop.getDeliveryExpenses());
            } else if (command.equals("show-cart")) { 
                try {
                    System.out.print("Enter client's id: ");
                    UUID id = this.commandReader.readUUID();
                    Client customer = this.clients.get(id);
                    System.out.println(customer.getCart());
                } catch (Exception e) {
                    System.err.println("Couldn't show customer's cart: " + e);
                }
            } else if (command.equals("show-receipts")) {      
                System.out.println(this.shop.getReceiptCount());
            } else if (command.equals("show-receipts-detailed")) { 
                for (Receipt c: this.shop.getReceipts()) {
                    System.out.println(c);
                }
            } else if (command.equals("help")) {
                StringBuilder sb = new StringBuilder()
                .append("Commands:\n")
                .append("create-client -> Create client instance\n")
                .append("client-buy -> Add a product from shop's inventory to a client's cart\n")
                .append("remove-from-cart -> Remove a product from client's cart\n")
                .append("free-cash-registry -> Remove a cashier from a cash registry\n")
                .append("restart-empty-registries -> Reassign available to cashiers to idle cash registries\n")
                .append("show-expenses -> Show total expenses of the shop\n")
                .append("show-salary-expenses -> Show salary expenses of the shop \n")
                .append("show-delivery-expenses -> Show delivery expenses of the shop\n")
                .append("show-cart -> Show the cart of a particular client\n")
                .append("show-inventory -> Show available products\n")
                .append("show-sold -> Show sold products\n" )
                .append("show-cash-registries -> Show cash registries and worker at the corresponding registry\n")
                .append("show-sales-profit -> Show sales profit of the shop\n")
                .append("show-receipts -> Show receipt's count\n")
                .append("show-receipts-detailed -> Show all receipts\n")
                .append("exit\n");
                System.out.println(sb.toString());
            } else if (command.equals("exit")) {
                System.exit(0);
            }
        }
    }
}
