package lib;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

public class Wine {

	private String wineID;
	private List<Integer> ratings = new ArrayList<Integer>();
	private List<Sale> sales = new ArrayList<Sale>(); 
	private String filename;
	//private List <String seller , int quantity, int value> sale =  

	public Wine(String wineID, String filename) {
		
		this.wineID = wineID;
		this.filename = filename;

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

	public void createSale (int value, int quantity, String userID) {
		Sale sale = new Sale(userID, quantity, value);
		sales.add(sale);
	}

	public String viewSales(){
		StringBuilder str = new StringBuilder();
		for (Sale sale : sales){
			str.append(sale.toString() + "\n");
		}

		return str.toString();
	}
}