import java.io.*;
//import java.nio.file.Files;
import java.util.Scanner;

public class gearsTagCheck {

	static BufferedReader reader;
	static FileReader reader2;
	
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		File codeFile = new File("ex");
		File outputFile;
		printFile(codeFile);
		System.out.println("\n");
		outputFile = convert(codeFile, "testing");
		System.out.println("\n");
		printFile(outputFile);
		
	}
	
	public static void printFile(File codeFile) throws FileNotFoundException{
		//this method is just used to check if the File class is being used correctly
		Scanner sc = new Scanner(codeFile);
		while(sc.hasNextLine()) {
			System.out.println(sc.nextLine());
		}
		System.out.println("end of line.");
		sc.close();
	}
	
	public static File convert(File codeFile, String newFileName) throws IOException {
		
		Scanner sc = new Scanner(codeFile);
		File outputFile = new File(newFileName);
		FileWriter fw = new FileWriter (outputFile);
		
		int deleteTagStatus = 0;
		//int and not boolean to account for future possibilities such as
		//conditionally deleting parts of the line of code after I gain access
		//to the logic files
		
		
		while (sc.hasNextLine()) {
			String currLine = sc.nextLine();
			if(currLine.contains("//Delete")) {
				if(deleteTagStatus == 0) {
					deleteTagStatus = 1;
					System.out.println("Found Tag, deleting lines");
				} else {
					deleteTagStatus = 0;
					System.out.println("Found Tag, saving lines");
				}
			} else {
				if(deleteTagStatus == 0) {
					System.out.println("Keep this line");
					fw.write(currLine + "\n");
				} else {
					System.out.println("Delete this line");
				}
			}
		}
		
		fw.close();
		sc.close();
		return outputFile;
	}
}
