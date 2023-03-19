package lib;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import lib.enums.Paths;
import lib.utils.Utils;

public class AccountCatalog implements Serializable
{
    // Singleton
    private static final AccountCatalog INSTANCE = new AccountCatalog();
    private AccountCatalog() {}
    
    // Mapa dos utilizadores do sistema.
    private static Map<String, Account> accountsByClientID = new HashMap<>();


    public static AccountCatalog getInstance(){
        return INSTANCE;
    }

    static{
        Utils.createDirectories(Paths.USER_DIRECTORY.getPath());

        File users = new File (Paths.USER_DATA.getPath());

        if(!Utils.createNewFile(users)){
            accountsByClientID = ( Map<String,Account> ) Utils.loadFromFile(Paths.USER_DATA.getPath());
        }

    }

    public static void insert( Account account ){
        accountsByClientID.put( account.getClientID(), account );
        Utils.saveToFile(accountsByClientID, Paths.USER_DATA.getPath());
    }

    public static Optional<Account> getAccountByClientID( String userID ){
		System.out.print(accountsByClientID.get( userID ).toString());

        return Optional.ofNullable( accountsByClientID.get( userID ) );
    }

	public static boolean hasEnoughMoney(String clientID, double cash) {
		return accountsByClientID.get(clientID).getBalance() - cash  > 0;
	}

	public static void transfer(String userID, String seller, double value) {
		AccountCatalog.getAccountByClientID(userID).get().setBalance(value, false);
		AccountCatalog.getAccountByClientID(seller).get().setBalance(value, true);
        Utils.saveToFile(accountsByClientID, Paths.USER_DATA.getPath());
	}
}
