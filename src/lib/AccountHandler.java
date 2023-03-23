package lib;

import java.util.Optional;

import javax.security.auth.login.AccountNotFoundException;

import lib.utils.Utils;


/**
 * Class that creates an account Handler, used to, as the name says, handle most operations within an account.
 */
public class AccountHandler {

    private AccountHandler() { }

	/**
	 * adds an account to the catalog given a clientid
	 * @param clientID given id
	 */
    public static void addAccount( String clientID ) {
    	
    	if(clientID != null)
    		AccountCatalog.insert( new Account( clientID ) );
    }


	/**
	 * checks the catalog to see if the account given an id is valid
	 * @param userID id to be checked
	 * @throws AccountNotFoundException
	 */
	public static void checkValid(String userID) throws AccountNotFoundException {

		if(userID != null) {
			Optional<Account> account = AccountCatalog.getAccountByClientID( userID );

	     	if ( account.isEmpty() ) throw new AccountNotFoundException();
		}
		
		return;
	}


	/**
	 * given an id checks the balance from the account
	 * @param clientID given id
	 * @return the balance from the account
	 */
	public static double getBalance(String clientID) {
		return AccountCatalog.getAccountByClientID(clientID).get().getBalance();
	}

	/**
	 * Sends a message to the account specificed
	 * @param clientID user receiving the message
	 * @param message message to be sent
	 */
	public static void talk(String clientID, String message){

		AccountCatalog.getAccountByClientID(clientID).get().receiveMessage(message);
		AccountCatalog.save();
	}


	/**
	 * Reads all unread messages
	 * @param clientID id from the account which messages are to be read from
	 * @return the messages to be read
	 */
	public static String read(String clientID){

		String message = AccountCatalog.getAccountByClientID(clientID).get().readMessages();
		AccountCatalog.save();
		return message;
	}


}
