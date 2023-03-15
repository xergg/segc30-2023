package lib;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

public class Wine {

	private String wineID;
	private List<Integer> ratings = new ArrayList<Integer>();
	private String filename;
	private double price;
	private int quantity; 
	
	public Wine(String wineID, String filename) {
		
		this.wineID = wineID;
		this.filename = filename;
		this.price = 0;
		this.quantity = 0;
	}

	public String getName() {
		return wineID;
	}

	public String getImage(){
		return filename;
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

	public double getPrice() {
		return price;
	}

	public void addStock(int quantity, int value) {	
		this.quantity += quantity;
		this.price = value;
	}
	
	public int getQuantity() {
		return this.quantity;
	}
}