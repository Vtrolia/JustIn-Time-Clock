import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.*;
import java.util.Date;

import javax.swing.*;

@SuppressWarnings("serial")
public class justInClientGUI extends gui {
	
	private static int employeenum = -1;
	
	public justInClientGUI() {
		super();
	}
	
	@Override
	public Component add(Component comp) {
		graphicsPanel.add(comp);
		return comp;
	}
	
	@Override
	public void add(Component comp, Object con) {
		graphicsPanel.add(comp, con);
	}
	
	public void refreshScreen() {
		graphicsPanel.revalidate();
		graphicsPanel.repaint();
	}
	
	
	public static void main(String[] args) {
		justInClientGUI g = new justInClientGUI();
		LoginWindow l = new LoginWindow(TITLE_FONT, WORD_FONT);
		g.add(l);
		l.continuouslyUpdateDate();
		g.resetScreen();
		OptionsWindow o = new OptionsWindow(TITLE_FONT, WORD_FONT, l.getIDString());
		o.setGreetingText("[Placeholder]");
		g.add(o);
		g.refreshScreen();
	}
	
	private void resetScreen() {
		graphicsPanel.removeAll();
		
	}
}


/**
 * This class contains the login screen for the client side GUI. Because the client GUI was designed to hold multiple
 * screens that change over time, each different screen is created as a separate JPanel class. This JPanel gives
 * the end user the ability to login to their own personal digital time sheet.
 * 
 * @author Vincent W. Trolia
 *
 */
@SuppressWarnings("serial") 
class LoginWindow extends JPanel implements ActionListener, KeyListener {

	/* Initially, store the entered ID number as a string to test validity */
	private String employeeID = null;
	
	/* The manipulations and responsiveness of the GUI come from the text field and button, so make this an iVar */
	private JTextField id;
	private JButton submit;
	
	/* The minimum length is stored in a JTextField object, but the maximum must be stored as a iVar */
	private int MAX_LENGTH = 10;
	
	/* The login screen will have a continuously updating clock, so now needs to be updated all the time */
	private Date now;
	private JLabel datetime;
	private boolean continueLooping = true;
	
	/* These are the colors that indicate to the user whether the ID they have entered is valid or not */
	private Color ERROR_COLOR = new Color(252, 96, 90);
	private Color GOOD_COLOR = new Color(182, 235, 139);
	
	
	/**
	 * This constructor sets up the text field with the default length and the fonts provided by the caller
	 * @param title: the font for the title section that says "Welcome to the JustIn Time Clock"
	 * @param words: the font for every other bit of text in the login screen
	 */
	public LoginWindow(Font title, Font words) {
		
		// basic setup, and make a call to the more complicated stuff. Of course, we want this panel to be consistent
		// with the look of the rest of the GUI
		setVisible(true);
		setLayout(new GridBagLayout());
		setBackground(Color.WHITE);
		createGUI(title, words);
	}
	
