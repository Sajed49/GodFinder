package godFinder;
import java.util.ArrayList;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import metrics.ATFD;
import metrics.TCC;
import metrics.WMC;

public class ClassData {
	
	private ClassOrInterfaceDeclaration cli;
	
	private ArrayList<Method> publicMethods = new ArrayList<Method>();
	private ArrayList<Method> otherMethods = new ArrayList<Method>();
	private ArrayList<String> classVariables = new ArrayList<String>();
	
	TCC tcc;
	WMC wmc;
	ATFD atfd;
	
	public ClassData(ClassOrInterfaceDeclaration cli) {
		this.cli = cli;
		
		getClassVariables();
		processMethods();
		
		getMetrics();
		
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
	
	private void getMetrics() {
		tcc = new TCC(publicMethods, classVariables);
		wmc = new WMC(publicMethods, otherMethods);
		atfd = new ATFD(publicMethods, otherMethods);
	}
}


