package lib;

import java.util.Map;

import lib.enums.Commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class Utils {

	public static void invokeMethod( Object obj, Map<String, Method> methodsMap, Commands command) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		if ( command.getType() != Commands.CommandType.FROM_CLIENT ||
				command.getMethodName().isEmpty() )
			System.out.print("Operation is not valid!");

		String key = command.getMethodName().get();

		if ( methodsMap.containsKey( key ) ) 
			methodsMap.get( key ).invoke( obj );
		
		else
			System.out.println("Command not found");
	}

}
