package lib.enums;
import java.util.Optional;

/**
 * Enum that has all the commands used throughout the assignment for each command the client types and the server processes.
 */
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


	/**
	 * Another enum that diferentiates between the client and server, as well as gives the result of each given operation.
	 */
	public enum CommandType {
		FROM_CLIENT, // operacao pedida a partir do cliente
		STATE, // respostas de estado das operacoes
		FROM_SERVER
	}

	private final CommandType type;
	private final String stubMethodName;
    private final int numberOfArguments;
    
	/**
	 * Defining a command
	 * @param type type of the command
	 */
    Commands( CommandType type )
    {
        this.type = type;
        this.numberOfArguments = 0;
        this.stubMethodName = null;
    }

	/**
	 * Defining a command
	 * @param type type of the command
	 * @param stubMethodName given command method name
	 */
	Commands(CommandType type, String stubMethodName) {
		this.type = type;
		this.stubMethodName = stubMethodName;
		this.numberOfArguments = 0;
	}

	/**
	 * Defining a command with method name and a number of arguments
	 * @param type type of the command
	 * @param stubMethodName given method name
	 * @param numberOfArguments number of arguments
	 */
    Commands( CommandType type, String stubMethodName, int numberOfArguments )
    {
        this.type = type;
        this.numberOfArguments = numberOfArguments;
        this.stubMethodName = stubMethodName;
    }

	/**
	 * Gets the command's type
	 * @return returns command's type
	 */
	public CommandType getType() {
		return this.type;
	}

	/**
	 * Gets method's name in case there's one
	 * @return method's name if existent
	 */
	public Optional<String> getMethodName() {
		return Optional.ofNullable( this.stubMethodName );
	}

	/**
	 * Gets a value of a command
	 * @param op command to be checked
	 * @return command found, not found if otherwise
	 */
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


	/**
	 * Gets number of arguments
	 * @return number of arguments
	 */
	public int getNumberOfArguments() {
		return this.numberOfArguments;
	}

}

