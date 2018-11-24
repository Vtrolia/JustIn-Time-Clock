import javax.imageio.ImageIO;
import javax.swing.*;

import java.io.File;
import java.io.IOException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;


/**
 * This is the base class that both GUIs for both the client end and the server end of the JustIn Time Clock
 * will be based off of. It creates the window and sets up the unimportant things to get it started. This will
 * be the framework used to create modular, reusable classes for the entire GUI to be more dynamic and be ever
 * shifting as functionality is added.
 * 
 * @author Vincent W. Trolia
 * @version 1.0
 * @see justInClientGUI, justInServerGUI
 *
 */
@SuppressWarnings("serial")
public class gui extends JPanel {

	/* We want to hold the constants for the size of our GUI as iVars */
	private static final int WINDOW_WIDTH = 800;
	private static final int WINDOW_HEIGHT = 600;
	
	/* fonts to be used for both the headers of pages, and then the content of the pages themselves */
	protected static final Font TITLE_FONT = new Font("Rockwell", Font.PLAIN, 22);
	protected static final Font WORD_FONT = new Font("Rockwell", Font.PLAIN, 16);
	
	/* The building blocks of the GUI to be used later */
	protected final JFrame framing;
	protected final JPanel graphicsPanel;
	
	/* Initially the values for the smallest and lowest ID numbers are arbitrary, but the user can set them */ 
	private int ID_MINLENGTH = 10;
	private int ID_MAXLENGTH = 64;
	
	
	/**
	 * The constructor sets up the JFrame window to what the basic look will be, and how the JPanel will
	 * be implemented. This is meant to be a parent class where both the client and server GUIs are based
	 */
	public gui() {
		framing = new JFrame("Justin Time Clock");
		graphicsPanel = new JPanel(new BorderLayout());
		graphicsPanel.setBackground(Color.white);
		graphicsPanel.setVisible(true);
		initWindow();
	}
	
	
	/**
	 * This method is a basic encapsulation of all the necessary setup for the GUI window (aka JFrame). It sets
	 * the size, the icon and all the other boring, typical steps expected with making up the frame.
	 */
	private void initWindow() {
		
		// basic JFrame setup for the 800x600 window
		framing.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		framing.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		framing.setLocationRelativeTo(null);
		framing.add(graphicsPanel);
		
		//	if for some reason the icon can't be loaded, don't even bother trying to find something else
		try {
			framing.setIconImage(ImageIO.read(new File("clock-image.png")));
		} catch (IOException e) {}
		framing.setVisible(true);
		framing.setBackground(Color.WHITE);
	}
	
	// Getter and Setter section
	
	
	/**
	 * Get the width of the window
	 * @return an int representing the width of the window in pixels
	 */
	public int getWidth() {
		return WINDOW_WIDTH;
	}
	
	/**
	 * Get the height of the window
	 * @return an int representing the height of the window in pixels
	 */
	public int getHeight() {
		return WINDOW_HEIGHT;
	}
	
	/**
	 * For purpose of making the GUI more dynamic, the min and max ID lengths are to be set by an administrator.
	 * The GUI will in turn react to whether of not the ID appears to be valid or not. That is why we have these
	 * getters
	 * @return an int that sets the minimum required length of an employee ID
	 */
	public int getMinID() {
		return ID_MINLENGTH;
	}
	
	/**
	 * For purpose of making the GUI more dynamic, the min and max ID lengths are to be set by an administrator.
	 * The GUI will in turn react to whether of not the ID appears to be valid or not. That is why we have these
	 * getters
	 * @return an int that sets the maximum allowed length of an employee ID
	 */
	public int getMaxID() {
		return ID_MAXLENGTH;
	}
	
	/**
	 * This gets the font used by the titles of all screens on the Client/server GUIs. Because these Font objects
	 * are not to be edited, so the pointer to the original is not passed back
	 * @return a cloned font that is identical to the TITLE_FONT in a new Font object
	 */
	public Font getTitleFont() {
		return new Font("Rockwell", Font.PLAIN, 22);
	}
	
	/**
	 * This gets the font used by all other areas of the GUI, and the general font used by the components that
	 * aren't the title. Like the TITLE_FONT, the original font is not meant to be edited so a pointer to a new 
	 * Font is returned
	 * @return a cloned font that is identical to the TITLE_FONT in a new Font object
	 */
	public Font getWordFont() {
		return new Font("Rockwell", Font.PLAIN, 16);
	}

	/**
	 * ID length is a dynamic variable that will change based on how the end user implements employee IDing,
	 * so the minimum length will be editable
	 * @param updatedLength, an integer that administrators want to use as the smallest employeeID
	 */
	public void setMinID(int updatedLength) {
		ID_MINLENGTH = updatedLength;
	}
	
	/**
	 * ID length is a dynamic variable that will change based on how the end user implements employee IDing,
	 * so the maximum length will be editable
	 * @param longestLength, and integer that will represent the largest employeeID that administrators will allow
	 */
	public void setMaxID(int longestLength) {
		ID_MAXLENGTH = longestLength;
	}
}