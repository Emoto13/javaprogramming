package src;

class Main {
    
    public static void main(String[] args) throws Exception {
        ReceiptManager receiptManager = ReceiptManager.getInstance();
        ShopBuilder shopBuilder = new ShopBuilder(receiptManager);
        
        CommandReader commandReader = new CommandReader(shopBuilder);
        CommandHandler commandHandler = new CommandHandler(commandReader);
        Shop shop = new Shop(receiptManager);
    
        shop.setCashiers(commandReader.readCashiers());
        shop.setCashRegistries(commandReader.readCashRegistries());

        shop.assignCashiersToRegistries();
        shop.startCashRegistries();
        shop.setInventory(shopBuilder.createRegistry());

        commandHandler.setShop(shop);
        commandHandler.run();
    }
}