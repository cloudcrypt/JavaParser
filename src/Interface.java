//Assignment 2 CPSC449
//Daniel Dastoor, James Gilders, Carlin Liu, Teresa Van, Thomas Vu

import java.util.Scanner;
public class Interface {
	Scanner in;
	/**
	*Constructor for class that creates a new scanner
	* @param None
	* @return None  
	*/
	Interface(){
		in = new Scanner(System.in);
	}
        /**
	*Method used for returning input from console
	* @param None
	* @return String containing Console Input
	*/
	public String GetInput(){
		return(in.nextLine());
	}
        /**
	*Method used to print out MainLoop's Menu
	* @param None
	* @return Void    
	*/	
	public void PrintMenu(){
		System.out.println("q           : Quit the program.\nv           : Toggle verbose mode (stack traces).\nf           : List all known functions.\n?           : Print this helpful text.\n<expression>: Evaluate the expression.\nExpressions can be integers, floats, strings (surrounded in double quotes) or function calls of the form '(identifier {expression}*)'.");
	}
	/**
	*Method that prints out synopsis that contains commands format line
	* @param None
	* @return Void
	*/
	public void PrintSyn(){
		System.err.print("Synopsis:\n  methods\n  methods { -h | -? | --help }+\n  methods {-v --verbose}* <jar-file> [<class-name>]\nArguments:\n  <jar-file>:   The .jar file that contains the class to load (see next line).\n  <class-name>: The fully qualified class name containing public static command methods to call. [Default=\"Commands\"]\nQualifiers:\n  -v --verbose: Print out detailed errors, warning, and tracking.\n  -h -? --help: Print out a detailed help message.\nSingle-char qualifiers may be grouped; long qualifiers may be truncated to unique prefixes and are not case sensitive.\n\nThis program interprets commands of the format '(<method> {arg}*)' on the command line, finds corresponding\nmethods in <class-name>, and executes them, printing the result to sysout. Terminate with ^D or \"exit\".");
	}
	/**
	* Method that prints out synopsis
	* @param None
	* @return Void
	*/
	public void PrintSyn2(){
		System.err.print("Synopsis:\n  methods\n  methods { -h | -? | --help }+\n  methods {-v --verbose}* <jar-file> [<class-name>]\nArguments:\n  <jar-file>:   The .jar file that contains the class to load (see next line).\n  <class-name>: The fully qualified class name containing public static command methods to call. [Default=\"Commands\"]\nQualifiers:\n  -v --verbose: Print out detailed errors, warning, and tracking.\n  -h -? --help: Print out a detailed help message.\nSingle-char qualifiers may be grouped; long qualifiers may be truncated to unique prefixes and are not case sensitive.");
	}
	/**
	* Method that prints out verbose on
	* @param None
	* @return Void   
	*/
	public void PrintVerboseOn(){
		System.out.println("Verbose on");
	}
	/**
	*Method that prints out Verbose off
	* @param None  
	* @return Void      
	*/
	public void PrintVerboseOff(){
		System.out.println("Verbose off");
	}
	/**
	* Method that prints out bye
	* @param None  
	* @return Void      
	*/
	public void PrintBye(){
		System.out.print("bye.");
	}
	/**
	*Method that dumps the threads current stack
	* @param V Boolean value containing the current verbose status
	* @return Void      
	*/
	public void PrintStack(boolean V){
		if(V == true){
			Thread.dumpStack();
		}
	}
}

