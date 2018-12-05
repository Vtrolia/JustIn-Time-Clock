import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.*;
import java.util.Date;
import java.util.HashMap;

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
		CardEntryScreen n = new CardEntryScreen(TITLE_FONT);
		LoginWindow w = new LoginWindow(TITLE_FONT, WORD_FONT);
		OptionsWindow o = new OptionsWindow(TITLE_FONT, WORD_FONT);
		
		g.add(w);
		w.continuouslyUpdateDate();
		w.getIDString();
		
		g.resetScreen();
		o.setGreetingText("Goblin Slayer");
		g.add(o);
		g.refreshScreen();
		
		while(o.getChoice() !=  1) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		g.resetScreen();
		g.add(n);
		g.refreshScreen();
		
		String[] p = {"Manager", "Loser", "QA Tester", "I'm already Tracer"};
		n.setPositions(p);
		
		String[] l = {"Here", "There", "Anywhere, really"};
		n.setLocations(l);
		n.continuouslyUpdate();
		System.out.println(n.getForm().toString());
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
	 * @param name: A string representing the employee's actual name, not the string version of their ID.
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
	
	
	/**
	 * This function draws all the objects on the screen and sets them all in place. In total, we have the title,
	 * the prompts for the buttons, and the buttons for where the user wants to go. This is the most simple JPanel
	 * in the gui, because there is only on thing it needs to do, so the layout is simple as a result.
	 */
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
	
	/**
	 * This is the private version of setGreetingText(), where the constructor can set the label visible or invisible
	 * based on which one was called. If an empty string gets passed, an exception is throw
	 * @param name: the user's name as a string
	 * @param setVis: a boolean representing whether or not the greeting text should be displayed or not. If not,
	 * the placeholder passed in by the constructor will be the value
	 */
	private void setGreetingText(String name, boolean setVis) {
		if (name != null) {
			greetingScreen.setText("Hello, " + name + ", what would you like to do?");
			setVisible(setVis);
			return;
		}
		
		// if the constructor passes a null string for some reason, this is an illegal argument, so it throws the
		// applicable exception
		greetingScreen.setText("AN UNKNOWN ERROR HAS OCCURED");
		throw (new IllegalArgumentException("You cannot enter an empty String!"));
		
	}
	
	
	/**
	 * This method was meant to be used as the inner method for both the ActionListener and the KeyListener component
	 * of this JPanel. I may still implement the KeyListener later, as the user should be allowed to select their
	 * choice by pressing the enter key. For now, this sets the choice to an integer representing which JPanel
	 * to display next
	 */
	private void prepareForNextScreen() {
		// get the name of the button the user selected
		String radioPressed = selectStatus.getSelection().getActionCommand();
		
		// in the JustInClientGUI, 0 represents login, 1 represents entry, 2 represents the time card review screen
		if (radioPressed.equals("Entry")) {
			this.choice = 1;
		}
		else {
			this.choice = 2;
		}
	}
	
	
	/**
	 * This is the public version to be used by the JustInClientGUI to allow the caller of this object to 
	 * set the name displayed as a greeting if it wasn't already set
	 * @param name: A string representing the employee's actual name, not the string version of their ID.
	 */
	public void setGreetingText(String name) {
		if (name != null) {
			greetingScreen.setText("Hello, " + name + ", what would you like to do?");
			setVisible(true);
			return;
		}
		
		// empty strings aren't allowed here
		throw (new IllegalArgumentException("You cannot Enter an empty String!"));
	}
	
	
	/**
	 * The only ActionListener is looking for a click of the continue button, so there is no need to handle special
	 * cases. Because the prepareForNextScreen() was meant to do the same thing for both an enter key press
	 * AND a click of the continue button, that method is the only thing this function needs to call.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		prepareForNextScreen();
	}
	
	
	/**
	 * The only getter in this class, it should be used to determine what the next screen should be after the user
	 * selects one. It returns -1 if the user has not made a choice yet.
	 * 
	 * @return an integer representing which JPanel to show the user next
	 */
	public int getChoice() {
		return this.choice;
	}
	
}


/**
 * This class allows the user to make time entries. They put in some metadata about where and what work they are 
 * doing then this information is stored form-style as a HashMap.
 * @author Vincent W. Trolia
 *
 */
@SuppressWarnings("serial")
class CardEntryScreen extends JPanel implements MouseListener {

	/* The server needs to be sent all the data for each entry, stored as a dictionary */
	private HashMap<String, String>EntryValues = null;
	
	/* The entry fields that need to be kept track of for later use */
	private JComboBox<String>[] fields;
	private JButton enter;
	
	/* The available positions and locations for entry will vary by business. Allows for multiple jobs/pay rates */
	private String[] positions = new String[0];
	private String[] locations = new String[0];
	
	/* Either initialize the CardEntryScreen with an ID#, or you can set it later */
	private int employeeID = 0;
	
