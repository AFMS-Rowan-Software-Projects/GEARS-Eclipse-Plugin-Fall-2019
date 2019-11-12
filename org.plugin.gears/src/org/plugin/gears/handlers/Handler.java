package org.plugin.gears.handlers;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

/*
 * Handler for the Gears Eclipse Plug-in.
 * Handles the GUI, and related menus and files browsing.
 */
public class Handler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public Handler() {
	}
	static File choice = null;
	/**
	 * fileList handles displaying the "browse" menu which shows relevant
	 * files in the users eclipse workspace and sub-directories.
	 *
	 * @param  check: int instruction, tells the method which files should
	 * be displayed for the user. Pass 0 to display only directories, 
	 * 1 to display only files, and any other int (ex: 2) to display
	 * both.
	 * @param  textToEdit: JTextField in main GUI that you want to
	 * make changes to.
	 * @see	fileList
	 */
	public static void fileList(int check, JTextField textToEdit){
		//create empty ArrayLists to hold file and directory names.
		List<String> files = new ArrayList<>();
        List<String> directories = new ArrayList<>();
        //get workspace files, save them in listOfFiles.
        String workspace = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
    	File folder = new File(workspace);
    	File[] listOfFiles = folder.listFiles();
        //splits listOfFiles into the two string ArrayLists files and directories.
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                files.add(listOfFiles[i].getName());
            } else if (listOfFiles[i].isDirectory()) {
                directories.add(listOfFiles[i].getName());
            }
        }
        //create empty array labels to be used in the fileList GUI.
        String[] labels;
        //fill labels with files, directories, or both, based on check value.
        if(check == 0){
        	 labels = new String[directories.size()];
             int i = 0;
             while(i < directories.size()){
             	labels[i] = directories.get(i);
             	i++;
             }
        }
        else if(check == 1){
        	 labels = new String[files.size()];
             int i = 0;
             while(i < files.size()){
             	labels[i] = files.get(i);
             	i++;
             }
        }
        else{
        	 labels = new String[files.size()+directories.size()];
             int i = 0;
             while(i < files.size()){
             	labels[i] = files.get(i);
             	i++;
             }
             while(i<files.size()+directories.size()){
             	labels[i] = directories.get(i-files.size());
             	i++;
             }
        }
        //fileList GUI components.
        String title = "JList Sample";
        JFrame f = new JFrame(title);
        JList list = new JList(labels);
        list.setFont(new Font("Monospace", Font.PLAIN, 25));
        JScrollPane scrollPane = new JScrollPane(list);
        Container contentPane = f.getContentPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);
        //goButton is a fileList GUI button that registers the file object selected
        //in the scroll list created above (scrollPane).
        JButton goButton;
        goButton = new JButton("select");
        goButton.setPreferredSize(new Dimension(100,35));
        goButton.addActionListener(new ActionListener(){
	        public void actionPerformed(ActionEvent e){
	        	int index = list.getSelectedIndex();
	        	if(index!=-1){ //if a file is selected.
	        		if(check==0){ //if only showing files,
	        			//adjust scrollPane index to correctly map to files.
	        			choice = listOfFiles[index+files.size()];
	        		}
	        		else{
	        			choice = listOfFiles[index];
	        		}
	        	}
	        	//set main GUI label (textToEdit) to users chosen file.
	        	textToEdit.setText(choice.getAbsolutePath());
	        	f.dispose(); //end
	        }
        });
        contentPane.add(goButton, BorderLayout.PAGE_END);
        f.setSize(200, 400);
        f.setVisible(true);
	}
	/**
	 * execute runs the plug-in and handles the main GUI.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
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
	    //adds "Select Directory" Label,Text field,and Button to pane1.
	    pane1.setBackground(Color.WHITE);
		    pane1.setLayout(new FlowLayout(FlowLayout.CENTER));
		    JLabel dirLabel = new JLabel("Select Directory : ");
		    dirLabel.setFont(new Font("Monospace", Font.PLAIN, 25));
		    dirLabel.setForeground(Color.BLACK);
		    pane1.add(dirLabel);
		    JTextField dirTextfield;
		    String workspace = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
	        dirTextfield = new JTextField(workspace);
	        dirTextfield.setPreferredSize(new Dimension(275,35));
	        pane1.add(dirTextfield);
	        JButton dirButton;
	        dirButton = new JButton("browse...");
	        dirButton.setPreferredSize(new Dimension(100,35));
	        dirButton.addActionListener(new ActionListener(){
		        public void actionPerformed(ActionEvent e){
		        	fileList(0, dirTextfield);
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
		        	fileList(1, logTextfield);
		        }
	        });
	        pane3.add(logButton);
	    //adds "Projected File Path" Label,Text field,and Button to pane1.
	    pane4.setBackground(Color.WHITE);
		    pane4.setLayout(new FlowLayout(FlowLayout.CENTER));
		    JLabel pfpLabel = new JLabel("Projected File Path : ");
		    pfpLabel.setFont(new Font("Monospace", Font.PLAIN, 20));
		    pfpLabel.setForeground(Color.BLACK);
		    pane4.add(pfpLabel);
		    JTextField pfpTextfield;
	        pfpTextfield = new JTextField("output file location");
	        pfpTextfield.setPreferredSize(new Dimension(275,35));
	        pane4.add(pfpTextfield);
	        JButton pfpButton;
	        pfpButton = new JButton("browse...");
	        pfpButton.setPreferredSize(new Dimension(100,35));
	        pfpButton.addActionListener(new ActionListener(){
		        public void actionPerformed(ActionEvent e){
		        	fileList(0, pfpTextfield);
		        }
	        });
	        pane4.add(pfpButton);
	    //adds "Create Projected File" Button to pane1.
	    pane5.setBackground(Color.WHITE);
	    	JButton goButton;
	        goButton = new JButton("Create Projected File");
	        goButton.setPreferredSize(new Dimension(300,35));
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
	    //plug-in return.
		return null;
	}
}
