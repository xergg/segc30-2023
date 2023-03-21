package lib;

import exceptions.NullArgumentException;
import exceptions.WineAlreadyExistsException;
import exceptions.WineNotFoundException;

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

	public static Wine getWine(String wineID) throws WineNotFoundException, NullArgumentException {
		
		return WineCatalog.getWine(wineID);
	}
	

	public static void classify(String wineID, int stars) throws WineNotFoundException, NullArgumentException {
	
		Wine wine = WineCatalog.getWine(wineID);
		wine.addRating(stars);
		WineCatalog.save();
	}
}