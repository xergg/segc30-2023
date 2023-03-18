package lib;

import exceptions.NullArgumentException;
import exceptions.WineAlreadyExistsException;
import exceptions.WineNotFoundException;
import exceptions.WineDoesNotExistException;

public class WineHandler {

	public static void create(String userID, String wineID, String filename) throws NullArgumentException, WineAlreadyExistsException{
		if(wineID == null || filename == null)
			throw new NullArgumentException();

		if(WineCatalog.exists(wineID))
			//criar funcao
			throw new WineAlreadyExistsException();

		Wine wine = new Wine(wineID, filename);
		WineCatalog.create(wine);
	}

	public static Wine getWine(String wineID, String userID) throws NullArgumentException, WineNotFoundException {

		return WineCatalog.getWine(wineID, userID);
	}


	public static Wine getWine(String wineID) throws WineNotFoundException, NullArgumentException {
		
		return WineCatalog.getWine(wineID);
	}
	

	public static void classify(String wineID, int stars) throws WineNotFoundException, NullArgumentException {
	
		Wine wine = WineCatalog.getWine(wineID);
		wine.addRating(stars);
	}

	public static void newSale(String wineID, int value, String userID, int quantity) throws WineDoesNotExistException, NullArgumentException, WineNotFoundException {
		if(!WineCatalog.exists(wineID))
			throw new WineDoesNotExistException();
		
		WineCatalog.addStock(wineID, value, userID, quantity);
	}
}