	/**
	 * This constructor takes additional parameters to set the desired length of the employee's ID#s.
	 * @param title: the font for the title section that says "Welcome to the JustIn Time Clock"
	 * @param words: the font for every other bit of text in the login screen
	 * @param min: the smallest length of a valid id (in characters), 5 = '12345'
	 * @param max: the longest allowed length of a valid id
	 */
	public LoginWindow(Font title, Font words, int min, int max) {
		setVisible(true);
		setLayout(new GridBagLayout());
		setBackground(Color.WHITE);
		createGUI(title, words);
		
		// for this constructor, call the public method
		setIDBounds(min, max);
	}
	
	
	/**
	 * A wrapper so that all initialization isn't done in the constructor, but it is called automatically. This
	 * function creates the login screen as it appears on the JFrame. It creates a title, the running time clock,
	 * and a form for the user to fill their id into. It also sets up listeners and allows for the more complicated
	 * functionality in the user editable components.
	 * @param title: the font for the title of the login screen
	 * @param words: the font for every other bit of text in the login screen
	 */
	private void createGUI(Font title, Font words) {
		
		// For GridBagLayout, we need a set of constraints to fully utilize the flexibility of this manager
		GridBagConstraints con = new GridBagConstraints();
		
		// The title needs to stick to the top of the screen
		JLabel welcome = new JLabel("Welcome to the JustIn Time Clock");
		welcome.setFont(title);
		con.anchor = GridBagConstraints.PAGE_START;
		con.ipady = 10;
		con.gridx = 1;
		con.gridy = 0;
		con.weighty = .05;
		add(welcome, con);
		
		// This is the label that holds the current time, it should be just a bit below the title
		now = new Date();
		datetime = new JLabel(now.toString().split(" ")[3]);
		datetime.setFont(words);
		con.anchor = GridBagConstraints.CENTER;
		con.gridx = 1;
		con.gridy = 1;
		con.weighty = 0;
		add(datetime, con);
		
		// Add a placeholder label for better spacing. Yeah, I know it's a bad practice, but it works so get off 
		// my back already, let's see you do better
		JLabel placeholder = new JLabel("");
		con.anchor = GridBagConstraints.CENTER;
		con.gridx = 1;
		con.gridy = 2;
		con.weighty = .2;
		add(placeholder, con);
		
		// Inform the user of what they need to type in the text box because background info matters 
		JLabel label = new JLabel("ID number: ");
		label.setFont(words);
		con.anchor = GridBagConstraints.LINE_START;
		con.gridx = 0;
		con.gridy = 3;
		con.weighty = 0;
		add(label, con);
		
		// now, add the TextField and its action listener, because it checks whether or not the id typed in is valid
		// or not
		id = new JTextField(10);
		id.setActionCommand("ENTER");
		id.addActionListener(this);
		id.addKeyListener(this);
		con.gridx = 1;
		con.fill = GridBagConstraints.BOTH;
		add(id, con);
		
		/* because a blank id is not valid, the button to submit the employee id number is not enabled by default,
		 * upon submitting, it is checked whether or not the id is valid and the user will either be redirected to
		 * the next screen or right back here if the id is not validated on the server end 
		 */
		submit = new JButton("Submit");
		submit.setEnabled(false);
		submit.addActionListener(this);
		con.gridx = 2;
		add(submit, con);
		
		// another placeholder so that the other elements aren't forced to the bottom of the screen.
		JLabel place = new JLabel("");
		con.gridy = 4;
		con.weighty = .3;
		add(place, con);
		
	}
	
	
	/**
	 * The timer needs to constantly update in order to have an updated time. DO NOT call any other methods after
	 * this, as this function will run until the GUI needs to change to another screen. It will forever update
	 * the text in the datetime JLabel to the current time in HH:MM:SS format
	 */
	public void continuouslyUpdateDate() {
		while (continueLooping) {
			now = new Date();
			datetime.setText(now.toString().split(" ")[3]);
		}
	}
	
	
	/**
	 * The action listener capability of this class only needs to listen for enter being pressed or the 
	 * submit button being pressed. If it does, then we stop the infinite looping time clock and then the
	 * calling class can get the string of the employeeID back.
	 * @note currently while in testing, it sets a placeholder label to show the number back to the user,
	 * this will be removed in production
	 * @param e an ActionEvent to check the source of
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		// if enter was pressed, check if it was a valid ID# first
		if ((e.getActionCommand().equals("ENTER") && submit.isEnabled()) || e.getSource().equals(submit)) {
			employeeID = id.getText();
			
			// end the updating timer loop so that the JFrame can move to the next screen
			continueLooping = false;
		}
	}

	
	/**
	 * For the key event, every key typed is going to be checked to give the user instant feedback for whether
	 * or not the ID they entered was valid or not. When it is valid, they are allowed to submit. This also
	 * sets tooltips to give users useful info on what is considered valid input
	 * @param e: a KeyeEvent
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		
		// if it is not the right size, tell the user that the ID needs to be of the length in the tooltip.
		// This also makes sure the submit button stays unclickable
		if (id.getText().length() < id.getColumns() - 1 || id.getText().length() > MAX_LENGTH) {
			id.setBackground(ERROR_COLOR);
			id.setToolTipText("You have to enter a valid ID between " + id.getColumns() + " and " +
							  MAX_LENGTH + " numbers long");
			
			submit.setEnabled(false);
			return;
		}
		
		// if the entry is of the correct length, it needs to be an integer. If not, the same warnings are given
		// to the user as the above.
		try {
			Integer.parseInt(id.getText());
			
			// let the user know that they can now sign in
			id.setBackground(GOOD_COLOR);
			id.setToolTipText(null);
			submit.setToolTipText("Click here to sign in");
			submit.setEnabled(true);
		}
		catch (NumberFormatException error) {
			id.setBackground(ERROR_COLOR);
			id.setToolTipText("You have to enter a valid ID between " + id.getColumns() + " and " +
					          MAX_LENGTH + " numbers long");
			
			submit.setEnabled(false);
		}
		
	}

	// we don't need these methods for this particular screen, so leave them blank
	@Override
	public void keyPressed(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}
	
	
	/**
	 * The only getter needed for this class, it gets the all important, valid user ID. If the user has not
	 * yet entered a valid ID#, this will return null.
	 * @return: a string if there is a valid ID# entered, else it returns null
	 */
	public String getIDString() {
		return employeeID;
	}
	
	
	/**
	 * To fit the needs of end user administrators, this allows the dynamic selection of ID number lengths.
	 * The minimum and maximum are setable for both ease of use and security, ensuring that invalid IDs are not
	 * even sent to the server
	 * @param min: the smallest length of a valid id (in characters), 5 = '12345'
	 * @param max: the longest allowed length of a valid id
	 */
	public void setIDBounds(int minimum, int maximum) {
		MAX_LENGTH = maximum;
		id.setColumns(minimum);
	}
		
}


