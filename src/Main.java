package src;

class Main {
    
    public static void main(String[] args) throws Exception {
        // TODO: Refactor CLI and write tests
        ReceiptManager receiptManager = ReceiptManager.getInstance();
        ShopBuilder shopBuilder = new ShopBuilder(receiptManager);
        CommandHandler commandHandler = new CommandHandler(shopBuilder);
        Shop shop = new Shop(receiptManager);
        shop.setCashiers(commandHandler.readCashiers());
        shop.setCashRegistries(commandHandler.readCashRegistries());
        shop.assignCashiersToRegistries();
        shop.startCashRegistries();

        shop.setInventory(shopBuilder.createRegistry());
        commandHandler.setShop(shop);

        commandHandler.run();
    }
}