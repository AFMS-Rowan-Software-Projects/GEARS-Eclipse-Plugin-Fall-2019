package org.plugin.gears.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.*;
/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class Handler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public Handler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
		//GUI
		JFrame frame = new JFrame("GEARS interface");
	    //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setSize(620,500);
	    //Below Layout aligns added panels vertically in the GUI
	    frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
	    //Below creates placeholder panels for GUI items
	    JPanel pane1 = new JPanel();
	    JPanel pane2 = new JPanel();
	    JPanel pane3 = new JPanel();
	    JPanel pane4 = new JPanel();
	    JPanel pane5 = new JPanel();
	    JPanel pane6 = new JPanel();
	    //Below adds Label,Text field,and Button to pane1.
	    pane1.setBackground(Color.WHITE);
		    pane1.setLayout(new FlowLayout(FlowLayout.CENTER));
		    JLabel dirLabel = new JLabel("Select Directory : ");
		    dirLabel.setFont(new Font("Monospace", Font.PLAIN, 25));
		    dirLabel.setForeground(Color.BLACK);
		    pane1.add(dirLabel);
		    
		    JTextField dirTextfield;
		    //testing directory fetching
		    String currentWorkingDir = System.getProperty("user.dir");
	        dirTextfield = new JTextField(currentWorkingDir);
	        dirTextfield.setPreferredSize(new Dimension(275,35));
	        pane1.add(dirTextfield);
	
	        JButton dirButton;
	        dirButton = new JButton("browse...");
	        dirButton.setPreferredSize(new Dimension(100,35));
	        pane1.add(dirButton);
	    
	    pane2.setBackground(Color.WHITE);
		    pane2.setLayout(new FlowLayout(FlowLayout.CENTER));
		    JLabel varLabel = new JLabel("Variant File : ");
		    varLabel.setFont(new Font("Monospace", Font.PLAIN, 25));
		    varLabel.setForeground(Color.BLACK);
		    pane2.add(varLabel);
		    
		    JTextField varTextfield;
	        varTextfield = new JTextField("variant file here");
	        varTextfield.setPreferredSize(new Dimension(275,35));
	        pane2.add(varTextfield);
	
	        JButton varButton;
	        varButton = new JButton("browse...");
	        varButton.setPreferredSize(new Dimension(100,35));
	        pane2.add(varButton);
        
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
	        pane3.add(logButton);
        
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
	        pane4.add(pfpButton);
        
	    pane5.setBackground(Color.WHITE);
	    	JButton goButton;
	        goButton = new JButton("Create Projected File");
	        goButton.setPreferredSize(new Dimension(300,35));
	        pane5.add(goButton);
	        
	    pane6.setBackground(Color.WHITE);
		    JButton showButton;
	        showButton = new JButton("View Projected File");
	        showButton.setPreferredSize(new Dimension(300,35));
	        pane6.add(showButton);
        
	    frame.add(pane1);
	    frame.add(pane2);
	    frame.add(pane3);
	    frame.add(pane4);
	    frame.add(pane5);
	    frame.add(pane6);
	    frame.setVisible(true);
	       
		return null;
	}
}
