import java.util.stream.IntStream;

/*
 * MusicLibrary class
 * @author Mike Ma
 */
public class MusicLibrary {
	public Artist[] artists;
	private final int DEFAULT_MAX_ARTISTS = 10;
	private int numArtists = 0;

	// private int NOT_FOUND = -1;

	public MusicLibrary() {

		artists = new Artist[DEFAULT_MAX_ARTISTS];

	}

	public void addArtist(Artist artist) {
		if (numArtists == artists.length)
			expandArtists();

		artists[numArtists] = artist;
		numArtists++;
	}

	public Artist findArtist(String artistName) {
		for (int i = 0; i < artists.length; i++) {
			if (artists[i].getName().equals(artistName))
				return artists[i];
		}
		return null;

	}

	public boolean searchArtist(String artistName) {
		
		for (int i = 0; i < artists.length; i++) {
			if (artists[i].getName().equals(artistName))
				return true;
		}
		return false;
	}

	public void removeArtist(String artistName) { // fix up
		if (searchArtist(artistName) == true) {
			Artist[] newArtists = new Artist[DEFAULT_MAX_ARTISTS];
			for (int i = 0; i < artists.length; i++) {
				artists[i] = artists[i + 1];
			}
			for (int i = 0; i < artists.length; i++) {
				newArtists[i] = artists[i];
				System.out.println(newArtists[i]);
			}

		}
	}

	public String mostAlbums() {
		int max = artists[0].getNumAlbums();
		Artist maxArt = null;
		for(int i=1; i<artists.length;i++){
			if(artists[i]!=null){
			if(artists[i].getNumAlbums()> max){
				max=artists[i].getNumAlbums();
				maxArt=artists[i];
			}}
		}
		
		
		return maxArt.getName(); // fix up

	}

	public String averageAlbumDuration() {
		int t=0;
		int sum=0;
		int avg = 0;
		for(Artist a:artists){
			if(a!=null){
				t=a.getArtistTotalDuration();
				sum = IntStream.of(t).sum();
			}
		}
		int r = sum / 5;
		int m = (r % 3600) / 60;
		int s = (r % 60);

		String minutes = Integer.toString(m);
		String seconds = Integer.toString(s);
		String y = minutes+":"+seconds;
		return y;
	}

	private void expandArtists() {

		Artist[] largerList = new Artist[artists.length * 2];

		for (int i = 0; i < artists.length; i++)
			largerList[i] = artists[i];

		artists = largerList;
	}

	public String toString() {
		
		String string = "";
		for(int i = 0 ; i < artists.length;i++){
			if(artists[i]!=null)
			string = string + artists[i].toString()+"\n";
		}
		return string+"\n";//album.toString()+"\n"+song.toString(); 

	}
}
