import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Stack;
import java.util.stream.Collectors;

public class ParseTree {

    public String input;
    public Node root;
    private ArrayList<MatchResult> matches;
	private Object instance = null;
	private Class<?> ClassName;
	private boolean verbose = false;

    public ParseTree(String input, 	Class<?> ClassName, Object instance, boolean V) {
    	verbose = V;
    	this.instance = instance;
    	this.ClassName = ClassName;
        this.input = input;
        tokenize();
        constructTree();
        validate(root);
    }

    public Object getEvaluation() {
        return evaluate(root);
    }


    private Object evaluate(Node n) {
        if (n.children.isEmpty()) {
            if (n.isValue) {
                return n.value;
            } else {
            	try{
                return executeMethod((String) n.value, (ArrayList<Object>) n.children.stream().map(c -> c.value).collect(Collectors.toList()));
            	}catch(Throwable e){
            		ErrorArrows(n.start, (String) n.value, (ArrayList<Object>) n.children.stream().map(this::evaluate).collect(Collectors.toList()), e);
            		System.out.println("Method not found here?");
            		return null;
            	}
            }
        } else {
        	try{
            return executeMethod((String) n.value, (ArrayList<Object>) n.children.stream().map(this::evaluate).collect(Collectors.toList()));
        	}catch(NoSuchMethodException f){
        		ErrorArrows(n.start, (String) n.value, (ArrayList<Object>) n.children.stream().map(this::evaluate).collect(Collectors.toList()), f);
        		return null;
        	}catch(IllegalArgumentException d){
        		ErrorArrows(n.start, (String) n.value, (ArrayList<Object>) n.children.stream().map(this::evaluate).collect(Collectors.toList()), d);
        		return null;
        	}
        	catch(Throwable e){
        		ErrorArrows(n.start, (String) n.value, (ArrayList<Object>) n.children.stream().map(this::evaluate).collect(Collectors.toList()), e);
        		return null;
        	}
        }
    }
    
    
//Execute Method ================================================
	private Object executeMethod(String function, ArrayList<Object> parameters) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IllegalArgumentException {
		Method method;
		Class[] parameterTypes = new Class[parameters.size()];
		int i = 0;
		Class<?> temp;
		int j = parameters.size();
		while (i < j){
			temp = parameters.get(i).getClass();
			if(temp.toString().equals("class java.lang.Integer")){
				parameterTypes[i] = int.class;
				parameters.set(i, ((int) parameters.get(i)));
				//System.out.println(((Integer) parameters.get(i)).intValue()); 
			}
			else if(temp.toString().equals("class java.lang.Float")){
				parameterTypes[i] = float.class;
				parameters.set(i, (float) parameters.get(i));
			}
			else{
				parameterTypes[i] = String.class;
				parameters.set(i, (String)parameters.get(i));
			}
			i++;
		}

		for(Object o: parameters){
			System.out.println(o.getClass());
			System.out.println(o);
		}
		
		for(Class o: parameterTypes){
			System.out.println(o);
		}
		
		method = ClassName.getMethod(function, parameterTypes);
		return(method.invoke(instance, parameters));
	}
//===================================================================
	
	
    private void tokenize() {
        Pattern p = Pattern.compile("\".*\"|([\\S&&[^()]]+)|([()])");
        Matcher m = p.matcher(input);
        matches = new ArrayList<>();
        while (m.find()) {
            matches.add(m.toMatchResult());
        }
    }

    private void constructTree() {
        Stack<Node> stack = new Stack<>();
        Node t, t1, t2;
        for (MatchResult m : matches) {
            if (m.group().equals(")")) {
                ArrayList<Node> children = new ArrayList<>();
                t1 = stack.pop();
                t2 = stack.peek();
                while (!t2.value.equals("(")) {
                    children.add(t1);
                    t1 = stack.pop();
                    t2 = stack.peek();
                }
                Collections.reverse(children);
                t1.children = children;
                stack.pop();
                stack.push(t1);
            } else {
                t = new Node(m);
                stack.push(t);
            }
        }
        t = stack.peek();
        stack.pop();
        root = t;
        if (!stack.isEmpty()) {
            System.out.println("Bracket mismatch error at: " + (input.length() - 1));
        }
        parse(root);
    }

    private void parse(Node n) {
        if (n.children.isEmpty()) {
            n.parse();
        } else {
            for (Node c : n.children) {
                parse(c);
            }
        }
    }

    private void validate(Node n) {
        n.validate();
        for (Node c : n.children) {
            validate(c);
        }
    }

    public class Node {

        public Object value;
        public int start;
        public boolean isValue = false;
        private ArrayList<Node> children;

        public Node(MatchResult match) {
            value = match.group();
            start = match.start();
            children = new ArrayList<>();
        }

        public void parse() {
            String str = value.toString();
            if (str.charAt(0) == '"' && str.charAt(str.length() - 1) == '"') {
                value = str.substring(1, str.length() - 1);
                isValue = true;
            } else {
                try {
                    value = Integer.parseInt(str);
                    isValue = true;
                } catch (NumberFormatException e1) {
                    try {
                        value = Float.parseFloat(str);
                        isValue = true;
                    } catch (NumberFormatException e2) {
                        isValue = false;
                    }
                }
            }

        }

        public void validate() {
            if (!isValue && (start - 1 < 0 || input.charAt(start - 1) != '(')) {
            	
                System.out.println("Error at: " + start);
            } else if (!isValue && ((String) value).matches("(\\W)|(\\b[0-9].*\\b)")) {
                System.out.println("Error at: " + start);
            }
        }

    }
  //============================================================================
//////////////////////////////////////////////////////////////////////////////
//	//
//ErrorArrows									//
//	//
//////////////////////////////////////////////////////////////////////////////
//============================================================================
//Displays the arrows and needed error message to be printed
private void ErrorArrows(int counter, String Function, ArrayList<Object> Parameters, Throwable e){
	Class[] parameterTypes = new Class[Parameters.size()];
	int i = 0;
	Class<?> temp;
	int j = Parameters.size();
	while (i < j){
		temp = Parameters.get(i).getClass();
		if(temp.toString().equals("class java.lang.Integer")){
			parameterTypes[i] = int.class;
		}
		else if(temp.toString().equals("class java.lang.Float")){
			parameterTypes[i] = float.class;
		}
		else{
			parameterTypes[i] = String.class;
		}
		i++;
	}
		String expression = "(" + Function +" ";
		for(Class C: parameterTypes){
				expression = expression + " " + C;
		}
		System.out.println("Matching function for '" + expression + ")' not found at offset " + (counter));
		System.out.println(input);
		int d = 0;
		while(d < counter){
				System.out.print("-");
				d++;
		}
		System.out.println("^");
		if(verbose == true){
				e.printStackTrace();
		}
	}

}
