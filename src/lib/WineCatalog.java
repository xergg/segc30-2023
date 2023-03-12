package lib;

import java.util.HashMap;
import java.util.Map;

public class WineCatalog {

    private static final WineCatalog INSTANCE = new WineCatalog();
    private static Map<String, Wine> wineList = new HashMap<>();
    
    private WineCatalog() {}
    
    public static WineCatalog getInstance(){
        return INSTANCE;
    }

	public static void create(Wine wine) {
		
		wineList.put(wine.getName(), wine);
		
	}

	public static void add(String wineID, int value, int quantity, String userID) {
		// TODO Auto-generated method stub
		
	}

	public static Wine getWine(String wineID) {
		return wineList.get(wineID);
	}

	public static void classify(String wineID, int stars) {
		
		Wine wine = getWine(wineID);

		wine.addRating(stars);

	}

	public static double getPrice(String wineID, String seller, int quantity) {
		// TODO Auto-generated method stub
		return 0;
	}

	public static void buyWine(String wineID, String seller, int quantity) {
		// TODO Auto-generated method stub
		
	}

}