	/* Just like the initial login screen, the entry card will display the time. However, this will not be
	 * an entry field added later, instead when the entry is made another call to the current time and date
	 * will be added to the EntryValues
	 */
	private Date clockStamp;
	private JLabel time;
	
	
	/**
	 * Constructor. This constructor takes in an ID if it's already supplied and converted into an integer.
	 * Otherwise it can be added later. 
	 * @param IDnumber: the numerical id of the employee making the time card entry
	 * @param Title: The font to be used for the text
	 */
	public CardEntryScreen (int IDnumber, Font Title) {
		employeeID = IDnumber;
		setFont(Title);
		createCard();
	}
	
	
	/**
	 * Constructor. This is the more basic version that only needs to take in a font.
	 * @param Title: The font to be used for the text
	 */
	public CardEntryScreen(Font Title) {
		setFont(Title);
		createCard();
	}
	
	
	/**
	 * This screen has a few selectable entries with a submit button and a running time clock. This function
	 * hides this from the constructor and sets up all the graphics. Like all the other panels, this has a 
	 * GridBagLayout
	 */
	private void createCard() {
		
		// make it consistent with the rest of the GUI
		setLayout(new GridBagLayout());
		setBackground(Color.WHITE);
		GridBagConstraints con = new GridBagConstraints();
		
		// set the title 
		JLabel title = new JLabel("Make a timecard entry:");
		title.setFont(this.getFont());
		con.anchor = GridBagConstraints.PAGE_START;
		con.gridx = 1;
		con.gridy = 0;
		con.ipady = 10;
		con.weighty = .05;
		con.weightx =.4;
		add(title, con);
		
		// The time is supposed to be a running clock, it starts here
		clockStamp = new Date();
		time = new JLabel(clockStamp.toString().split(" ")[3]);
		time.setFont(this.getFont());
		con.gridy = 1;
		add(time, con);
		con.weighty = .3;
		add(new JLabel(), con);
		
		// The important part of the GUI needs to be remembered, so it is stored in this array. Also, set up a 
		// combo box for the different job titles employees may have
		fields = new JComboBox[2];
		fields[0] = new JComboBox<String>(positions);
		con.weighty = .2;
		con.gridy = 3;
		add(fields[0], con);
		
		// add another combo box for the many different locations an employee may have
		fields[1] = new JComboBox<String>(locations);
		con.gridy = 4;
		con.weighty = .4;
		add(fields[1], con);
		
		enter = new JButton("Submit");
		enter.setEnabled(false);
		enter.addMouseListener(this);
		con.weighty = .1;
		con.gridy = 5;
		add(enter, con);
		
		// add labels for the combo boxes
		JLabel names = new JLabel("Select your position:");
		names.setFont(new Font("Rockwell", Font.PLAIN, 16));
		con.anchor = GridBagConstraints.NORTH;
		con.gridx = 0;
		con.gridy = 3;
		con.weighty = .2;
		con.weightx = .1;
		add(names, con);
		
		// a placeholder label to center the important components
		con.gridx = 2;
		con.weightx = .3;
		add(new JLabel("                     "), con);
		
		JLabel places = new JLabel("Select your location:");
		places.setFont(new Font("Rockwell", Font.PLAIN, 16));
		con.gridx = 0;
		con.gridy = 4;
		con.weightx = .1;
		add(places, con);
		
	}
	
	
	/**
	 * This window is similar to the LoginWindow screen, with it having an active time clock constantly running
	 * in order to give the employee a good idea of the exact time they are entering, more accurate than just down
	 * to hour and minutes. This clock stops when a valid entry is made
	 */
	public void continuouslyUpdate() {
		// check each turn if there is a valid entry, so that the button can be clicked
		boolean enable = true;
		
		while (EntryValues == null) {
			// continuously update to the new date
			clockStamp = new Date();
			time.setText(clockStamp.toString().split(" ")[3]);
			
			// if any of the fields are not selected, the entry is not valid, so the end user is not allowed to
			// click the submit button
			for (int i = 0; i < fields.length; i ++) {
				if (fields[i].getSelectedItem() == null)
				{
					enable = false;
					break;
				}
			}
			
			// if there was a valid entry, then it's okay for the button to be clickable
			if (enable) {
				enter.setEnabled(enable);
			}
			
			enable = true;
			
		}
	}
	
	
	/**
	 * The public interface for adding the possible jobs for either just the user or the company, depending on
	 * the implementation later
	 * @param pos: the string array of the possible job titles
	 */
	public void setPositions(String[] pos) {
		positions = pos;
		
		// add each possible position to the combo box
		for (int i = 0; i < pos.length; i++) {
			fields[0].addItem(pos[i]);
		}
	}
	
	
	/**
	 * The public interface for adding the locations the user may be working at.
	 * @param locs: the string array of possible locations
	 */
	public void setLocations(String [] locs) {
		locations = locs;
		
		// add each possible location to the combo box
		for (int i = 0; i < locs.length; i++) {
			fields[1].addItem(locs[i]);
		}
	}
	
	
	/**
	 * If the id wasn't supplied by the constructor, it can be set here
	 * @param id: the id of the current user
	 */
	public void setId(int id) {
		employeeID = id;
	}
	
	
	/**
	 * Public getter that returns a copy of the entry form filled out by the end user to be sent to the server
	 * later on. There is a suppressed unchecked warning, but only because the checking works differently than
	 * what would normally be required for an object cast. The object is either a HashMap or null, the only code
	 * that touches this HashMap will set it to one of the two.
	 * @return: A cloned copy of the EntryValues HashMap
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, String> getForm() {
		if (EntryValues == null) {
			return null;
		}
		
		else {
			return (HashMap<String, String>) EntryValues.clone();
		}
	}
	
	
	/**
	 * When the mouse is clicked, if there is a valid entry from the user, the button will be clicked. When the
	 * button is clicked, the data is sent as a form and gettable in its getter.
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if (enter.isEnabled()) {
			
			// The relevant values for the form are the date & time, the employee's id, their position, and
			// the location they are currently working at
			EntryValues = new HashMap<String, String>();
			EntryValues.put("datetime", new Date().toString());
			EntryValues.put("id", Integer.toString(employeeID));
			EntryValues.put("position", fields[0].getSelectedItem().toString());
			EntryValues.put("location", fields[1].getSelectedItem().toString());
		}
		
	}

	// unused, not needed methods
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	
}