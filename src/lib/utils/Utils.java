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
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Utils {

	public static void invokeMethod(Object obj, Map<String, Method> methodsMap, Commands command) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		if ( command.getType() != Commands.CommandType.FROM_CLIENT ||
				command.getMethodName().isEmpty() )
			System.out.print("Operation is not valid!");

		String key = command.getMethodName().get();
		
		if ( methodsMap.containsKey( key ) ) 
			methodsMap.get( key ).invoke( obj );

		else
			System.out.println("Command not found");
	}


	public static void invokeMethod(Object obj,  Map<String, Method> methodsMap, Commands command, String[] args, boolean isArgumentNumberChecked )
			throws IncorrectNumberOfArgumentsException {
		if (command.getType() != Commands.CommandType.FROM_CLIENT ||
				command.getMethodName().isEmpty() )
			System.out.print("Operation is not valid!");

		String key = command.getMethodName().get().toLowerCase();
		
		if ( methodsMap.containsKey( key ) ) {
			try {
				checkArgsNum(args, command.getNumberOfArguments());
				methodsMap.get( key ).invoke( obj, Arrays.stream( args ).toArray() );
			} catch ( IllegalAccessException | IllegalArgumentException | InvocationTargetException e1 ) {
				e1.printStackTrace();
			}
		}

	}


	private static void checkArgsNum( Object[] args, int... numArgs ) throws IncorrectNumberOfArgumentsException {
		int argsNum = args.length;
		
		if ( Arrays.stream( numArgs ).noneMatch( n -> n == argsNum ) )
            throw new IncorrectNumberOfArgumentsException( Arrays.toString( numArgs ));
	}


	//Utils for files 

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


	public static byte[] receiveFile( ObjectInput inStream ) throws IOException
	{
		int fileSize = inStream.readInt();
		byte[] buffer = new byte[fileSize];

		inStream.readFully( buffer, 0, fileSize );

		return buffer;
	}

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

}
