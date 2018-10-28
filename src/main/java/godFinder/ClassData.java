package godFinder;

import java.io.File;
import java.util.ArrayList;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

public class ClassData {
	
	private ClassOrInterfaceDeclaration cli;
	private File file;
	private double tcc;
	
	private ArrayList<Method> publicMethods = new ArrayList<Method>();
	private ArrayList<Method> otherMethods = new ArrayList<Method>();
	private ArrayList<String> classVariables = new ArrayList<String>();
	
	public ClassData(ClassOrInterfaceDeclaration cli, File file) {
		this.cli = cli;
		this.file = file;
		
		getClassVariables();
		processMethods();
		setTcc();
		
		//System.out.println(tcc);
	}
	
	private void getClassVariables() {
		
		cli.findAll( FieldDeclaration.class).forEach( f -> {
			
			String temp = f.getVariable(0).toString().split(" +")[0];
			classVariables.add(temp);
		});
	}
	
	private void processMethods() {
		
		cli.findAll(MethodDeclaration.class).forEach(m ->{

			if( m.getDeclarationAsString().contains("public")) 
				publicMethods.add( new Method(m) );
			else 
				otherMethods.add( new Method(m));
		});
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
		
		
		return false;
	}
	
	private Boolean analyzeLocalCalls(int first, int second) {
		
		if( publicMethods.get(first).localMethodCalls.contains( publicMethods.get(second).methodName)) return true;
		else if( publicMethods.get(second).localMethodCalls.contains( publicMethods.get(first).methodName)) return true;
		else return false;
	}
	
	private double getNP() {
		
		double n = publicMethods.size();
		n = (n*(n-1))/2.0;
		
		return n;
	}
}


