import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class SalePriceCalculator {
    public static double foodDiscount;
    public static double nonFoodDiscount;
    public static int foodDaysLeft;
    public static int nonFoodDaysLeft;

    public static double getSalePrice(Product product) {
        ProductType type = product.getType();
        long daysLeft = ChronoUnit.DAYS.between(product.getExpirationDate(), LocalDate.now());

        switch (type) {
            case FOOD: {
                return product.getDeliveryPrice() + foodDiscount * (foodDaysLeft - daysLeft);     
            }
            case NONFOOD:
                return product.getDeliveryPrice() + nonFoodDiscount * (nonFoodDaysLeft - daysLeft); 
        }

        return product.getDeliveryPrice();
    }
}
