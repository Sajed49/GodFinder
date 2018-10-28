package godFinder;

import java.util.ArrayList;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MethodCallExpr;

public class Method {
	
	public String visibility;
	public NodeList<Parameter> parameters;
	public String methodName;
	public ArrayList<String> localVariables = new ArrayList<String>();
	public ArrayList<String> localMethodCalls = new ArrayList<String>();
	public ArrayList<String> localObjectMethodCalls = new ArrayList<String>();
	
	private MethodDeclaration m;
	
	
	public Method(MethodDeclaration m) {
		
		init(m);
	}
	
	
	public void init(MethodDeclaration m) {
		this.m = m;
		setVisibility();
		setMethodName();
		setParameters();
		
		findLocalFields();
		findLocalMethodCalls();
		findLocalObjectMethodCalls();
		
		//printInfo();
		//test();
	}
	
	
	public void printInfo() {
		
		System.out.println(visibility);
		System.out.println(parameters);
		System.out.println(methodName);
		System.out.println(localVariables);
		System.out.println(localMethodCalls);
		System.out.println(localObjectMethodCalls);
		System.out.println();
		
	}
	
	private void findLocalObjectMethodCalls() {
		
		m.findAll( MethodCallExpr.class ).forEach( mc -> {
			
			if( !mc.getScope().toString().equals("Optional.empty")) {
				//System.out.println(mc.getScope()+"."+mc.getNameAsString());
				localObjectMethodCalls.add( mc.getScope()+" "+mc.getNameAsString() );
			}
		});
	}
	
	private void findLocalMethodCalls() {
		
		m.findAll( MethodCallExpr.class ).forEach( mc -> {
			
			if(mc.getScope().toString().equals("Optional.empty")) 
				localMethodCalls.add(mc.getNameAsString());
		});
	}
	
	private void findLocalFields() {
		
		m.findAll( VariableDeclarator.class).forEach( v -> {
			
			localVariables.add( v.getNameAsString() );
		});
		
	}
	
	private void setParameters() {
		
		parameters = m.getParameters();
	}
	
	private void setVisibility() {
		
		if( m.getDeclarationAsString().contains("public") ) visibility = "public";
		else if( m.getDeclarationAsString().contains("private") ) visibility = "private";
		else if( m.getDeclarationAsString().contains("protected") ) visibility = "protected";
		else visibility = "package";
		
	}
	
	private void setMethodName() {
		
		methodName = m.getNameAsString();
	}
}
