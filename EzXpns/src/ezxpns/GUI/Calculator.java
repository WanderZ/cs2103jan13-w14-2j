/**
 * 
 */
package ezxpns.GUI;

import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * A singleton class to evaluate equations using javascript
 * @author A0099621X
 */
public class Calculator {
	@SuppressWarnings("serial")
	static public class EvaluationException extends Exception{
		public EvaluationException(){
			super();
		}
	}
	static private Calculator instance = new Calculator();
	static public Calculator getInstance(){
		return instance;
	}
	
	private final ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
	static public final Pattern INVALID_EQUATION = Pattern.compile("[^0-9-+ \t*/().]");
	
	private Calculator(){
		// this is a singleton man
	}
	
	/**
	 * Evaluate an equation that's in a string, throws exception if the equation is invalid
	 */
	public double evaluate(String equation) throws EvaluationException{
		if(INVALID_EQUATION.matcher(equation).find()){
			throw new EvaluationException();
		}
		try {
			return (Double) engine.eval(equation);
		} catch (ScriptException e) {
			throw new EvaluationException();
		}
	}
}
