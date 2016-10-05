
/*
 * Artist class
 * @author Timal Peramune
 * Student ID: 250850703
 */

import java.util.Arrays;
import java.util.stream.IntStream;

public class Artist {
	public Album[] albums;
	private Album a;;
	private String name;
	private int numAlbums = 0;
	private final int DEFAULT_MAX_ALBUMS = 5;
	String album;

	public Artist(String name) {
		this.name = name;

		albums = new Album[DEFAULT_MAX_ALBUMS];

	}

	public void addAlbum(Album album) {
		if (numAlbums == albums.length)
			expandAlbum();
		albums[numAlbums] = album;
		numAlbums++;

	}

	public boolean hasAlbum(String albumTitle) {
		return Arrays.asList(albums).contains(albumTitle);

	}

	public Album findAlbum(Album albumTitle) {
		if (Arrays.asList(albums).contains(albumTitle))
			return albumTitle;
		else
			return null;

	}

	public String getName() {

		return name;
	}

	public int getNumAlbums() {

		return numAlbums;
	}

	private void expandAlbum() {

		Album[] largerList = new Album[albums.length * 2];

		for (int i = 0; i < albums.length; i++)
			largerList[i] = albums[i];

		albums = largerList;
	}

	public int getArtistTotalDuration() {
		int sum=0;
		int x=0;
	
		for(int i = 0; i < albums.length;i++){
			if(albums[i]!=null){
				x = albums[i].getAlbumDuration();
				sum = IntStream.of(x).sum();
		 }}
		return sum;

	}

	public String toString() {
	
		String string = "";
		for(int i = 0; i < albums.length;i++){
			if(albums[i]!=null)
			string = string+ "---"+this.getName()+"---"+"\n"+albums[i].toString()+"\n";
		}
		return string;
	}

}
