package godFinder;

import java.io.FileNotFoundException;

public class App 
{
    public static void main( String[] args )
    {
        try {
			new ProjectData("E:\\GitHub\\GodFinder");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
