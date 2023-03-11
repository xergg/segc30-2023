package lib;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AccountCatalog
{
    // Singleton
    private static final AccountCatalog INSTANCE = new AccountCatalog();
    private AccountCatalog() {}
    
    // Mapa dos utilizadores do sistema.
    private static Map<String, Account> accountsByClientID = new HashMap<>();


    public static AccountCatalog getInstance(){
        return INSTANCE;
    }

    public static void insert( Account account ){
        accountsByClientID.put( account.getClientID(), account );
    }

    public static Optional<Account> getAccountByClientID( String userID ){
        return Optional.ofNullable( accountsByClientID.get( userID ) );
    }

}
