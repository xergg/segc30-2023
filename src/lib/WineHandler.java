package lib;

import java.util.Optional;

import exceptions.NullArgumentException;
import exceptions.WineAlreadyExistsException;
import exceptions.WineNotFoundException;

public class WineHandler {

	public static void create(String wineID, String filename) throws NullArgumentException, WineAlreadyExistsException {
		
		if(wineID == null || filename == null)
			throw new NullArgumentException();
		 
		if(wineExists(wineID))
			//criar funçao
			throw new WineAlreadyExistsException();

		Wine wine = new Wine(wineID , filename);

		WineCatalog.create(wine);

	}

	public static void newSale(String wineID, int value, int quantity, String userID) throws WineNotFoundException, NullArgumentException {

		try {
			Wine wine = getWine(wineID);

			wine.createSale(value, quantity, userID);

		} catch (WineNotFoundException e) {
			
			throw e;

		} catch (NullArgumentException e1) {
			
			throw e1;
			
		}


	}

	public static Wine getWine(String wineID) throws NullArgumentException, WineNotFoundException {
		if(wineID == null)
			throw new NullArgumentException();
		if(!wineExists(wineID))
			throw new WineNotFoundException();
		
		return WineCatalog.getWine(wineID);
	}

	//TODO , n sei se faz sentido implementar, ver view no enunciado 
	public static double getPrice(String wineID, String seller, int quantity) {
		return WineCatalog.getPrice(wineID, seller, quantity);
	}

	public static void classify(String wineID, int stars) throws WineNotFoundException, NullArgumentException {
		//criar funçao
		if(wineID == null)
			throw new NullArgumentException();
		if(!wineExists(wineID))
			throw new WineNotFoundException();
		
		Wine wine = WineCatalog.getWine(wineID);

		wine.addRating(stars);
	}

	//TODO
	public static void buyWine(String wineID, String seller, int quantity) {
		WineCatalog.buyWine(wineID, seller, quantity);		
	}

	public static boolean wineExists(String wineID) throws NullArgumentException{
		if(wineID != null) {
			
			Wine wine = WineCatalog.getWine( wineID );

			return (wine != null) ? true : false ;
			
		} 
		else {
			throw new NullArgumentException();
		}


	}

}