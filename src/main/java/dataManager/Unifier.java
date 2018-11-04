package dataManager;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import io.CustomFileReader;
import io.CustomFileWriter;

public class Unifier {
	
	private int useLessColumns = 2;
	
	private String basePath = "Result\\";
	private String outputPath = "Result\\Normalized Feature Data\\";
	private ArrayList< ArrayList< ArrayList<String> > > allVersionData = 
			new ArrayList< ArrayList< ArrayList<String> > >();
	
	private Map<String, Integer> classMap = new TreeMap<String, Integer>();
	
	private ArrayList<File> featureFiles = new  ArrayList<File>();
	private ArrayList< ArrayList< ArrayList<String> > > normalizedData = 
			new ArrayList< ArrayList< ArrayList<String> > >();
	
	public static void main(String[] args) {
		new Unifier();
	}
	
	
	
	public Unifier() {
		
		createResultDirectory();
		
		loadVersionData();
		
		getFeatures();
		
		mapClasses();

		combineVersionData();
		
		getNormalizedSum();
	}
	
	
	private void getNormalizedSum() {
		
		ArrayList< ArrayList<String> > data = new ArrayList<ArrayList<String>>();
		
		addClasses(data); // Add the path and class name
		
		for( int k=0; k<normalizedData.get(0).size(); k++ ) { //row count
				
			for(int i=useLessColumns; i< normalizedData.get(0).get(0).size() ; i++) { //column count
				
				double temp = 0;
				for( int j=0; j<normalizedData.size(); j++ ) { //feature file count
					
					String s = normalizedData.get(j).get(k).get(i);
					
					if( j == 2) temp += (1.000-Double.parseDouble(s)); //tcc less is better
					else temp += Double.parseDouble(s);

				}
				
				data.get(k).add( doubleToString(temp) );
			}
		}
		
		CustomFileWriter.writeAFile( new File(outputPath+"All Features.csv"), data);
	}
	
	
	private void combineVersionData() {
		
		int featureID = useLessColumns;
		
		for(File currentFeatureFile: featureFiles) {
			
			ArrayList< ArrayList<String> > data = new ArrayList<ArrayList<String>>();
			
			addClasses(data);
			
			addSinglefeatureData(data, featureID++);
			
			normalizeData(data);
			
			normalizedData.add(data);
			
			CustomFileWriter.writeAFile(currentFeatureFile, data);
		}
	}
	
	
	private void normalizeData( ArrayList< ArrayList<String> > data ) {
		
		double highest = 0.0;
		double lowest = Double.MAX_VALUE;
		
		for(ArrayList<String> line: data  ) {
			
			for( int i=useLessColumns; i<line.size(); i++) {
				double temp = Double.parseDouble( line.get(i) );
				
				if( temp > highest ) highest = temp;
				if( temp < lowest ) lowest = temp;
			}
			
		}
		
		
		for(ArrayList<String> line: data  ) {
			
			for( int i=useLessColumns; i<line.size(); i++) {
				
				double temp = Double.parseDouble( line.get(i) );
				temp = ( temp - lowest ) / ( highest - lowest);
				
				line.set(i, doubleToString(temp) );
			}
			
		}
		//System.out.println(data.get(0));
	}
	
	private String doubleToString(double temp) {
		
		BigDecimal bd = new BigDecimal(temp);
	    bd = bd.setScale(5, RoundingMode.HALF_UP);
	    temp =  bd.doubleValue();
	    
	    return  String.valueOf(temp);
	}
	
	private void addSinglefeatureData( ArrayList< ArrayList<String> > data, int id ) {
		
		int counter = useLessColumns;
		for( ArrayList< ArrayList<String> > version: allVersionData) {
			
			counter++;
			for( ArrayList<String> line: version) {
				
				String key = line.get(0)+" "+line.get(1);
				int serial = classMap.get(key);
				//System.out.println(line.get(id));
				data.get(serial).add( line.get(id) );
				
			}
			
			for( ArrayList<String> temp: data) {
				if( temp.size() < counter) temp.add("0");
			}
		}
		
		//System.out.println( data.get(410).size());
	}
	
	private void addClasses( ArrayList< ArrayList<String> > data ) {
		
		SortedSet<String> keys = new TreeSet<>(classMap.keySet());
		
		for( String key: keys) {
			String words[] = key.split(" ");
			
			ArrayList<String> temp = new ArrayList<String>();
			temp.add(words[0]);
			temp.add(words[1]);
			data.add(temp);
		}
	}
	
	private void mapClasses() {
		
		int id  = 0;
		for(ArrayList< ArrayList<String> >version: allVersionData) {
			
			for( ArrayList<String> line: version ) {
				
				String temp = line.get(0)+" "+line.get(1);
				
				if( !classMap.containsKey(temp) ) {
					classMap.put(temp, id++);
				}
			}
		}

	}
	
	
	private void loadVersionData() {
		
		for( File f: new File(basePath+"RawData\\").listFiles() ) {
			allVersionData.add( getRawData(f) );	
		}
		
	}
	
	private void getFeatures() {
		
		//total feature files (excluding first two features)
		int totalFeatures = allVersionData.get(0).get(0).size();
		for( int i=useLessColumns; i<totalFeatures; i++) {
			String name = allVersionData.get(0).get(0).get(i);
			featureFiles.add(  new File( outputPath + name+".csv") );
		}
		
		//removing header features
		for(ArrayList< ArrayList<String> >version: allVersionData) version.remove(0);
				
	}
	
	private ArrayList< ArrayList<String> > getRawData(File file) {
		
		return CustomFileReader.readAfile( file );
	}
	
	private void createResultDirectory() {
    	
    	new File(outputPath).mkdirs();
    }
}
