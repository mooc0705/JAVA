package observe;

import java.util.Date;

/**
 * An object that records a change in price of a {@link e3soln.Product}.
 * @author anya
 */
public class PriceChange {
    private Product product; // a Product whose price changed
    private Date date; // the Date when the change occurred

    /**
     * Creates a new {@link e3soln.PriceChange} for the given 
     * {@link e3soln.Product}.
     * @param product the {@link e3soln.Product} whose price changed
     */
    public PriceChange(Product product){
        this.product = product;
        this.date = new Date();
    }

    /**
     * Returns the {@link e3soln.Product} whose price changed.
     * @return the {@link e3soln.Product} whose price changed
     */
    public Product getProduct() {
        return this.product;
    }

    /**
     * Returns the {@link java.util.Date} of the price change.
     * @return the {@link java.util.Date} of the price change
     */
    public Date getDate() {
        return this.date;
    }
}
