package lib;

import exceptions.NullArgumentException;
import exceptions.SameUserException;
import exceptions.WineDoesNotExistException;
import exceptions.WineNotFoundException;
import exceptions.NotEnoughMoneyException;
import exceptions.NotEnoughQuantitiesException;

public class SaleHandler {

	public static void buy(String userID, String seller, String wineID, int quantity) throws WineNotFoundException, NullArgumentException, 
	NotEnoughQuantitiesException, NotEnoughMoneyException, SameUserException {

		Sale sale = WineCatalog.getWine(wineID).getSale(seller);
		if (userID != seller){
			if(sale != null && sale.getQuantity() >= quantity) {
				//alterei sale.quantity pra quantity
				double value = quantity * sale.getValue();
				if(AccountCatalog.getAccountByClientID(userID).get().getBalance() >= value){
					AccountCatalog.transfer(userID, seller, value);
					// diminui-se a quantidade de vinhos na sale
					sale.setQuantity(quantity);
					if(sale.getQuantity()<=0)
						WineCatalog.getWine(wineID).removeSale(seller);
				}
				else {
					throw new NotEnoughMoneyException();
				}
			} else
				throw new NotEnoughQuantitiesException();
		} else {
			throw new SameUserException();
		}

		WineCatalog.save();
	}


	public static void addStock(String wineID, double value, String userID, int quantity) throws NullArgumentException, WineNotFoundException, WineDoesNotExistException {

		if(!WineCatalog.exists(wineID))
			throw new WineDoesNotExistException();
		WineCatalog.addStock(wineID, value, userID, quantity);
	}

}
