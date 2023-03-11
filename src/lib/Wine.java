package lib;

public class Wine {

	private String wineID;
	private int rating;
	private int unity;
	private String seller;

	public Wine(String wineID, String filename, String seller) {
		
		this.wineID = wineID;
		this.rating = 0;
		this.unity = 0;
		this.seller = seller;
	
	}	
	
	public String getName() {
		return wineID;
	}

	public int getRating() {
		return rating;
	}

	public int getUnity() {
		return unity;
	}
	
	public String getSeller() {
		return seller;
	}
	
	
}
