/*
 * Song class
 * @author Mike Ma
 */
public class Song {

	private String title;
	private int duration;
	
	public Song(String title, int duration ){
		this.title=title;
		this.duration= duration;
	}
	
	public int getDuration(){
		return duration;
		
	}
	public String getTitle(){
		return title;
	}
	public String toString(){
		int m = (duration%3600)/60;
		int s = (duration%60);
		
		String minutes = Integer.toString(m);
		String seconds = Integer.toString(s);
		
		return title+" "+ minutes+":"+seconds; 
	}
	
	
	
	
}
