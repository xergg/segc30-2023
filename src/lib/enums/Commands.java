package lib.enums;
import java.util.Optional;

public enum Commands {

	VALID_LOGIN(CommandType.STATE, "Valid login"),
	INVALID_LOGIN(CommandType.STATE, "Invalid login"),

	ADD(CommandType.FROM_CLIENT, "Add"),
	SELL(CommandType.FROM_CLIENT, "Sell"),
	VIEW(CommandType.FROM_CLIENT, "View"),
	BUY(CommandType.FROM_CLIENT, "Buy"),
	WALLET(CommandType.FROM_CLIENT, "Wallet"),
	CLASSIFY(CommandType.FROM_CLIENT, "Classify"),
	TALK(CommandType.FROM_CLIENT, "Talk"),
	READ(CommandType.FROM_CLIENT, "Read"),
	QUIT(CommandType.FROM_CLIENT, "Quit"), 
	SUCCESS(CommandType.FROM_SERVER, "Sucess"), 
	BALANCE_NOT_ENOUGH(CommandType.FROM_SERVER, "Balance is not enough"), 
	QUANTITY_NOT_ENOUGH(CommandType.FROM_SERVER, "Quantity is not enough"), 
	ERROR(CommandType.FROM_SERVER, "Error");

	public enum CommandType {
		FROM_CLIENT, // operacao pedida a partir do cliente
		STATE, // respostas de estado das operacoes
		FROM_SERVER
	}
	
	private final CommandType type;
	private final String stubMethodName;

	Commands(CommandType type, String stubMethodName) {
		this.type = type;
		this.stubMethodName = stubMethodName;
	}

	public CommandType getType() {
		return this.type;
	}

	public Optional<String> getMethodName() {
		return Optional.ofNullable( this.stubMethodName );
	}

	public static Commands valueOfType(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getNumberOfArguments() {
		// TODO Auto-generated method stub
		return 0;
	}


}

