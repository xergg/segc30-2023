package lib;

import java.util.Optional;

import javax.security.auth.login.AccountNotFoundException;

import lib.enums.Commands;

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

	public static double getBalance(String clientID) {
		return AccountCatalog.getAccountByClientID(clientID).get().getBalance();
	}

	public static void talk(String clientID, String message){

		AccountCatalog.getAccountByClientID(clientID).get().receiveMessage(message);

	}

	public static String read(String clientID){

		return AccountCatalog.getAccountByClientID(clientID).get().readMessages();

	}
}
