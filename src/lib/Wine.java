package lib;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

public class Wine {

	private String wineID;
	private List<Integer> ratings = new ArrayList<Integer>();
	private List<sale> sales = new ArrayList<sale>(); 
	private String filename;
	//private List <String seller , int quantity, int value> sale =  

	public Wine(String wineID, String filename) {
		
		this.wineID = wineID;
		this.filename = filename;

	}	
	
	public String getName() {
		return wineID;
	}

	public OptionalDouble getRating() {
		OptionalDouble average = ratings
            .stream()
            .mapToDouble(a -> a)
            .average();

		return average;
	}

	public void addRating(int rating){
		ratings.add(rating);
	}
	
}
