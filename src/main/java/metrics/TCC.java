package metrics;

import java.util.ArrayList;
import java.util.List;

import godFinder.Method;

public class TCC {
	
	private double tcc;
	private ArrayList<Method> publicMethods = new ArrayList<Method>();
	private ArrayList<String> classVariables = new ArrayList<String>();
	
	public TCC(ArrayList<Method> publicMethods, ArrayList<String> classVariables) {

		this.publicMethods = publicMethods;
		this.classVariables = classVariables;
		
		setTcc();
	}
	
	public double getTcc() {
		return tcc;
	}

	private void setTcc() {

		if( publicMethods.size() < 2 ) tcc = 0.0;
		else {
			//System.out.println("u");
			tcc = getNDC() / getNP();
		}
	}
	
	private double getNDC() {
		
		int positive = 0;
		
		int len = publicMethods.size();
		
		for(int i=0; i<len; i++) {
			for(int j=i+1; j<len; j++) {
				
				if( pairResult(i, j) == true ) positive++;
			}
		}
		
		return (double) positive;
	}
	
	private Boolean pairResult(int first, int second) {
		
		if( analyzeLocalCalls(first, second) == true) return true;

		if( analyzeOtherMethods(first, second) == true ) return true;
		
		if( analyzeGlobalVariables(first, second) == true ) return true;
		
		return false;
	}
	
	
	
	private Boolean analyzeLocalCalls(int first, int second) {
		
		if( publicMethods.get(first).localMethodCalls.contains( publicMethods.get(second).methodName)) return true;
		else if( publicMethods.get(second).localMethodCalls.contains( publicMethods.get(first).methodName)) return true;
		else return false;
	}
	
	private Boolean analyzeOtherMethods(int first, int second) {
		
		
		List<String> one = new ArrayList<String>();
		List<String> two = new ArrayList<String>();
		
		one.addAll( publicMethods.get(first).localMethodCalls );
		two.addAll( publicMethods.get(second).localMethodCalls );
		
		one.retainAll(two);
		
		if( one.size() > 0 ) return true;
		else return false;
	}
	
	private Boolean analyzeGlobalVariables(int first, int second) {
		
		List<String> common = new ArrayList<String>();
		
		common.addAll( publicMethods.get(first).assignments );
		common.retainAll(  publicMethods.get(second).assignments );
		common.retainAll(  classVariables );
		
		
		if( common.size() > 0 ) return true;
		else return false;
	}
	
	private double getNP() {
		
		double n = publicMethods.size();
		n = (n*(n-1))/2.0;
		
		return n;
	}
}
