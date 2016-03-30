import java.io.*;
import java.util.Scanner;



public class someFile {
	
	public static void main(String[] args) throws IOException {
		FileReader fr = null;
		BufferedReader br = null;
		String charArray = "";
		
		try {
			br = new BufferedReader(new FileReader("./text/test.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Could not find file");
		}
		
		int i = br.read();
		while (i != -1) {
			//System.out.println((char)i);
			if ((char)i == '\n') {
				if ((char)(i+1) != '\r') {
					charArray += 
				}
			}
			i = br.read();
		}
	}
}