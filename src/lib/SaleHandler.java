package lib;

import exceptions.NullArgumentException;
import exceptions.SameUserException;
import exceptions.WineDoesNotExistException;
import exceptions.WineNotFoundException;
import exceptions.InvalidQuantityPriceException;
import exceptions.NotEnoughMoneyException;
import exceptions.NotEnoughQuantitiesException;

public class SaleHandler {

	public static void buy(String userID, String seller, String wineID, int quantity) throws WineNotFoundException, NullArgumentException, 
	NotEnoughQuantitiesException, NotEnoughMoneyException, SameUserException, InvalidQuantityPriceException {
		if(quantity >= 0){
			Sale sale = WineCatalog.getWine(wineID).getSale(seller);
			if (!userID.equals(seller)){
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
		} else {
			throw new InvalidQuantityPriceException();
		}
		WineCatalog.save();

	}


	public static void addStock(String wineID, double value, String userID, int quantity) throws NullArgumentException, WineNotFoundException, WineDoesNotExistException, InvalidQuantityPriceException {

		if(!WineCatalog.exists(wineID))
			throw new WineDoesNotExistException();
		if(value <= 0 || quantity <= 0)
			throw new InvalidQuantityPriceException();
		WineCatalog.addStock(wineID, value, userID, quantity);
	}

}
