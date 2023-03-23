package lib.utils;

import java.util.Arrays;
import java.util.Map;

import exceptions.IncorrectNumberOfArgumentsException;

import lib.enums.Commands;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Class used throughout most classes, mainly the client and server side, with tools to invoke methods chosen by the client and processed through the server, such as invoking the methods.
 */
public class Utils {

	/**
	 * Invokes the method from a map of methods given a command.
	 * @param obj the method being invoked
	 * @param methodsMap the map which the methods are stored in
	 * @param command the method invoked from the client
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static void invokeMethod(Object obj, Map<String, Method> methodsMap, Commands command) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		if ( command.getType() != Commands.CommandType.FROM_CLIENT ||
				command.getMethodName().isEmpty() )
			System.out.print("Operation is not valid!");
		
		String key = command.getMethodName().get().toLowerCase();

		if ( methodsMap.containsKey( key ) ) 
			methodsMap.get( key ).invoke( obj );
		else
			System.out.println("Command not found");
	}

	/**
	 * Invokes the method from a map of methods given a command.
	 * @param obj the method being invoked
	 * @param methodsMap the map which the methods are stored in
	 * @param command the method invoked from the client
	 * @param args array of arguments passed
	 * @param isArgumentNumberChecked flag to check if all number of arguments have been fulfilled
	 * @throws IncorrectNumberOfArgumentsException
	 */
	public static void invokeMethod(Object obj,  Map<String, Method> methodsMap, Commands command, String[] args, boolean isArgumentNumberChecked )
			throws IncorrectNumberOfArgumentsException {
		if (command.getType() != Commands.CommandType.FROM_CLIENT ||
				command.getMethodName().isEmpty() )
			System.out.print("Operation is not valid!");
		
		String key = command.getMethodName().get().toLowerCase();

		if ( methodsMap.containsKey( key ) ) {
			try {
				String [] s = args;
				
				if(command.toString().equals("TALK")) {
					s = new String[2];
					
					StringBuilder sb = new StringBuilder();
					for(int i = 1; i< args.length; i++)
						sb.append(args[i] + " ");
					sb.deleteCharAt(sb.length()-1);
					sb.append("\n");
					s[0] = args[0];
					s[1] = sb.toString();
				}
				
				checkArgsNum(s, command.getNumberOfArguments());
				methodsMap.get( key ).invoke( obj, Arrays.stream( s ).toArray() );
			} catch ( IllegalAccessException | IllegalArgumentException | InvocationTargetException e1 ) {
				e1.printStackTrace();
			}
		}

	}


	/**
	 * Checks the number of arguments
	 * @param args array of arguments being checked
	 * @param numArgs the length to be compared to
	 * @throws IncorrectNumberOfArgumentsException
	 */
	private static void checkArgsNum( Object[] args, int... numArgs ) throws IncorrectNumberOfArgumentsException {
		int argsNum = args.length;

		if ( Arrays.stream( numArgs ).noneMatch( n -> n == argsNum ) )
			throw new IncorrectNumberOfArgumentsException( Arrays.toString( numArgs ));
	}


	//Utils for files 

	/**
	 * Sends a file through a stream
	 * @param outStream the output stream to send
	 * @param file file to be sent
	 * @throws IOException
	 */
	public static void sendFile( ObjectOutput outStream, File file ) throws IOException
	{
		FileInputStream fileInStream = new FileInputStream( file );
		InputStream inStream = new BufferedInputStream( fileInStream );

		// pomos o conteudo do ficheiro num buffer para enviarmos ao servidor
		byte[] buffer = new byte[(int)file.length()];
		int bytesRead = inStream.read( buffer );

		// enviamos o tamanho do ficheiro e ficheiro
		outStream.writeInt( bytesRead );
		outStream.write( buffer, 0, bytesRead );
		outStream.flush();

		inStream.close();
		fileInStream.close();
	}


	/**
	 * Receives a file through a stream
	 * @param inStream the input stream tor receive
	 * @return file size
	 * @throws IOException
	 */
	public static byte[] receiveFile( ObjectInput inStream ) throws IOException
	{
		int fileSize = inStream.readInt();
		byte[] buffer = new byte[fileSize];

		inStream.readFully( buffer, 0, fileSize );

		return buffer;
	}


	/**
	 * Creates a file given a buffer and a path
	 * @param buffer given buffer
	 * @param filePath path file
	 * @throws IOException
	 */
	public static void createFile( byte[] buffer, String filePath ) throws IOException
	{

		File fileReceived = new File( filePath );

		try ( FileOutputStream fileOutStream = new FileOutputStream( fileReceived );
				OutputStream output = new BufferedOutputStream( fileOutStream ) )
		{
			output.write( buffer );
		}
		catch ( FileNotFoundException e )
		{
			e.printStackTrace();
		}
	}

	/**
	 * Creates a directory given a path
	 * @param path given path
	 */
	public static void createDirectories(String path) {
		try
		{
			Files.createDirectories( Paths.get(path) );
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
	}


	/**
	 * Saves a given object to a file
	 * @param obj given object
	 * @param file given file
	 */
	public static void saveToFile(Object obj , String file){
		try (FileOutputStream fos = new FileOutputStream(file);
				ObjectOutputStream oos = new ObjectOutputStream(fos)){
			oos.writeObject(obj);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Loads an object/ objects given a file
	 * @param file given file
	 * @return the object file
	 */
	public static Object loadFromFile(String file){

		try ( FileInputStream fis = new FileInputStream( file );
				ObjectInputStream ois = new ObjectInputStream( fis ) )
		{
			return ois.readObject();
		}
		catch ( ClassNotFoundException e1 )
		{
			e1.printStackTrace();
		}
		catch ( IOException e2 )
		{
			e2.printStackTrace();
		}

		return null;

	}


	/**
	 * Creates a new file given a file
	 * @param file given file
	 * @return a new file if the file is existent, false otherwise
	 */
	public static boolean createNewFile(File file) {

		try {
			return file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;

	}

}
