/**
 * 
 */
package ezxpns.GUI;

import java.util.List;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * A singleton class to evaluate equations using javascript
 * yeah man javascript yo yo yo
 * @author yyjhao
 *
 */
public class Calculator {
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
	static public final Pattern p = Pattern.compile("[^0-9-+ \t*/()]");
	
	private Calculator(){
		// this is a singleton man
	}
	
	public double evaluate(String equation) throws EvaluationException{
		if(p.matcher(equation).find()){
			throw new EvaluationException();
		}
		try {
			return (Double) engine.eval(equation);
		} catch (ScriptException e) {
			throw new EvaluationException();
		}
	}
}
