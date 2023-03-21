package lib;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

public class Wine implements Serializable{
	
    private static final long serialVersionUID = 7577955277254581557L;
	private String wineID;
	private List<Integer> ratings = new ArrayList<Integer>();
	private List<Sale> sales = new ArrayList<Sale>(); 
	private String filename;
	
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

	public boolean containsSale(Sale sale) {
		return sales.contains(sale);
	}

	public void setStock(int quantity, double value, String userID) {
		for(Sale s : sales)
			if(s.getSeller().equals(userID))
				s.setStock(quantity, value);
			
	}

	public void addSale(Sale sale) {
		sales.add(sale);
	}

	public int getQuantity() {
		int qty = 0;
		for(Sale s : sales)
			qty += s.getQuantity();
		return qty;
	}
	
	
}