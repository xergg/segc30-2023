package lib;

import exceptions.NullArgumentException;
import exceptions.WineAlreadyExistsException;
import exceptions.WineNotFoundException;

/**
 * Public Wine Handler that as the name implies, handles most operations regarding the wine.
 */
public class WineHandler {

	/**
	 * Creates a wine entry given a usedID, a wineID and a filename, and adds it to the catalog.
	 * @param userID the creator's id
	 * @param wineID the wine's id
	 * @param filename the image to be sent
	 * @throws NullArgumentException
	 * @throws WineAlreadyExistsException
	 */
	public static void create(String userID, String wineID, String filename) throws NullArgumentException, WineAlreadyExistsException{
		if(wineID == null || filename == null)
			throw new NullArgumentException();

		if(WineCatalog.exists(wineID))
			//criar funcao
			throw new WineAlreadyExistsException();

		Wine wine = new Wine(wineID, filename);
		WineCatalog.create(wine);
	}


	/**
	 * Gets a wine from the catalog given an id.
	 * @param wineID given wine id
	 * @return the wine
	 * @throws WineNotFoundException
	 * @throws NullArgumentException
	 */
	public static Wine getWine(String wineID) throws WineNotFoundException, NullArgumentException {
		
		return WineCatalog.getWine(wineID);
	}
	

	/**
	 * Classifies a wine with a rating of given stars.
	 * @param wineID given wine id
	 * @param stars given stars to rate the wine
	 * @throws WineNotFoundException
	 * @throws NullArgumentException
	 */
	public static void classify(String wineID, int stars) throws WineNotFoundException, NullArgumentException {
	
		Wine wine = WineCatalog.getWine(wineID);
		wine.addRating(stars);
		WineCatalog.save();
	}
}