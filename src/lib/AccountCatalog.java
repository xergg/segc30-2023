package lib;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import lib.enums.Paths;
import lib.utils.Utils;


/**
 * Class that implements a catalog of accounts, such as defining it as a Singleton pattern, typical of a catalog, creating directories, inserting accounts into the catalog, getting accounts by id
 * and some more
 */
public class AccountCatalog implements Serializable
{
    // Singleton
    private static final AccountCatalog INSTANCE = new AccountCatalog();
    private AccountCatalog() {}
    
    // Mapa dos utilizadores do sistema.
    private static Map<String, Account> accountsByClientID = new HashMap<>();

    /**
     * Singleton from the catalog
     * @return the catalog instance
     */
    public static AccountCatalog getInstance(){
        return INSTANCE;
    }

    /**
     * creates the directory for the account catalog
     */
    static{
        Utils.createDirectories(Paths.USER_DIRECTORY.getPath());

        File users = new File (Paths.USER_DATA.getPath());

        if(!Utils.createNewFile(users)){
            accountsByClientID = ( Map<String,Account> ) Utils.loadFromFile(Paths.USER_DATA.getPath());
        }

    }

    /**
     * inserts account into the catalog
     * @param account account to be inserted into the catalog
     */
    public static void insert( Account account ){
        accountsByClientID.put( account.getClientID(), account );
        save();
    }


    /**
     * Gets an account from the catalog given an id
     * @param userID given userid
     * @return returns a user if the userID's in the catalog, empty otherwise
     */
    public static Optional<Account> getAccountByClientID( String userID ){
        return Optional.ofNullable( accountsByClientID.get( userID ) );
    }

    /**
     * Checks if the client has enough money
     * @param clientID given clientid
     * @param cash given cash to be checked
     * @return true if the value is bigger than 0, false otherwise
     */
	public static boolean hasEnoughMoney(String clientID, double cash) {
		return accountsByClientID.get(clientID).getBalance() - cash  > 0;
	}

    /**
     * Transfers money between a buyer and a seller
     * @param userID buyer id 
     * @param seller seller id
     * @param value money to be transfered
     */
	public static void transfer(String userID, String seller, double value) {
		AccountCatalog.getAccountByClientID(userID).get().setBalance(value, false);
		AccountCatalog.getAccountByClientID(seller).get().setBalance(value, true);
        save();
	}


    /**
     * Saves the catalog throughout all its changes.
     */
    public static void save(){
        Utils.saveToFile(accountsByClientID,Paths.USER_DATA.getPath());
    }
}
