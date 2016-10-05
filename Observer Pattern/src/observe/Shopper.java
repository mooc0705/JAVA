package observe;

import java.util.Observable;
import java.util.Observer;

public class Shopper extends Observable implements Observer{

	private String name1;
    /**
     * Creates a new {@link e3soln.Shopper} with the given name.
     * @param name name of the new {@link e3soln.Shopper}
     */
    // constructor
	public Shopper(String name1) {
		this.name1 = name1;
		
	}

    /**
     * Returns the name of this {@link e3soln.Shopper}.
     * @return the name of this {@link e3soln.Shopper}
     */
    // getName
	public String getName(){
		return name1; 
	}

    /**
     * Prints a message about a price change.
     * @return 
     * @return 
     */
	public void update(Observable o, Object d){

		 System.out.println(name1 + " was notified about a price change of " + 
		 ((PriceChange)d).getProduct().getName() + 
		 " at " + ((PriceChange)d).getProduct().getStore() + 
		 " to "+ String.format("%.2f", ((PriceChange)d).getProduct().getPrice()) + ".");

		
	}

	
	
}
