package metrics;

import java.util.ArrayList;

import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.WhileStmt;

import godFinder.Method;

public class WMC {
	
	private ArrayList<Method> methods = new ArrayList<Method>();
	private int wmc;
	
	public int getWmc() {
		return wmc;
	}
	
	public WMC(ArrayList<Method> publicMethods, ArrayList<Method> otherMethods) {
		methods.addAll(publicMethods);
		methods.addAll(otherMethods);
		
		setWmc();
	}
	
	private void setWmc() {
		
		int counter = 0;
		
		for(Method m: methods) counter += calculateCoplexity(m);
		
		wmc = counter;
	}
	
	private int calculateCoplexity(Method method) {
		
		int complexity = 1;
		
		complexity += method.m.findAll( IfStmt.class).size();
		complexity += method.m.findAll( ForStmt.class).size();
		complexity += method.m.findAll( WhileStmt.class).size();
		complexity += method.m.findAll( DoStmt.class).size();
		complexity += method.m.findAll( ForeachStmt.class).size();
		complexity += method.m.findAll( SwitchStmt.class).size();
		
		return complexity;
	}
}