/**
 * This class is the most basic out of all the JPanels in the GUI. The client side application gives the user
 * the choice to either view their previous time entries (how far back depends on the implementation of the server)
 * or to make a new entry. This JPanel is where the user makes the selection, then they are taken to the JPanel 
 * representing their choice.
 * 
 * @author Vincent W. Trolia
 *
 */
@SuppressWarnings("serial")
class OptionsWindow extends JPanel implements ActionListener {

	/* These are the fonts that the text will be */
	private Font TITLE_FONT;
	private Font WORD_FONT;
	
	/* In order to get the user's selection, there needs to be a iVar representing the radio buttons */
	private ButtonGroup selectStatus;
	
	/* this int will represent the user's choice for where they want to go next */
	private int choice = -1;
	
	
	/**
	 * Constructor. Builds the OptionsWindow without a name. This will have to be set later by the user in order
	 * for this JPanel to be visible. In this case, a placeholder is used to prepare this panel to appear
	 * 
	 * @param title: The font that will be used by the title, it greets the user with their name
	 * @param words: The font used by the prompts for the different choices the user has
	 */
	public OptionsWindow (Font title, Font words) {
		TITLE_FONT = title;
		WORD_FONT = words;
		
		// these are methods to cover up the ugliness of the constructor
		setUpScreen(null);
		drawScreen();
	}
	
	
	/**
	 * Constructor. This version will completely fill up the JPanel and set it visible. To be used only if there
	 * is a String representing the name of the person who is currently logged in. There is no need to call the
	 * setGreetingScreen() method after this. 
	 * 
	 * @param title: The font that will be used by the title, it greets the user with their name
	 * @param words: The font used by the prompts for the different choices the user has
	 * @param name: A string representing the employee's actual name, not the string version of their ID.
	 * @throws IllegalArgumentException if name is empty. Use the other constructor if there is no username for the
	 * current user. Handling of this exception is up to the calling class
	 */
	public OptionsWindow(Font title, Font words, String name) throws IllegalArgumentException {
		TITLE_FONT = title;
		WORD_FONT = words;
		setUpScreen(name);
		drawScreen();
	}
	
