package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CustomFileReader {
	
	public static ArrayList< ArrayList<String> > readAfile( File currentFile){
		
		ArrayList< ArrayList<String> > raw = new ArrayList< ArrayList<String> >();
		
		BufferedReader br = null;
		FileReader fr = null;

		try {

			
			fr = new FileReader(currentFile);
			br = new BufferedReader(fr);

			String line;
			
			while ((line = br.readLine()) != null) {

				String temp [] = line.split(",");
				for(int i=0; i<temp.length; i++) temp[i] = temp[i].trim();
				raw.add( new ArrayList<String>( Arrays.asList( temp )) );
			}

		} catch (FileNotFoundException e) {

			System.out.println("Could not found "+currentFile.getName()+" in "+currentFile.getPath());

		}
		catch (IOException e) {

			System.out.println("Error in reading file in "+currentFile.getName()+" in "+currentFile.getPath());

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
		
		return raw;
	}
}
