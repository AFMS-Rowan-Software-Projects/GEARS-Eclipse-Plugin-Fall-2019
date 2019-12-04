package org.plugin.gears.handlers;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.ResourcesPlugin;

/*
 * Handler for the Gears Eclipse Plug-in.
 * Handles the GUI, and related menus and file browsing.
 */
public class Handler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public Handler() {
	}
	//counter for double click monitoring across the GUI.
	int clickCount = 0;
	//String name of the currently displayed directory.
	static String currentDir = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
	//empty ArrayList to hold scroll list labels.
	static ArrayList<String> labels = new ArrayList<>();
	//empty ArrayList to hold all files in a root directory.
	static ArrayList<File> listOfFiles = new ArrayList<>();
	//empty ArrayLists to hold file and directory names.
	static ArrayList<String> files = new ArrayList<>();
    static ArrayList<String> directories = new ArrayList<>();
	static File choice = new File(ResourcesPlugin.getWorkspace().getRoot().getLocation().toString());
	//Files chosen by the user.
	static File rootDir = new File(ResourcesPlugin.getWorkspace().getRoot().getLocation().toString());
	static File logic;
	static File projDir = new File(ResourcesPlugin.getWorkspace().getRoot().getLocation().toString());
	static int size = 0;
	static int runCount = 0;
	/**
	 * fileList handles displaying the "browse" menu which shows relevant
	 * files in the users eclipse workspace and sub-directories.
	 *
	 * @param  check: int instruction, tells the method which area
	 * is being edited by the user. Pass 0 to change the root directory field, 
	 * 1 (or other) to edit logic file field, and 2 to edit the
	 * projected directory field.
	 * @param  input: String file location of the directory whose contents the user
	 * wants to display. 
	 * @see	makeList
	 */
	public static void getSize(File tempDir){
		int index = 0;
		File[] tempArray = tempDir.listFiles();
		while(index<tempArray.length){
			size++;
			if(tempArray[index].isDirectory()){
				getSize(tempArray[index]);
			}
			index++;
		}
	}
	public static ArrayList<String> makeList(int check, String input){
		File folder = new File(input);
		if(folder.isDirectory()){
			labels.clear();
	    	listOfFiles = new ArrayList<File>(Arrays.asList(folder.listFiles()));
	    	files.clear();
	    	directories.clear();
	        //splits listOfFiles into the two string ArrayLists files and directories.
	        for (int i = 0; i < listOfFiles.size(); i++) {
	            if (listOfFiles.get(i).isFile()) {
	                files.add(listOfFiles.get(i).getName());
	            } else if (listOfFiles.get(i).isDirectory()) {
	                directories.add(listOfFiles.get(i).getName());
	            }
	        }
	        //fill labels with files, directories, or both, based on check value.
	        if(check == 0 || check == 2){
	             int i = 0;
	             while(i < directories.size()){
	            	labels.add(directories.get(i));
	             	i++;
	             }
	        }
	        else{
	             int i = 0;
	             while(i<files.size()){
	            	 labels.add(files.get(i));
	             	i++;
	             }
	             while(i<files.size()+directories.size()){
		            	labels.add(directories.get(i-files.size()));
		             	i++;
		             }
	        }
		}
		return labels;
	}
	/**
	 * fileList handles displaying the "browse" menu which shows relevant
	 * files in the users eclipse workspace and sub-directories.
	 *
	 * @param  check: int instruction, tells the method which area
	 * is being edited by the user. Pass 0 to change the root directory field, 
	 * 1 (or other) to edit logic file field, and 2 to edit the
	 * projected directory field.
	 * @param  textToEdit: JTextField in main GUI that you want to
	 * make changes to.
	 * @see	fileList
	 */
	public static void fileList(int check, JTextField textToEdit, JTextField projTextToEdit){
		//create empty ArrayLists to hold file and directory names.
        //get workspace files, save them in listOfFiles.
        labels = makeList(check, currentDir);
        //fileList GUI components.
        String title = "JList Sample";
        JFrame f = new JFrame(title);
        JList list = new JList(labels.toArray());
        list.setFont(new Font("Monospace", Font.PLAIN, 25));
        JScrollPane scrollPane = new JScrollPane(list);
        Container contentPane = f.getContentPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);
        //mouseListener detects a double click
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
            	JList list = (JList)evt.getSource();
                if (evt.getClickCount() == 2) {
                    // Double-click detected
                	int index;
                	index = list.locationToIndex(evt.getPoint());
                	if(index!=-1){
                		if(check==1){
                    		currentDir = listOfFiles.get(index).getAbsolutePath();
                    	}
                    	else{
                    		currentDir = listOfFiles.get(index+files.size()).getAbsolutePath();
                    	}
                		f.dispose();
                    	fileList(check, textToEdit, projTextToEdit);
                	}
                }
            }
        });
        JPanel buttonPane = new JPanel();
        //goButton is a fileList GUI button that registers the file object selected
        //in the scroll list created above (scrollPane).
        JButton goButton;
        goButton = new JButton("Select");
        goButton.setPreferredSize(new Dimension(100,35));
        goButton.addActionListener(new ActionListener(){
	        public void actionPerformed(ActionEvent e){
	        	int index = list.getSelectedIndex();
	        	if(index!=-1){ //if a file is selected.
	        		if(check==0){//if choosing root directory
	        			//adjust scrollPane index to correctly map to files.
	        			choice = listOfFiles.get(index+files.size());
	        			rootDir = choice;
	        			projDir = choice.getParentFile();
	        		}
	        		else if(check==2){//if choosing projected directory
	        			choice = listOfFiles.get(index+files.size());
	        			projDir = choice;
	        		}
	        		else{//if choosing logic file
	        			choice = listOfFiles.get(index);
	        			logic = choice;
	        		}
	        	}
	        	//set main GUI label (textToEdit) to the users chosen file.
	        	if(check==0){
	        		projTextToEdit.setText(projDir.getAbsolutePath());
	        	}
	        	textToEdit.setText(choice.getAbsolutePath());
	        	f.dispose(); //end
	        }
        });
        JButton cancelButton;
        cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(100,35));
        cancelButton.addActionListener(new ActionListener(){
	        public void actionPerformed(ActionEvent e){
	        	f.dispose(); //end
	        }
        });
        buttonPane.add(goButton, BorderLayout.LINE_START);
        buttonPane.add(cancelButton, BorderLayout.LINE_END);
        contentPane.add(buttonPane, BorderLayout.PAGE_END);
        JPanel toppane = new JPanel();
        JLabel browserLabel = new JLabel(currentDir);
        JButton backButton;
        backButton = new JButton("back");
        backButton.setPreferredSize(new Dimension(100,35));
        backButton.addActionListener(new ActionListener(){
	        public void actionPerformed(ActionEvent e){
	        	File temp = new File(currentDir);
	        	temp = temp.getParentFile();
	        	currentDir = temp.getAbsolutePath();
	        	fileList(check, textToEdit, projTextToEdit);
	        	f.dispose();
	        }
        });
        browserLabel.setFont(new Font("Monospace", Font.PLAIN, 20));
        browserLabel.setForeground(Color.BLACK);
        toppane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toppane.add(backButton);
        toppane.add(browserLabel);
        contentPane.add(toppane, BorderLayout.PAGE_START);
        f.setSize(620,400);
        f.setVisible(true);
        
	}
	/**
	 * mainGUI handles the main GUI window.
	 */
	public static void mainGUI(){
		//main GUI components.
		JFrame frame = new JFrame("GEARS interface");
		frame.setSize(620,400);
		//below layout aligns added panels vertically in the GUI.
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		//creates placeholder panels for GUI items.
		JPanel pane1 = new JPanel();
		JPanel pane4 = new JPanel();
		JPanel pane5 = new JPanel();
		JPanel pane6 = new JPanel();
		//adds "Projected File Path" Label,Text field,and Button to pane1.
	    pane4.setBackground(Color.WHITE);
		    pane4.setLayout(new FlowLayout(FlowLayout.RIGHT));
		    JLabel pfpLabel = new JLabel("Projected File Path   ");
		    pfpLabel.setFont(new Font("Monospace", Font.PLAIN, 22));
		    pfpLabel.setForeground(Color.BLACK);
		    pane4.add(pfpLabel);
		    JTextField pfpTextfield;
	        pfpTextfield = new JTextField(projDir.toString());
	        pfpTextfield.setPreferredSize(new Dimension(275,35));
	        pane4.add(pfpTextfield);
	        JButton pfpButton;
	        pfpButton = new JButton("browse...");
	        pfpButton.setPreferredSize(new Dimension(100,35));
	        pfpButton.addActionListener(new ActionListener(){
		        public void actionPerformed(ActionEvent e){
		        	fileList(2, pfpTextfield, pfpTextfield);
		        }
	        });
	        pane4.add(pfpButton);
		//adds "Select Directory" Label,Text field,and Button to pane1.
		pane1.setBackground(Color.WHITE);
			pane1.setLayout(new FlowLayout(FlowLayout.RIGHT));
		    JLabel dirLabel = new JLabel("Select Directory        ");
			dirLabel.setFont(new Font("Monospace", Font.PLAIN, 22));
			dirLabel.setForeground(Color.BLACK);
			pane1.add(dirLabel);
			JTextField dirTextfield;

			dirTextfield = new JTextField(rootDir.getAbsolutePath());
			dirTextfield.setPreferredSize(new Dimension(275,35));
			pane1.add(dirTextfield);
			JButton dirButton;
			dirButton = new JButton("browse...");
			dirButton.setPreferredSize(new Dimension(100,35));
			dirButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					fileList(0, dirTextfield, pfpTextfield);
				}
			});
			pane1.add(dirButton);  
	    pane6.setBackground(Color.WHITE);
	    //adds "Create Projected File" Button to pane1.
	    pane5.setBackground(Color.WHITE);
		 	JButton goButton;
		    goButton = new JButton("Create Projected File");
		    goButton.setPreferredSize(new Dimension(300,35));
		    goButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					try {
							project(rootDir);
							getSize(rootDir);
							JProgressBar pb = new JProgressBar();
					        pb.setMinimum(0);
					        pb.setMaximum(size);
					        pb.setStringPainted(true);
					        pb.setPreferredSize(new Dimension(500,35));
					        pane6.removeAll();					    
							pane6.add(pb);
							pane6.revalidate();
							int i = 0;
							while(i<size) {
								i = runCount;
					            try {
					            	pb.setValue(runCount);
			                        pb.revalidate();
			                        pane6.revalidate();
					                java.lang.Thread.sleep(1);
					                }
					             catch (InterruptedException p) {
					                JOptionPane.showMessageDialog(frame, p.getMessage());
					            }
					        }
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
		    pane5.add(goButton);
        //adding all components to main GUI frame.
	    frame.add(pane1);
	    frame.add(pane4);
	    frame.add(pane5);
	    frame.add(pane6);
	    frame.setVisible(true);
	}

	public static void project(File from) throws IOException
	{
		checkVar(from);
	}
	static boolean debug = false;
	
	public static void checkVar(File dir) throws IOException
	{
		
		boolean isVar = false;
		String pattern = "[.]+.*";
		String name = dir.getName();
		File temp = null;
		if(Pattern.matches(pattern, name))	//if name matches the pattern, a variant directory or a variant FILE directory
		{
			isVar = true;
			if(name.length()<4) //this is a slight concession, but if a File object matches the pattern, it must be a variant directory, because _cpp and _java variants will have to have longer names.
				temp = applyDirectoryProjection(dir, projDir);
			else if(name.substring(name.length()-4,name.length()).equals("_cpp")) //if the end of the directory has _cpp, it's a variant file directory. Could include logic here for _java.
				temp = applyFileProjection(dir, projDir);
			else //it is safe to assume a File object that matches the pattern but doesn't have _cpp or _java, it is a directory.
				temp = applyDirectoryProjection(dir, projDir);
			isVar = true;
		}
		else
		{
			temp = new File(projDir,dir.getName() + "_proj");
			temp.mkdir();
		}
		if(temp.isDirectory())
			cloneDir(dir,temp,isVar);
	}
	
	public static void cloneDir(File from, File to, boolean projectDirectory) throws IOException
	{
		File[] files = from.listFiles();
		for(int i = 0; i < files.length; i++)
		{
			File temp = new File(to, files[i].getName());
			runCount++;
			System.out.println(runCount);
			System.out.println(temp.getName());
			if(files[i].isDirectory())
			{
				String pattern = "[.]+.*";
				String name = files[i].getName();
				if(Pattern.matches(pattern, name))	//if name matches the pattern, it's either a variant directory or a variant FILE directory
				{
					
					if(name.length()<4) //this is a slight concession, but if a File object matches the pattern, it must be a variant directory, because _cpp and _java variants will have to have longer names.
						temp = applyDirectoryProjection(files[i], to);
					else if(name.substring(name.length()-4,name.length()).equals("_cpp")) //if the end of the directory has _cpp, it's a variant file directory. Could include logic here for _java.
						temp = applyFileProjection(files[i], to);
					else //it is safe to assume a File object that matches the pattern but doesn't have _cpp or _java, it is a directory.
						temp = applyDirectoryProjection(files[i], to);
					if(temp.isDirectory())
						cloneDir(files[i],temp,true);
				}
				else	//else continue recursing
				{
					if(projectDirectory) //With this, the directory will be cloned but not copied to the projection. This essentially turns the ".h" and "h_var" file hierarchy into the projected version, which is just "h"
						cloneDir(files[i],temp.getParentFile(),false);
					else //Otherwise, clone and copy the directory. This will copy the directory exactly as it is in the variant path.
					{
						temp.mkdir();
						cloneDir(files[i], temp, false);
					}
				}
			}
			else
				try
				{	
					if(!files[i].getName().contains("big_leaver"))
					{
						temp.createNewFile();
						Path source = Paths.get(files[i].toString());
				        Path dest = Paths.get(temp.toString());
	
				        try (InputStream fis = Files.newInputStream(source);
				             OutputStream fos = Files.newOutputStream(dest)) 
				        {
	
				            byte[] buffer = new byte[1024];
				            int length;
	
				            while ((length = fis.read(buffer)) > 0)
				                fos.write(buffer, 0, length);
				        }
					}
				}
				catch (IOException e)
				{
				    e.printStackTrace();
				}
		}
		
	}

	/**
	 * applyFileProjection projects a single file from a single variant file 
	 * directory. variant file directories should contain ONLY two files: 
	 * big_leaver.bllt and the single variant file.
	 * @param from
	 * @throws IOException 
	 */
	public static File applyFileProjection(File from, File to) throws IOException
	{
		
		File[] files = from.listFiles();
		File BL = null;
		File cppVar = null;
		for(int i = 0; i < files.length; i++)
		{
			runCount++;
			if(files[i].getName().contains("big_leaver"))
				BL = files[i];
			if(files[i].getName().contains(".cpp"))
				cppVar = files[i];
		}
		//TODO: read big_leaver and based on its contents, decide what files to project
		//For now, we decide just to project the variant file assuming all tags are
		//slated for deletion
		
		if(cppVar == null)
		{
			System.err.println("NO VARIANT FILE DETECTED IN " + from.getAbsolutePath());
			return null;
		}
		
		File temp = convert(cppVar, new File(to,from.getName().substring(1,from.getName().length()-4) + ".cpp"));
		temp.createNewFile();
		return temp;
	}
	
	/**
	 * applyDirectoryProjection projects a single directory from a single variant 
	 * file directory. variant file directories currently ONLY support two files: 
	 * big_leaver.bllt and the single variant directory.
	 * @param from: The variant file directory.
	 * @param to: The directory to project to.
	 * @throws IOException
	 */
	public static File applyDirectoryProjection(File from, File to) throws IOException
	{
		File[] files = from.listFiles();
		File BL = null;
		File dirVar = null;
		for(int i = 0; i < files.length; i++)
		{
			if(files[i].getName().contains("big_leaver"))
				BL = files[i];
			if(files[i].getName().contains("_var"))
				dirVar = files[i];
		}
		//TODO: read big_leaver and based on its contents, decide what files to project
		//For now, we decide just to project the variant file assuming all tags are
		//slated for deletion
		if(dirVar == null)
		{
			System.err.println("NO VARIANT DIRECTORY DETECTED IN " + from.getAbsolutePath());
			return null;
		}
		File temp = new File(to, from.getName().substring(1));
		temp.mkdir();
		return temp;
	}
	
	public static File convert(File codeFile, File newFile) throws IOException 
	{		
		Scanner sc = new Scanner(codeFile);
		FileWriter fw = new FileWriter (newFile);
		
		int deleteTagStatus = 0;
		//int and not boolean to account for future possibilities such as
		//conditionally deleting parts of the line of code after I gain access
		//to the logic files
		
		
		while (sc.hasNextLine()) {
			String currLine = sc.nextLine();
			if(currLine.contains("//Delete")) {
				if(deleteTagStatus == 0) {
					deleteTagStatus = 1;
					if(debug)
						System.out.println("Found Tag, deleting lines");
				} else {
					deleteTagStatus = 0;
					if(debug)
						System.out.println("Found Tag, saving lines");
				}
			} else {
				if(deleteTagStatus == 0) {
					if(debug)
						System.out.println("Keep this line");
					fw.write(currLine + "\n");
				} else if(debug)
						System.out.println("Delete this line");
			}
		}
		
		fw.close();
		sc.close();
		return newFile;
	}
	/**
	 * executes runs the plug-in and related methods.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		mainGUI();
	    //plug-in return.
		return null;
	}
}
