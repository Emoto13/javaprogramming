package src;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class SalePriceCalculator {
    public static double getSalePrice(Product product) {
        ProductType type = product.getType();
        long daysLeft = ChronoUnit.DAYS.between(product.getExpirationDate(), LocalDate.now());

        switch (type) {
            case FOOD: {
                return 1.5*product.getDeliveryPrice() - 0.1*(4-daysLeft);     
            }
            case NONFOOD:
                return 1.4*product.getDeliveryPrice() - 0.1*(3 - daysLeft); 
        }

        return product.getDeliveryPrice();
    }
}
