package lib;

import java.util.Optional;

import javax.security.auth.login.AccountNotFoundException;

public class AccountHandler {

    private AccountHandler() { }

    public static void addAccount( String clientID ) {
    	
    	if(clientID != null)
    		AccountCatalog.insert( new Account( clientID ) );
    }

	public static void checkValid(String userID) throws AccountNotFoundException {
		if(userID != null) {
			
			Optional<Account> account = AccountCatalog.getAccountByClientID( userID );

	     	if ( account.isEmpty() ) throw new AccountNotFoundException();
		}
	}

	public static boolean hasEnoughMoney(String clientID, double cash) {
		return AccountCatalog.getAccountByClientID(clientID).get().getBalance() - cash  > 0;
	}

	public static double getBalance(String clientID) {
		return AccountCatalog.getAccountByClientID(clientID).get().getBalance();
	}

	public static void buy(String clientID, String seller, double cash) {
		AccountCatalog.getAccountByClientID(clientID).get().setBalance(cash, false);
		AccountCatalog.getAccountByClientID(seller).get().setBalance(cash, true);
	}

}
