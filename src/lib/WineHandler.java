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

	//TODO
	public static void newSale(String wineID, int value, int quantity, String userID) {
		WineCatalog.newSale(wineID, value, quantity, userID);
	}

	public static Wine getWine(String wineID) {
		return WineCatalog.getWine(wineID);
	}

	//TODO , n sei se faz sentido implementar, ver view no enunciado 
	public static double getPrice(String wineID, String seller, int quantity) {
		return WineCatalog.getPrice(wineID, seller, quantity);
	}

	public static void classify(String wineID, int stars) throws WineNotFoundException {
		//criar funçao
		if(wineID == null)
			throw new NullArgumentException();
		if(!wineExists(wineID))
			throw new WineNotFoundException();
		
		WineCatalog.classify(wineID, stars);
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
