package godFinder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import fileAnalyzer.RawData;

public class App 
{	
	
	private String rootDirectory = "D:\\dataset\\Spring-boot";
	private String resultDirectory = "Result\\RawData\\";
	private ArrayList<File> versionFolders = new ArrayList<File>();
	
    public static void main( String[] args )
    {	
    	new App();
        
    	
    }
    
    public App() {
    	
    	//while( rootDirectory.equals("") ) selectRootDirectory();
    	
    	getVersionFolders();
    	createResultDirectory();
    	
    	makeRawData();
	}
    
    
    private void getVersionFolders() {
    	
    	File root = new File(rootDirectory);
    	
    	for( File version: root.listFiles() ) {
    		
    		if( version.isDirectory() ) {
    			versionFolders.add(version);
    		}
    	}
    }
    
    private void makeRawData() {
    	
    	
    	for( File currentVersion: versionFolders) {
    		
    		try {
    			System.out.println( currentVersion.getName());
    			new RawData(currentVersion, resultDirectory);
    		}
    		catch (FileNotFoundException e) {
    			e.printStackTrace();
			}
    	}
        //new GodFinder( resultDirectory+"raw.csv", resultDirectory );
    }
    
    private void createResultDirectory() {
    	
    	new File(resultDirectory).mkdirs();
    }
    
    public void selectRootDirectory() {
    	
    	JFileChooser f = new JFileChooser();
        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
        f.showOpenDialog(null);
        
        try {
        	rootDirectory = f.getSelectedFile().toString();
        }
        catch (NullPointerException e) {
			System.out.println("No Directory Selected");
		}
	}
}
