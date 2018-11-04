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
	
	private final File directoryRoot;
	private ArrayList<File> files = new ArrayList<File>();
	
	
	private ArrayList<String> header = new ArrayList<String>( 
			Arrays.asList( "File Path", "Class Name",  "Access To Foreign Data", "Weighted Method Complexity",
					"Tight Class Cohession"));
	private ArrayList< ArrayList<String> > result = new ArrayList< ArrayList<String> >();
	
	
	public RawData(File directoryRoot, String resultDirectory) throws FileNotFoundException{
		
		this.directoryRoot = directoryRoot;
		result.add(header);
		
		getFilePaths( directoryRoot ); //get .java files
		
		getClassPaths(); //get classes inside .java files
		
		outputRawData(resultDirectory);
		
	}
	
	
	private void outputRawData(String resultDirectory) {
		
		File file = new File(resultDirectory+directoryRoot.getName()+".csv");
		CustomFileWriter.writeAFile( file, result);
	}
	
	private void getClassPaths() throws FileNotFoundException {
		
		for(File file : files) {
			//System.out.println(file);
			CompilationUnit cu = JavaParser.parse( file );
			cu.findAll(ClassOrInterfaceDeclaration.class).forEach(cli -> {
				
				if( cli.isInterface() == false ) {
					//System.out.println(file);
					makeClassDataAndAddResult( cli, file );
				}
				
			});
		}
		
		
	}
	
	private void makeClassDataAndAddResult( ClassOrInterfaceDeclaration cli, File file) {
		
		ClassData cd = new ClassData(cli);
		
		addResult( cli.getNameAsString(), file.getPath(), cd);
	}
	
	private void addResult(String name, String path, ClassData cd) {
		
		ArrayList<String> temp = new ArrayList<String>();
		
		String localPath = path.substring( directoryRoot.getPath().length());
		temp.add(localPath);
		
		temp.add( name );
		
		temp.add( Integer.toString( cd.atfd.getAtfd() ) );
		temp.add( Integer.toString( cd.wmc.getWmc() ) );
		temp.add( Double.toString( cd.tcc.getTcc() ) );
		
		
		
		
		result.add(temp);
	}
	
	private void getFilePaths( File projectDir ) {
		
		new DirectoryExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {

			files.add( new File(projectDir.getAbsoluteFile()+ path) );
			
        }).explore(projectDir);
	}
	
}
