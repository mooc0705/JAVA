package observe;

import java.util.*;

public class Observable {
	protected List<Observer> obs ;
	
	public Observable(){
		obs = new ArrayList<Observer>();
	}
	
	public void addObserver(Observer x){
		obs.add(x);
	}
}
