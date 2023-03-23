package lib;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

/**
 * Class that represents Wine, implementing Serializable. Shows each wine's characteristics, such as name, image, ratings and much more.
 */
public class Wine implements Serializable{
	
    private static final long serialVersionUID = 7577955277254581557L;
	private String wineID;
	private List<Integer> ratings = new ArrayList<Integer>();
	private List<Sale> sales = new ArrayList<Sale>(); 
	private String filename;
	
	/**
	 * Basic constructor for a wine. Has wine id and the image.
	 * @param wineID given wine id
	 * @param filename given image
	 */
	public Wine(String wineID, String filename) {
		
		this.wineID = wineID;
		this.filename = filename;
	}

	/**
	 * Gets wine name
	 * @return wine name
	 */
	public String getName() {
		return wineID;
	}


	/**
	 * Gets wine image
	 * @return wine image
	 */
	public String getImage(){
		return filename;
	}


	/**
	 * Gets wine rating
	 * @return wine rating
	 */
	public Double getRating() {
		OptionalDouble average = ratings
            .stream()
            .mapToDouble(a -> a)
            .average();

		if (average.isEmpty())
			return (double) -1;
		return average.getAsDouble();
	}


	/**
	 * Adds a rating to the wine
	 * @param rating given rating to be added
	 */
	public void addRating(int rating){
		ratings.add(rating);
	}


	/**
	 * Gets sales for the given user
	 * @param userID given user id
	 * @return sales for the user if there's any
	 */
	public boolean containsSale(String userID) {
		return getSale(userID) != null;
	}


	/**
	 * Sets stock for wine
	 * @param quantity given new quantity
	 * @param value given value for the wine
	 * @param userID seller id
	 */
	public void setStock(int quantity, double value, String userID) {

		for(Sale s : sales) 
			if(s.getSeller().equals(userID)) 
				s.setStock(quantity, value);	
	}

	/**
	 * Adds a sale for the wine
	 * @param sale given sale
	 */
	public void addSale(Sale sale) {
		sales.add(sale);
	}

	/**
	 * Gets quantity
	 * @return quantity
	 */
	public int getQuantity() {
		int qty = 0;
		for(Sale s : sales)
			qty += s.getQuantity();
		return qty;
	}

	/**
	 * Gets sale given a seller
	 * @param seller seller id
	 * @return sale from seller
	 */
	public Sale getSale(String seller) {
		
		for(Sale s: sales)
			if(s.getSeller().equals(seller))
				return s;
		return null;
	}


	/**
	 * Removes sale given a seller
	 * @param seller seller id
	 */
	public void removeSale(String seller){
		sales.remove(getSale(seller));
	}

	/**
	 * Gets all sales
	 * @return all sales
	 */
	public String getAllSales(){
		StringBuilder str = new StringBuilder();
		for (Sale sale : sales){
			str.append(sale.toString() + "\n");
		}

		return str.toString();
	}
	
	
}