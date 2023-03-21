package lib;

import exceptions.NullArgumentException;
import exceptions.WineDoesNotExistException;
import exceptions.WineNotFoundException;

import java.util.HashMap;
import java.util.Map;

import exceptions.NotEnoughMoneyException;
import exceptions.NotEnoughQuantitiesException;

public class SaleHandler {

	public static void buy(String userID, String seller, String wineID, int quantity) throws WineNotFoundException, NullArgumentException, 
	NotEnoughQuantitiesException, NotEnoughMoneyException {

		Sale sale = WineCatalog.getWine(wineID).getSale(seller);

		if(sale != null && sale.getQuantity() >= quantity) {
			double value = sale.getQuantity() * sale.getValue();

			if(AccountCatalog.getAccountByClientID(userID).get().getBalance() >= value)
				AccountCatalog.transfer(userID, seller, value);
			else 
				throw new NotEnoughMoneyException();
		} else
			throw new NotEnoughQuantitiesException();
	}


	public static void addStock(String wineID, double value, String userID, int quantity) throws NullArgumentException, WineNotFoundException, WineDoesNotExistException {

		if(!WineCatalog.exists(wineID))
			throw new WineDoesNotExistException();
		WineCatalog.addStock(wineID, value, userID, quantity);
	}
}
