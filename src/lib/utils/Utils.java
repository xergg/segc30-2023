package lib.utils;

import java.util.Arrays;
import java.util.Map;

import lib.enums.Commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


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


	public static void invokeMethod(Object obj,  Map<String, Method> methodsMap, Commands command, String[] args, boolean isArgumentNumberChecked ) {

		if (command.getType() != Commands.CommandType.FROM_CLIENT ||
				command.getMethodName().isEmpty() )
			System.out.print("Operation is not valid!");


		String key = command.getMethodName().get();

		if ( methodsMap.containsKey( key ) ) {
			try {
				checkArgsNum( args, command.getNumberOfArguments() );

				methodsMap.get( key ).invoke( obj, Arrays.stream( args ).toArray() );
			} catch ( IllegalAccessException | IllegalArgumentException | InvocationTargetException e1 ) {
				e1.printStackTrace();
			}
		}

	}


	private static void checkArgsNum( Object[] args, int... numArgs ) {
		int argsNum = args.length;

		if ( Arrays.stream( numArgs ).noneMatch( n -> n == argsNum ) )
			System.out.println("Incorrect number of args");
	}

}
