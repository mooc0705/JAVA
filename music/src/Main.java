import java.awt.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class Main {
	private static ArrayList<String[]> list = new ArrayList<String[]>();
	static MusicLibrary library = new MusicLibrary();
    public static void main(String[] args){

        // Once you write all your classes, create a new MusicLibrary here!
         

        // Don't worry about the 'try' block - we will get to this later.
        try{
            // This code opens the data file for reading. MusicData.csv should be located in the root directory
            // of your project, i.e. one directory above 'src'.
            BufferedReader reader = new BufferedReader(new FileReader("./MusicDataSmall.txt"));

            // Read in the number of artists and songs (1st and 2nd line in the file)
            int numArtists = Integer.parseInt(reader.readLine().trim());
            int numSongs = Integer.parseInt(reader.readLine().trim());

            // A for-loop to read in all the artist names - you will need to modify this to create Artist objects
			// and store them appropriately in the MusicLibrary
            for(int i=0; i<numArtists; i++){
                // Reads the whole line, storing it in a variable
                String artistName = reader.readLine().trim();

                // Print the artist name
               // System.out.println(artistName);
            }

            // This code reads one line from the data file and splits it into an array for easy access
            for(int i=0;i<numSongs;i++){
            String[] line = reader.readLine().trim().split("\t");

            list.add(line);
            
            }
            construct(list);
                  
            
            }

		
        catch (IOException e){
       
            e.printStackTrace();
        }System.out.println(library);
         System.out.println(String.format("The average album duration is %s.", library.averageAlbumDuration()));
         System.out.println(String.format("The Artist with the most Albums in this MusicLibrary is %s.", library.mostAlbums()));
        }
    
        public static void construct(ArrayList<String[]> list)
        {
        	
        	ArrayList<String[]> copy = (ArrayList<String[]>)list.clone();
        	ArrayList<String[]> list2 = new ArrayList<String[]>();
        	
        	
        	list2.add(list.get(0));
        	for(int i=1;i<list.size();i++)
        	{
        		if(list.get(i)[1].equals(list.get(0)[1]))
        		{	
        			list2.add(list.get(i));
        			//String[] temp = list.get(i);
        			copy.remove(list.get(i));
        			
        		}
        	}
        	
        	copy.remove(list.get(0));
			

        	Artist artist = new Artist(list.get(0)[1]);
        

        	library.addArtist(recursion(list2, artist));
        	
        	if(copy.size()!=0)
        	construct(copy);
        	
        }

        public static Artist recursion(ArrayList<String[]> list, Artist artist)
        {
			
        	if(list.size()==0)
        	return artist;
        	
    
        	ArrayList<String[]> copy = (ArrayList<String[]>) list.clone();       	
        	ArrayList<String[]> list2 = new ArrayList<String[]>();
        	
        	list2.add(list.get(0));
        	for(int i=1;i<list.size();i++)
        	{
        		if(list.get(i)[2].equals(list.get(0)[2]))
        		{	
        			list2.add(list.get(i));
        			copy.remove(list.get(i));
	
        		}
        	}
        	copy.remove(list.get(0));
        	//System.out.println(list.size());
        	Album album = new Album(list.get(0)[2]);
        	artist.addAlbum(recursion2(list2,album));
        	
			

        		return recursion(copy,artist);
        		
        		
        }

        public static Album recursion2(ArrayList<String[]> list, Album album)
        {
        	
        	for(int i=0;i<list.size();i++)
        	album.addSong(new Song(list.get(i)[0], Integer.parseInt(list.get(i)[3])));
        	
        	return album;
        }



        // CODE TO TEST YOUR IMPLEMENTATION - USE THIS AFTER YOU IMPLEMENT YOUR CLASSES
        // Print out the whole library!
        
       
    
}
