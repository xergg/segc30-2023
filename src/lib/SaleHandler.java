package lib;

import exceptions.NullArgumentException;
import exceptions.WineNotFoundException;
import exceptions.NotEnoughMoneyException;
import exceptions.NotEnoughQuantitiesException;

public class SaleHandler {

	public static void buy(String userID, String seller, String wineID, int quantity) throws WineNotFoundException, NullArgumentException, 
	NotEnoughQuantitiesException, NotEnoughMoneyException {
		
		Wine wine = WineCatalog.getWine(wineID, seller);
		
		if(wine.getQuantity() >= quantity) {
			double value = wine.getQuantity() * wine.getPrice();
		
			if(AccountCatalog.getAccountByClientID(userID).get().getBalance() >= value)
				AccountCatalog.transfer(userID, seller, value);
			else 
				throw new NotEnoughMoneyException();
		} else
			throw new NotEnoughQuantitiesException();
	}

	
}
