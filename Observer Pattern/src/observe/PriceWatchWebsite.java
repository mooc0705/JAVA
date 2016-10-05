package observe;

import java.util.Observable;
import java.util.Observer;

public class PriceWatchWebsite extends Observable implements Observer{
	private String URL;
	
    /**
     * Creates a new {@link e3soln.PriceWatchWebsite} with the given URL.
     * @param url the URL of the new {@link e3soln.PriceWatchWebsite}
     */
    // constructor
	public PriceWatchWebsite(String URL){
		this.URL = URL;
		
	}
    /**
     * Returns the URL of this {@link e3soln.PriceWatchWebsite}.
     * @return the URL of this {@link e3soln.PriceWatchWebsite}
     */
    // getUrl
	public String getUrl(){
		
		return URL;
		
	}

    /**
     * Prints a message about a price change.
     * Notifies all observers of the change in price.
     */
    // update
	public void update(Observable o, Object d){
		
		
		System.out.println("You are subscribed to " + URL + ".");
		System.out.println("It was notified about a price change of " +((Product) o).getName() + 
				 " at " + ((Product) o).getStore() + 
				 " to "+ String.format("%.2f", ((Product) o).getPrice()) + ".");
	
		super.setChanged();
		super.notifyObservers(d);
		
		}

	}

	
