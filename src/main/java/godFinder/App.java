package godFinder;

import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;

import fileAnalyzer.RawData;

public class App 
{	
	
	private String rootDirectory = "";
	private String resultDirectory = "Result//";
    public static void main( String[] args )
    {	
    	new App();
        
    	
    }
    
    public App() {
    	
    	while( rootDirectory.equals("") ) selectRootDirectory();
    	createResultDirectory();
    	
    	processing();
	}
    
    private void processing() {
    	
    	try {
			new RawData(rootDirectory, resultDirectory);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        new GodFinder( resultDirectory+"raw.csv", resultDirectory );
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
