/*
 * Album class
 * @author Mike Ma
 */
public class Album {
	private String title;
	private int albumDuration;
	public Song[] tracklist;
	private int numSongs = 0;


	private final int DEFAULT_MAX_SONGS = 10;

	public Album(String title) {

		this.title = title;
		tracklist = new Song[DEFAULT_MAX_SONGS];
	}

	public void addSong(Song song) {
		//if (numSongs == tracklist.length)
		if(numSongs == DEFAULT_MAX_SONGS-1)	
		expandTracklist();
		
		tracklist[numSongs] = song;
		
		numSongs++;

	}

	public String getTitle() {
		return title;
	}

	public int getAlbumDuration() {		
		for(Song s:tracklist){
			if(s !=null){
			albumDuration += s.getDuration();}
		}		
		return albumDuration;
	}

	private void expandTracklist() {

		Song[] largerList = new Song[tracklist.length * 2];

		for (int i = 0; i < tracklist.length; i++)
			largerList[i] = tracklist[i];

		tracklist = largerList;
	}

	public String toString() {
		int m = (this.getAlbumDuration() % 3600) / 60;
		int s = (this.getAlbumDuration() % 60);

		String minutes = Integer.toString(m);
		String seconds = Integer.toString(s);
		
		String string =this.getTitle()+" "+m+":"+s+"\n";
		for(int i= 0 ;  i < tracklist.length;i++){
			if(tracklist[i]!=null)
			string = string + tracklist[i].toString()+"\n";
			
		}
		return string;
	}

}
