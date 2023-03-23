package lib;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import exceptions.NullArgumentException;
import exceptions.WineNotFoundException;
import lib.enums.Paths;
import lib.utils.Utils;


/**
 * Class that implements a catalog of wines, such as defining it as a Singleton pattern, typical of a catalog, creating directories, inserting wines into the catalog, getting wines by id
 * and some more
 */
public class WineCatalog implements Serializable {

    private static final WineCatalog INSTANCE = new WineCatalog();
    private static Map <String, Wine> wineList = new HashMap<>();
    
    private WineCatalog() {}
    
	/**
	 * Singleton for the catalog
	 * @return instance of the catalog
	 */
    public static WineCatalog getInstance(){
        return INSTANCE;
    }

	/**
	 * creates directories for the catalog
	 */
	static{
		 
        Utils.createDirectories(Paths.WINE_DIRECTORY.getPath());

        File wines = new File (Paths.WINE_DATA.getPath());

        if(!Utils.createNewFile(wines)){
            wineList = ( Map<String,Wine> ) Utils.loadFromFile(Paths.WINE_DATA.getPath());
        }

    }

	/**
	 * Creates a wine and puts it in the catalog
	 * @param wine given wine
	 */
	public static void create(Wine wine) {
		wineList.put(wine.getName(), wine);
		save();
	}

	/**
	 * Gets wine from the catalog
	 * @param wineID given wine id
	 * @return the wanted wine
	 * @throws NullArgumentException
	 * @throws WineNotFoundException
	 */
	public static Wine getWine(String wineID) throws NullArgumentException, WineNotFoundException {
		
		if(wineID == null)
			throw new NullArgumentException();
		
		if(!wineList.containsKey(wineID))
			throw new WineNotFoundException();

		return wineList.get(wineID);
	}

	/**
	 * Checks if the wine is in the catalog
	 * @param wineID given wine id
	 * @return gets wine if it's existent, null otherwise
	 */
	public static boolean exists(String wineID) {
		return wineList.get(wineID) != null;
	}


	/**
	 * Adds new stock to a wine
	 * @param wineID given wine id
	 * @param value new value of the wine
	 * @param userID given user id
	 * @param quantity new stock quantity
	 */
	public static void addStock(String wineID, double value, String userID, int quantity) {
		
		Sale sale = new Sale(userID, quantity, value);
		Wine wine = wineList.get(wineID);
		
		//verifica pelo objeto ? 
		if(wine.containsSale(userID))
			wine.setStock(quantity, value, userID);
		else
			wine.addSale(sale);
		
		save();
	}
	
	/**
	 * Saves all the changes made throughout the catalog
	 */
    public static void save(){
        Utils.saveToFile(wineList, Paths.WINE_DATA.getPath());
    }

}