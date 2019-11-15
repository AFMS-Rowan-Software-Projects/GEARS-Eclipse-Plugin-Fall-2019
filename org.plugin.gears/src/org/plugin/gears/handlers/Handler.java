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
import java.nio.file.Files;
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
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
        JLabel browserLabel = new JLabel(currentDir);
        browserLabel.setFont(new Font("Monospace", Font.PLAIN, 20));
        browserLabel.setForeground(Color.BLACK);
        contentPane.add(browserLabel, BorderLayout.PAGE_START);
        f.setSize(620,500);
        f.setVisible(true);
        
	}
	/**
	 * mainGUI handles the main GUI window.
	 */
	public static void mainGUI(){
		//main GUI components.
		JFrame frame = new JFrame("GEARS interface");
		frame.setSize(620,500);
		//below layout aligns added panels vertically in the GUI.
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		//creates placeholder panels for GUI items.
		JPanel pane1 = new JPanel();
		JPanel pane3 = new JPanel();
		JPanel pane4 = new JPanel();
		JPanel pane5 = new JPanel();
		JPanel pane6 = new JPanel();
		//adds "Projected File Path" Label,Text field,and Button to pane1.
	    pane4.setBackground(Color.WHITE);
		    pane4.setLayout(new FlowLayout(FlowLayout.CENTER));
		    JLabel pfpLabel = new JLabel("Projected File Path : ");
		    pfpLabel.setFont(new Font("Monospace", Font.PLAIN, 20));
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
			pane1.setLayout(new FlowLayout(FlowLayout.CENTER));
			JLabel dirLabel = new JLabel("Select Directory : ");
			dirLabel.setFont(new Font("Monospace", Font.PLAIN, 25));
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
		//adds "Logic File" Label,Text field,and Button to pane1.
		pane3.setBackground(Color.WHITE);
			pane3.setLayout(new FlowLayout(FlowLayout.CENTER));
			JLabel logLabel = new JLabel("Logic File : ");
			logLabel.setFont(new Font("Monospace", Font.PLAIN, 25));
			logLabel.setForeground(Color.BLACK);
			pane3.add(logLabel);
			JTextField logTextfield;
		    logTextfield = new JTextField("logic file here");
		    logTextfield.setPreferredSize(new Dimension(275,35));
		    pane3.add(logTextfield);
		    JButton logButton;
		    logButton = new JButton("browse...");
		    logButton.setPreferredSize(new Dimension(100,35));
		    logButton.addActionListener(new ActionListener(){
			    public void actionPerformed(ActionEvent e){
			    	fileList(1, logTextfield, pfpTextfield);
			    }
		    });
		    pane3.add(logButton);
		//adds "Create Projected File" Button to pane1.
	    pane5.setBackground(Color.WHITE);
	    	JButton goButton;
	        goButton = new JButton("Create Projected File");
	        goButton.setPreferredSize(new Dimension(300,35));
	        goButton.addActionListener(new ActionListener(){
			    public void actionPerformed(ActionEvent e){
			    	try {
						project(rootDir);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			    }
		    });
	        pane5.add(goButton);
	    //adds "View Projected File" Button to pane1.    
	    pane6.setBackground(Color.WHITE);
		    JButton showButton;
	        showButton = new JButton("View Projected File");
	        showButton.setPreferredSize(new Dimension(300,35));
	        pane6.add(showButton);
        //adding all components to main GUI frame.
	    frame.add(pane1);
	    frame.add(pane3);
	    frame.add(pane4);
	    frame.add(pane5);
	    frame.add(pane6);
	    frame.setVisible(true);
	}
	/**
	 * executes runs the plug-in and related methods.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		mainGUI();
	    //plug-in return.
		return null;
	}
	public static void project(File from) throws IOException
	{
		File to = new File(projDir,from.getName() + "_proj");
		to.mkdir();
		
		cloneDir(from, to);
	}
	static boolean debug = false;
	
	public static void cloneDir(File from, File to) throws IOException
	{
		File[] files = from.listFiles();
		String pattern = "[.]+.*";
		for(int i = 0; i < files.length; i++)
		{
			File temp = new File(to, files[i].getName());
			if(debug)
				System.out.println(temp.getName());
			if(files[i].isDirectory())
			{
				if(Pattern.matches(pattern, files[i].getName()))	//if temp is projected directory, modify name and apply projection
				{
					temp = new File(to, temp.getName().substring(1));
					temp.mkdir();
					//TODO: apply projection
				}
				else	//else, create the directory and continue copying recursively
					temp.mkdir();
				cloneDir(files[i], temp);
			}
			else
				try
				{
					if(Pattern.matches(pattern, files[i].getName()))	//if temp is projected file, modify name and apply projection
					{
						temp = new File(to, temp.getName().substring(1));
						convert(files[i],temp);
					}
					else	//else, just copy the file as below
					{					
						temp.createNewFile();
						var source = Paths.get(files[i].toString());
				        var dest = Paths.get(temp.toString());
	
				        try (var fis = Files.newInputStream(source);
				             var fos = Files.newOutputStream(dest)) {
	
				            byte[] buffer = new byte[1024];
				            int length;
	
				            while ((length = fis.read(buffer)) > 0) {
	
				                fos.write(buffer, 0, length);
				            }
				        }
					}
				}
				catch (IOException e)
				{
				    e.printStackTrace();
				}
		}
		
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
}
