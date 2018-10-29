package fileAnalyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import io.CustomFileWriter;
import io.DirectoryExplorer;


public class RawData {
	
	private final String directoryRoot;
	private ArrayList<String> filePaths = new ArrayList<>();
	
	private ArrayList<String> header = new ArrayList<>( 
			Arrays.asList( "Class Name", "Access To Foreign Data", "Weighted Method Complexity",
					"Tight Class Cohession", "File Path"));
	private ArrayList< ArrayList<String> > result = new ArrayList< ArrayList<String> >();
	
	
	public RawData(String directoryRoot, String resultDirectory) throws FileNotFoundException{
		
		this.directoryRoot = directoryRoot;
		result.add(header);
		
		getFilePaths( new File(this.directoryRoot) ); //get .java files
		getClassPaths(); //get classes inside .java files
		
		outputRawData(resultDirectory);
		
	}
	
	private void outputRawData(String resultDirectory) {
		
		File file = new File(resultDirectory+"raw.csv");
		CustomFileWriter.writeAFile( file, result);
	}
	
	private void getClassPaths() throws FileNotFoundException {
		for(String file : filePaths) {
			//System.out.println(file);
			CompilationUnit cu = JavaParser.parse(new File(file));
			cu.findAll(ClassOrInterfaceDeclaration.class).forEach(cli -> {
				
				if( cli.isInterface() == false ) {
					//System.out.println(file);
					makeClassDataAndAddResult( cli, file );
				}
				
			});
		}
		
		
	}
	
	private void makeClassDataAndAddResult( ClassOrInterfaceDeclaration cli, String path) {
		
		ClassData cd = new ClassData(cli);
		
		addResult( cli.getNameAsString(), path, cd);
	}
	
	private void addResult(String name, String path, ClassData cd) {
		
		ArrayList<String> temp = new ArrayList<String>();
		
		temp.add( name );
		
		temp.add( Integer.toString( cd.atfd.getAtfd() ) );
		temp.add( Integer.toString( cd.wmc.getWmc() ) );
		temp.add( Double.toString( Math.round( cd.tcc.getTcc()*100)/100D ) );
		
		temp.add(path);
		
		
		result.add(temp);
	}
	
	private void getFilePaths( File projectDir ) {
		
		new DirectoryExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {

			filePaths.add(projectDir.getAbsoluteFile()+ path);
            
        }).explore(projectDir);
	}
	
}
