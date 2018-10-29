package io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CustomFileWriter {
	
	public static void writeAFile(File currentFile ,ArrayList< ArrayList<String> > lines) {
		
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {

			
			fw = new FileWriter(currentFile);
			bw = new BufferedWriter(fw);

			for(ArrayList<String> temp: lines) {
				
				String s = makeCSVLine(temp);
				bw.write(s);
				bw.newLine();
			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}
	}
	
	
	private static String makeCSVLine( ArrayList<String> temp) {
		
		String line = "";
		
		if(temp.size() > 0 ) line += temp.get(0);
		
		for(int i=1; i<temp.size(); i++) {
			line += ", "+temp.get(i);
		}
		
		return line;
	}
}
