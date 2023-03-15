package lib;

import java.util.HashMap;
import java.util.Map;

import exceptions.NullArgumentException;
import exceptions.WineNotFoundException;

public class WineCatalog {

    private static final WineCatalog INSTANCE = new WineCatalog();
    private static Map <String, Wine> wineList = new HashMap<>();
    private static Map <String, Map<String, Wine>> sellersList = new HashMap<>();
    
    private WineCatalog() {}
    
    public static WineCatalog getInstance(){
        return INSTANCE;
    }

	public static void create(Wine wine) {
		wineList.put(wine.getName(), wine);
	}

	public static void buyWine(String wineID, String seller, int quantity) {
		// TODO Auto-generated method stub
		
	}

	public static Wine getWine(String wineID) throws NullArgumentException, WineNotFoundException {
		if(wineID == null)
			throw new NullArgumentException();
		if(!wineList.containsKey(wineID))
			throw new WineNotFoundException();

		return wineList.get(wineID);
	}
	

	public static Wine getWine(String wineID, String userID) throws NullArgumentException, WineNotFoundException {
		
		if(wineID == null)
			throw new NullArgumentException();
		if(!wineList.containsKey(wineID))
			throw new WineNotFoundException();

		return sellersList.get(userID).get(wineID);
	}


	public static void addStock(Wine wine, int value, String userID, int quantity) {
		
		if(sellersList.containsKey(userID))
			sellersList.get(userID).get(wine.getName()).addStock(quantity, value);
		else {
			Map <String, Wine> newWine = new HashMap <>();
			newWine.put(wine.getName(), wine);
			sellersList.put(userID, newWine);
			wine.addStock(quantity, value);
		}
	}

	public static boolean exists(String wineID) {
		return wineList.get(wineID) != null;
	}

}