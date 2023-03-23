package lib;

import exceptions.NullArgumentException;
import exceptions.SameUserException;
import exceptions.WineDoesNotExistException;
import exceptions.WineNotFoundException;
import exceptions.InvalidQuantityPriceException;
import exceptions.NotEnoughMoneyException;
import exceptions.NotEnoughQuantitiesException;

/**
 * A handler for sales.
 */
public class SaleHandler {

	/**
	 * Given a buyer Id and a seller id, makes a sale for a specified quantity for the specificed wine
	 * @param userID given buyer id
	 * @param seller given seller id
	 * @param wineID given wine id
	 * @param quantity quantity to be bought
	 * @throws WineNotFoundException
	 * @throws NullArgumentException
	 * @throws NotEnoughQuantitiesException
	 * @throws NotEnoughMoneyException
	 * @throws SameUserException
	 * @throws InvalidQuantityPriceException
	 */
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


	/**
	 * Adds stock to the wine from the seller.
	 * @param wineID given wine id
	 * @param value value of the wine
	 * @param userID seller id
	 * @param quantity new quantity to be added to the current stock.
	 * @throws NullArgumentException
	 * @throws WineNotFoundException
	 * @throws WineDoesNotExistException
	 * @throws InvalidQuantityPriceException
	 */
	public static void addStock(String wineID, double value, String userID, int quantity) throws NullArgumentException, WineNotFoundException, WineDoesNotExistException, InvalidQuantityPriceException {

		if(!WineCatalog.exists(wineID))
			throw new WineDoesNotExistException();
		if(value <= 0 || quantity <= 0)
			throw new InvalidQuantityPriceException();
		WineCatalog.addStock(wineID, value, userID, quantity);
	}

}
