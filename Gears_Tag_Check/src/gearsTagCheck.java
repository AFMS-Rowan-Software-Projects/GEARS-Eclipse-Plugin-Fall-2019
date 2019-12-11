import java.io.*;
import java.util.ArrayList;
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
		ArrayList<String> tags = new ArrayList<String>();
		tags.add("//Delete");
		//Dummy tag "//Delete" is for the test files, but for the actual files the
		// arrayList of Strings for the tags would be determined from the logic files
		System.out.println("\n");
		outputFile = convert(codeFile, "testing", tags);
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
	
	public static File convert(File codeFile, String newFileName, ArrayList<String> tags) throws IOException {
		
		Scanner sc = new Scanner(codeFile);
		File outputFile = new File(newFileName);
		FileWriter fw = new FileWriter (outputFile);
		
		int deleteTagStatus = 0;
		//int and not boolean to account for future possibilities such as
		//conditionally deleting parts of the line of code after I gain access
		//to the logic files
		
		
		while (sc.hasNextLine()) {
			String currLine = sc.nextLine();
			//iterates through the current set of deletion tags provided by the tags arraylist
			for(int i=0; i<tags.size(); i++) {
				if(currLine.contains(tags.get(i))) {
					if(deleteTagStatus == 0) {
						//If found the deletion tag, start the deletion process
						deleteTagStatus = 1;
						System.out.println("Found Tag, deleting lines");
					} else {
						//If found the deletion tag, end the deletion process
						deleteTagStatus = 0;
						System.out.println("Found Tag, saving lines");
					}
				} else {
					if(deleteTagStatus == 0) {
						//if not deleting, write the line to the new file
						fw.write(currLine + "\n");
					} else {
						//if deleting, ignore the line and move to the next one
						System.out.println("Delete this line");
					}
				}
			}
		}
			
		
		fw.close();
		sc.close();
		return outputFile;
	}
}
