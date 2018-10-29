package metrics;

import java.util.ArrayList;

import com.github.javaparser.ast.expr.FieldAccessExpr;

import fileAnalyzer.Method;

public class ATFD {
	
	private ArrayList<Method> methods = new ArrayList<Method>();
	private int atfd;
	
	
	public int getAtfd() {
		return atfd;
	}
	
	public ATFD(ArrayList<Method> publicMethods, ArrayList<Method> otherMethods) {
		
		methods.addAll(publicMethods);
		methods.addAll(otherMethods);
		
		setAtfd();
	}
	
	private void setAtfd() {
		
		int totalForeignAccess = 0;
		
		for( Method m: methods) totalForeignAccess += calculateMethodATFD(m);
		
		
		atfd = totalForeignAccess;
		
	}
	
	private int calculateMethodATFD( Method method) {
		
		int counter = 0;

		//number of external object field access directly
		counter += method.m.findAll( FieldAccessExpr.class ).size();
		
		//remove this.field
		for(FieldAccessExpr f:  method.m.findAll( FieldAccessExpr.class ) ) {
			if( f.getScope().toString().equals("this") ) counter--;
		}
		
		//number of getters and setters
		for( String s: method.localObjectMethodCalls) {
			if( s.split(" +")[1].startsWith("get") || s.split(" +")[1].startsWith("set") ) {
				counter++;
			}
		}
		
		return counter;
	}
	
}
