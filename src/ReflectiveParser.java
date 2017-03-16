import java.lang.reflect.*;

public class ReflectiveParser {

	public static Boolean Verbose = false;
	public static Boolean Run = true;
	public static String MenuC;
	public static Interface MainI = new Interface();
	public static Parser P;
	
    public static void main(String[] args) {
        
		ConsoleParse cp = new ConsoleParse();

		String jarString = "";
		String functionClassName = "";
		
		if(args.length == 0)
		{
			System.out.println("len 0");
			Verbose = cp.ParseProgramInput0(args, Verbose);
			System.exit(0);
		}
		
		else if(args.length == 1)
		{
			System.out.println("len 1");
			Verbose = cp.ParseProgramInput1(args, Verbose);
			System.exit(0);
		}
		
		else if(args.length == 2)
		{
			System.out.println("len 2");
			if(args[0].charAt(0) == '-' && args[1].endsWith(".jar"))
			{
				System.out.println("default class");
				Verbose = cp.ParseProgramInput(args, Verbose);
				try
				{
					jarString = args[1];
					functionClassName = "Commands";
					P = new Parser(jarString, functionClassName, Verbose);
				}
				catch(ClassNotFoundException e)
				{
					System.out.println("Could not find class: " + functionClassName);
					P = new Parser();
					System.exit(0);
				}
				catch(Exception f)
				{
				}
			}
			
			else
			{
				try
				{
					jarString = args[0];
					functionClassName = args[1];
					P = new Parser(jarString, functionClassName, Verbose);
				}
				catch(ClassNotFoundException e)
				{
					System.out.println("Could not find class: " + functionClassName);
					P = new Parser();
					System.exit(0);
				}
				catch(Exception f)
				{
				}
			}
			mainLoop();
		}
		else if(args.length == 3)
		{
			System.out.println("len 3");
			Verbose = cp.ParseProgramInput(args, Verbose);
			
			try
			{
				if(args[1].endsWith(".jar"))
				{
					jarString = args[1];
				}
				else
				{
					System.out.println("This program requires a jar file as the first command line argument (after any qualifiers)");
					System.exit(-3);
				}
				functionClassName = args[2];
				P = new Parser(jarString, functionClassName, Verbose);
			}
			catch(Exception e)
			{
				System.out.println("asdasdasd");
				P = new Parser();
				System.exit(0);
			}
			
			mainLoop();
		}
		else
		{
			System.out.println("This program takes at most two command line arguments");
			System.exit(-2);
		}
		
        JarExecutor jarExec = new JarExecutor("commands.jar", "Commands");
        try {
            ParseTree p = new ParseTree("(add 100 (mul 10 (len \"Hello world!\"))", jarExec);
            p.getEvaluation();
        } catch (ParseException e) {
            e.printErrorMessage();
        }
    }

	public static void mainLoop()
	{
		while(Run == true)
		{
			System.out.print("> ");
			MenuC = MainI.GetInput();
			switch(MenuC)
			{
				case "q":
					Run = false;
					MainI.PrintBye();
					break;

				case "v":
					if(Verbose == false)
					{
						Verbose = true;
						MainI.PrintVerboseOn();
						P.SetVerbose(true);
					}
					else
					{
						MainI.PrintVerboseOff();
						Verbose = false;
						P.SetVerbose(false);
					}
					break;

				case "f":
					P.PrintFunctions();
					break;

				case "?":
					MainI.PrintMenu();
					break;

				default:
					P.ParseExpression(MenuC);
			}
		}
	}
}
