package lib.enums;
import java.util.Optional;

public enum Commands {

	VALID_LOGIN(CommandType.STATE, "Valid login"),
	INVALID_LOGIN(CommandType.STATE, "Invalid login"),

	ADD(CommandType.FROM_CLIENT, "Add", 2),
	SELL(CommandType.FROM_CLIENT, "Sell", 3),
	VIEW(CommandType.FROM_CLIENT, "View", 1),
	BUY(CommandType.FROM_CLIENT, "Buy", 3),
	WALLET(CommandType.FROM_CLIENT, "Wallet"),
	CLASSIFY(CommandType.FROM_CLIENT, "Classify", 2),
	TALK(CommandType.FROM_CLIENT, "Talk", 2),
	READ(CommandType.FROM_CLIENT, "Read"),
	QUIT(CommandType.FROM_CLIENT, "Quit"), 
	SUCCESS(CommandType.FROM_SERVER, "Sucess"), 
	BALANCE_NOT_ENOUGH(CommandType.FROM_SERVER, "Balance is not enough"), 
	QUANTITY_NOT_ENOUGH(CommandType.FROM_SERVER, "Quantity is not enough"),
	MESSAGE_SENT(CommandType.FROM_SERVER, "The message was sent!"), 
	ERROR(CommandType.FROM_SERVER, "Error"), 
	NOT_FOUND(CommandType.STATE, "Not found");

	public enum CommandType {
		FROM_CLIENT, // operacao pedida a partir do cliente
		STATE, // respostas de estado das operacoes
		FROM_SERVER
	}

	private final CommandType type;
	private final String stubMethodName;
    private final int numberOfArguments;
    
    Commands( CommandType type )
    {
        this.type = type;
        this.numberOfArguments = 0;
        this.stubMethodName = null;
    }

	Commands(CommandType type, String stubMethodName) {
		this.type = type;
		this.stubMethodName = stubMethodName;
		this.numberOfArguments = 0;
	}

    Commands( CommandType type, String stubMethodName, int numberOfArguments )
    {
        this.type = type;
        this.numberOfArguments = numberOfArguments;
        this.stubMethodName = stubMethodName;
    }

	public CommandType getType() {
		return this.type;
	}

	public Optional<String> getMethodName() {
		return Optional.ofNullable( this.stubMethodName );
	}

	public static Commands valueOfType(String op) {
		if ( op == null )
			throw new IllegalArgumentException( "Argumento nao pode ser nulo." );

		String typeUpperCase = op.toUpperCase();

		for ( Commands command : Commands.values() )
		{
			if ( typeUpperCase.equals( command.name() ) )
				return command;

			if ( command.type == CommandType.FROM_CLIENT &&
					typeUpperCase.length() == 1 &&
					typeUpperCase.charAt( 0 ) == command.name().charAt( 0 ) )
				return command;
		}

		return Commands.NOT_FOUND;
	}

	public int getNumberOfArguments() {
		return this.numberOfArguments;
	}

}

