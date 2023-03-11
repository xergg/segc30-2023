package lib;

public class AccountHandler {

    private AccountHandler() { }

    public static void addAccount( String clientID ) {
        AccountCatalog.insert( new Account( clientID ) );
    }

	public static boolean checkValid(String userID) {
		// TODO Auto-generated method stub
		return false;
	}

	public static boolean hasEnoughMoney(double cash) {
		// TODO Auto-generated method stub
		return false;
	}

	public static double getBalance() {
		// TODO Auto-generated method stub
		return 0;
	}

}
