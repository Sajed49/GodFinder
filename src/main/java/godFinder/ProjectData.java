package godFinder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;


public class ProjectData {
	
	private final String directoryRoot;
	private ArrayList<String> filePaths = new ArrayList<>();
	public ArrayList<String> classNames = new ArrayList<>();
	public ArrayList<String> interfaceNames = new ArrayList<>();
	
	
	public ProjectData(String directoryRoot) throws FileNotFoundException{
		
		this.directoryRoot = directoryRoot;
		
		getFilePaths( new File(this.directoryRoot) ); //get .java files
		getClassPaths(); //get classes inside .java files
	}
	
	
	
	private void getClassPaths() throws FileNotFoundException {
		for(String file : filePaths) {
			//System.out.println(file);
			CompilationUnit cu = JavaParser.parse(new File(file));
			cu.findAll(ClassOrInterfaceDeclaration.class).forEach(cli -> {
				
				if( cli.isInterface() == false ) {
					//System.out.println(file);
					makeClassData( cli );
				}
				else interfaceNames.add(cli.getNameAsString());
			});
		}
		
		
	}
	
	private void makeClassData( ClassOrInterfaceDeclaration cli) {
		
		classNames.add(cli.getNameAsString());
		
		new ClassData(cli);
	}
	
	private void getFilePaths( File projectDir ) {
		
		new DirectoryExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {

			filePaths.add(projectDir.getAbsoluteFile()+ path);
            
        }).explore(projectDir);
	}
	
}
