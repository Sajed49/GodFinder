package godFinder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;

public class Method {
	
	public String visibility;
	public NodeList<Parameter> parameters;
	public String methodName;
	public ArrayList<String> localMethodCalls = new ArrayList<String>();
	public ArrayList<String> localObjectMethodCalls = new ArrayList<String>();
	public Set<String> assignments = new HashSet<String>();
	
	public MethodDeclaration m;
	
	
	public Method(MethodDeclaration m) {
		
		init(m);
	}
	
	
	public void init(MethodDeclaration m) {
		
		this.m = m;
		setVisibility(); //tcc
		setMethodName();
		setParameters();
		
		findLocalMethodCalls(); //tcc
		findLocalObjectMethodCalls(); //aftd
		findAssignments();  //tcc
		
		//printInfo();
	}
	
	
	public void printInfo() {
		
		System.out.println(visibility);
		System.out.println(parameters);
		System.out.println(methodName);
		System.out.println(localMethodCalls);
		System.out.println(localObjectMethodCalls);
		System.out.println(assignments);
		System.out.println();
		
	}
	
	private void findAssignments( ) {
		
		m.findAll( AssignExpr.class ).forEach( mc -> {
			
			String beforeAssignmentOperator = mc.toString().split(" +")[0];
			assignments.add(beforeAssignmentOperator);
			
			String temp[] = mc.getValue().toString().split(" +");
			for( String s: temp ) {
				
				if( Character.isAlphabetic( s.charAt(0)) ) assignments.add(s);
			}
		});
		
		
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
