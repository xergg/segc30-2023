package lib;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import exceptions.NullArgumentException;
import exceptions.WineNotFoundException;
import lib.enums.Paths;
import lib.utils.Utils;

public class WineCatalog implements Serializable {

    private static final WineCatalog INSTANCE = new WineCatalog();
    private static Map <String, Wine> wineList = new HashMap<>();
    
    private WineCatalog() {}
    
    public static WineCatalog getInstance(){
        return INSTANCE;
    }

	static{
		 
        Utils.createDirectories(Paths.WINE_DIRECTORY.getPath());

        File wines = new File (Paths.WINE_DATA.getPath());

        if(!Utils.createNewFile(wines)){
            wineList = ( Map<String,Wine> ) Utils.loadFromFile(Paths.WINE_DATA.getPath());
        }

    }

	public static void create(Wine wine) {
		wineList.put(wine.getName(), wine);
		save();
	}

	public static Wine getWine(String wineID) throws NullArgumentException, WineNotFoundException {
		
		if(wineID == null)
			throw new NullArgumentException();
		
		if(!wineList.containsKey(wineID))
			throw new WineNotFoundException();

		return wineList.get(wineID);
	}

	public static boolean exists(String wineID) {
		return wineList.get(wineID) != null;
	}

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
	
    public static void save(){
        Utils.saveToFile(wineList, Paths.WINE_DATA.getPath());
    }

}