	/* This is the JLabel that will eventually personally greet the user. For now, make sure other methods can edit */
	private JLabel greetingScreen;
	
	
	/**
	 * This function does the neccesary steps for creating the new JPanel and making it look correct. If a constructor
	 * with no name is called, it sets the greeting title text to a placeholder and will not make the panel visible.
	 * If the constructor with a name is called, then just jump ahead and make it visible.
	 * @param name
	 * @throws IllegalArgumentException
	 */
	private void setUpScreen(String name) throws IllegalArgumentException {
		
		// keep style consistent across all JPanels representing the screens
		setBackground(Color.WHITE);
	    setLayout(new GridBagLayout());
		
		greetingScreen = new JLabel("");
		greetingScreen.setFont(TITLE_FONT);
		
		// call the private setGreetingText() method even if a name is supplied, in order to set the screen as visible.
		// If there is an exception, throw that all the way up to the class using this one.
		if (name == null) {
			setGreetingText("[NONE, AN ERROR HAS OCCURED]", false);
		}
		else {
			setGreetingText(name, true);
		}
		
	}
	
	
	private void drawScreen() {
		
		//	The greetingScreen text is created before this function and will change based on what user
		//	is currently logged in.
		GridBagConstraints con = new GridBagConstraints();
		con.anchor = GridBagConstraints.PAGE_START;
		con.gridx = 0;
		con.gridy = 0;
		con.ipady = 10;
		con.weightx = .6;
		con.weighty = .35;
		add(greetingScreen, con);
		
		//	I'm not proud of this, but this is the only way that the two labels look correct
		JLabel entry = new JLabel("            Make a new time entry:", JLabel.LEFT);
		entry.setFont(WORD_FONT);
		con.anchor = GridBagConstraints.CENTER;
		con.gridy = 2;
		con.ipadx = 200;
		con.weightx = .3;
		con.weighty = .05;
		add(entry, con);
		
		JLabel review = new JLabel("Review the latest time period: ", JLabel.LEFT);
		review.setFont(WORD_FONT);
		con.anchor = GridBagConstraints.CENTER;
		con.gridy = 4;
		add(review, con);
		
		
		//	represents the choice of making a new clock in or clock out. This will be the default choice
		JRadioButton entryButton = new JRadioButton();
		entryButton.setSelected(true);
		entryButton.setActionCommand("Entry");
		con.anchor = GridBagConstraints.LINE_END;
		con.gridy = 2;
		con.ipadx = 200;
		con.weightx = .1;
		add(entryButton, con);
		
		//	represents choosing to review the latest time period
		JRadioButton reviewButton = new JRadioButton();
		reviewButton.setActionCommand("Review");
		con.anchor = GridBagConstraints.LINE_END;
		con.gridy = 4;
		add(reviewButton, con);
		
		//	Radio buttons need to be put into a ButtonGroup to ensure only 1 is selected at a time
		selectStatus = new ButtonGroup();
		selectStatus.add(entryButton);
		selectStatus.add(reviewButton);
		
		//	the bottom button for users to confirm their choice
		JButton toBeContinued = new JButton("Continue");
		toBeContinued.addActionListener(this);
		con.anchor = GridBagConstraints.PAGE_END;
		con.gridy = 6;
		con.weighty = .55;
		con.ipadx = 0;
		add(toBeContinued, con);
	}
	
	
	private void setGreetingText(String name, boolean setVis) {
		if (name != null) {
			greetingScreen.setText("Hello, " + name + ", what would you like to do?");
			setVisible(setVis);
			return;
		}
		
		greetingScreen.setText("AN UNKNOWN ERROR HAS OCCURED");
		throw (new IllegalArgumentException("You cannot enter an empty String!"));
		
	}
	
	private void prepareForNextScreen() {
		String radioPressed = selectStatus.getSelection().getActionCommand();
		if (radioPressed.equals("Entry")) {
			this.choice = 1;
		}
		else {
			this.choice = 2;
		}
	}
	
	
	public void setGreetingText(String name) {
		if (name != null) {
			greetingScreen.setText("Hello, " + name + ", what would you like to do?");
			setVisible(true);
			return;
		}
		throw (new IllegalArgumentException("You cannot Enter an empty String!"));
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		prepareForNextScreen();
	}
	
	public int getChoice() {
		return this.choice;
	}
	
}