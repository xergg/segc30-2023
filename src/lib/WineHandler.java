package lib;


public class WineHandler {

	public static void create(String wineID, String filename) {
		if(wineID != null && filename != null)
			WineCatalog.create(new Wine(wineID, filename));
	}

	public static void add(String wineID, int value, int quantity, String userID) {
		WineCatalog.add(wineID, value, quantity, userID);
	}

	public static Wine getWine(String wineID) {
		return WineCatalog.getWine(wineID);
	}

	public static double getPrice(String wineID, String seller, int quantity) {
		return WineCatalog.getPrice(wineID, seller, quantity);
	}

	public static void classify(String wineID, int stars) {
		WineCatalog.classify(wineID, stars);
	}

	public static void buyWine(String wineID, String seller, int quantity) {
		WineCatalog.buyWime(wineID, seller, quantity);		
	}

}
