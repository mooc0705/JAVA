package observe;


import java.util.Observable;


/** A product in a store. **/
public class Product extends Observable{
	private String name;
	private double d;
	private String store;
	

    /**
     * Creates a {@link e3soln.Product} with the given name, price, and store.
     * @param name name of the new {@link e3soln.Product}
     * @param d price of the new {@link e3soln.Product}
     * @param store store of the new {@link e3soln.Product}
     */
    // constructor
	public Product(String name, double d, String store){

		this.name = name;
		this.d = d;
		this.store = store;
		
		
	}
	
    /**
     * Returns the name of this {@link e3soln.Product}.
     * @return the name of this {@link e3soln.Product}
     */
    // getName
	public String getName(){
		
		return name;
		
	}
	

    /**
     * Returns the price of this {@link e3soln.Product}.
     * @return the price of this {@link e3soln.Product} 
     */
    // getPrice (returns a double)
	public double getPrice(){

		return d;
	}
    /**
     * Returns the store of this {@link e3soln.Product}.
     * @return the store of this {@link e3soln.Product}
     */
    // getStore
	public String getStore(){
		
		return store;
	}
    /**
     * Changes the price of this {@link e3soln.Product} to newPrice. All 
     * observers are notified, if the price is changed.
     * @param newPrice the new price of this {@link e3soln.Product}
     */
    // changePrice
	public void changePrice(double newPrice){
		if(this.d!=newPrice){
			this.d = newPrice;
			PriceChange pricechange = new PriceChange(this);
			setChanged();
			notifyObservers(pricechange);
		}	
		
	}
	
	public String toString(){
		return "The price of "+ name+" at "+store+" is "+String.format("%.2f", d)+".";
	}

}
