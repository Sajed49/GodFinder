package godFinder;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import io.CustomFileReader;
import io.CustomFileWriter;

public class GodFinder {
	
	private String resultDirectory;
	
	private ArrayList< ArrayList<String> > raw = new ArrayList< ArrayList<String> >();
	private ArrayList< ArrayList<String> > result = new ArrayList< ArrayList<String> >();
	private ArrayList<String> header = new ArrayList<>( 
			Arrays.asList( "Class Name", "Access To Foreign Data", "Weighted Method Complexity",
					"Tight Class Cohession", "File Path"));
	
	public GodFinder(String rawFilePath, String resultDirectory) {
		this.resultDirectory = resultDirectory;
		loadRawData(rawFilePath);
		
		raw.remove(0); // remove header line
		
		findGodClasses();
		outputFinalResult();
	}
	
	
	private void outputFinalResult() {
		
		CustomFileWriter.writeAFile( new File( resultDirectory + "result.csv"), result);
		
	}
	private void findGodClasses() {
		
		//add header to result
		result.add(header);
		
		for( ArrayList<String> temp: raw) {
			
			int atfd = Integer.valueOf( temp.get(1) );
			int wmc = Integer.valueOf( temp.get(2) );
			double tcc = Double.valueOf( temp.get(3) );
			
			if( atfd > 5 && wmc > 47 && tcc > 0.33) result.add(temp);
		}
	}
	
	private void loadRawData(String rawFilePath) {
		
		raw = CustomFileReader.readAfile( new File(rawFilePath) );
	}
}
