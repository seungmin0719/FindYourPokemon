/* Name: Riley Zhou and Christine Baek
   Date: January 24, 2017
   Description: This program is a single-player game that the player opens the pokeballs by clicking to
   				finds the two same pokemons inside the balls. Two balls can stay open at the same time. 
   				If the pokemons inside of the two balls are the same, the balls stay open; otherwise, they
   				would be closed while clicking on another ball. If all the balls are opened within the time
   				limits, the player goes to next level until Level 5, the final level.
*/

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import sun.audio.*;

public class FindYourPokemon extends JPanel implements ActionListener, MouseListener, KeyListener
{
	static JFrame frame;

	Image startpage, background, ball, mouse, timerBG;
	Image[] level;
	Image[] pokemon;
	Image[] pokeball;
	Image[] open;
	
	int[] pokemonNum = new int[10];
	int[][] pokeballs = new int[4][7] ;
	int[][] pokemons = new int[4][7];
	int row1, row2, column1, column2;
	int row = 3, column = 6;
	int[] co = new int[2];
	int[] ro = new int[2];
	int countOpen = 0;
	int[] count = new int[5];
	int[] total = {10,12,18,20,24};
	int currentLevel = 0;
	
	
	int BORDER_SIZE = 150, BALL_SIZE = 150, TOP_OFFSET = 200;
	int music = 1;

	boolean start = true;
	boolean[] click = new boolean[5];
	boolean openTheBall = false;
	boolean nextLevel = false;
	boolean gameStart = false;
	

	boolean timerOn = false;
	int time = 0;
	int[] timeAllowed = {300,500,800,1500,2000};


	Image offScreenImage;
	Graphics offScreenBuffer;

	AudioClip backGroundSound, backGroundSound2;
	JLabel label;
	
	public FindYourPokemon()
	{ 

		addMouseListener (this);

		timerBG = Toolkit.getDefaultToolkit().getImage("timer.png");

		mouse = Toolkit.getDefaultToolkit().getImage("cursor.png");
		Point hotspot = new Point (0, 0);
		Toolkit toolkit = Toolkit.getDefaultToolkit ();
		Cursor cursor = toolkit.createCustomCursor (mouse, hotspot, "smile");

		level = new Image [5];
		for (int imageNo = 0 ; imageNo < 5 ; imageNo++)
		{
			String imageFileName = "level" + (imageNo+1) + ".jpg";
			level [imageNo]= Toolkit.getDefaultToolkit().getImage(imageFileName);
		}

		pokeball = new Image [5];
		for (int imageNo = 0 ; imageNo < 5 ; imageNo++)
		{
			String imageFileName = "cp" + (imageNo+1) + ".gif";
			pokeball [imageNo]= Toolkit.getDefaultToolkit().getImage(imageFileName);
		}

		pokemon = new Image [12];
		for (int imageNo = 0 ; imageNo < 12 ; imageNo++)
		{
			String imageFileName = "p" + (imageNo+1) + ".png";
			pokemon [imageNo]= Toolkit.getDefaultToolkit().getImage(imageFileName);
		}
		open = new Image [5];

		for (int imageNo = 0 ; imageNo < 5 ; imageNo++)
		{
			String imageFileName = "op" + (imageNo+1) + ".gif";
			open [imageNo]= Toolkit.getDefaultToolkit().getImage(imageFileName);
		}

		background = Toolkit.getDefaultToolkit().getImage("background.jpg");
		startpage = Toolkit.getDefaultToolkit().getImage("startpage.gif");
		setPreferredSize (new Dimension (969, 606));	

		for(int r = 1; r<3;r++){
			for(int c = 1; c<6;c++){
				pokeballs[r][c] = -1;
			}
		}

		backGroundSound = Applet.newAudioClip (getCompleteURL ("gameBGM.wav"));
		backGroundSound2 = Applet.newAudioClip (getCompleteURL ("gameBGM2.wav"));

		// Set up the Menu
		// Set up the MenuItems
		JMenu gameMenu, levelMenu, helpMenu, settingMenu;
		JMenuItem beginnerOption, intermediateOption, advancedOption, aboutOption;
		JMenuItem newOption, exitOption, backgroundOption;


		// Set up the Game Menu
		gameMenu = new JMenu ("Game");
		newOption = new JMenuItem ("New");
		exitOption = new JMenuItem ("Exit");
		settingMenu = new JMenu ("Setting");

		// Set up the Help Menu
		aboutOption = new JMenuItem ("About...");
		helpMenu = new JMenu ("Help");
		backgroundOption = new JMenuItem ("Change background sound");

		// Add each MenuItem to the Game Menu (with a separator)
		gameMenu.add (newOption);
		gameMenu.addSeparator ();
		gameMenu.add (exitOption);
		helpMenu.add (aboutOption);
		settingMenu.add(backgroundOption);


		JMenuBar mainMenu = new JMenuBar ();
		mainMenu.add (gameMenu);
		mainMenu.add (helpMenu);
		mainMenu.add(settingMenu);

		// Set the menu bar for this frame to mainMenu
		frame.setJMenuBar (mainMenu);

		// Set up the icon image (Tracker not needed for the icon image)
		Image iconImage = Toolkit.getDefaultToolkit ().getImage ("p1.gif");
		frame.setIconImage (iconImage);

		// Start a new game and then make the window visible
		newGame ();

		newOption.setActionCommand ("New");
		newOption.addActionListener (this);
		exitOption.setActionCommand ("Exit");
		exitOption.addActionListener (this);
		aboutOption.setActionCommand ("About");
		aboutOption.addActionListener(this);
		backgroundOption.setActionCommand ("Change background sound");
		backgroundOption.addActionListener(this);


		setFocusable (true); 
		addKeyListener (this);
    	addMouseListener (this);

		frame.setCursor (cursor);

		backGroundSound.loop ();

	} // Constructor

	public URL getCompleteURL (String fileName)
	{
		try
		{
			return new URL ("file:" + System.getProperty ("user.dir") + "/" + fileName);
		}
		catch (MalformedURLException e)
		{
			System.err.println (e.getMessage ());
		}
		return null;
	}

	// To handle normal menu items
	public void actionPerformed (ActionEvent event)
	{

		String eventName = event.getActionCommand ();
		if (eventName.equals ("New"))
		{
			newGame ();
		}
		if (eventName.equals ("Exit"))
		{
			System.exit (0);
		}
		if (eventName.equals ("About"))
			JOptionPane.showMessageDialog (frame, (Object) "Click two balls and check if the pokemons are the same. \nIf the pokemons are different, "
					+ "open another ball while closing the former two balls. \nIf the pokemons are same, the balls remain open.\n"
					+ "Once all the balls are opened within the time limit, you can enter the next level.\n"
					+ "There will be five levels to complete.", "About...", JOptionPane.INFORMATION_MESSAGE);			
		if (eventName.equals ("Change background sound"))
		{
			if (music == 1) {
				backGroundSound.stop ();
				backGroundSound2.loop ();
				music++;
			}
			else if (music == 2) {
				backGroundSound2.stop ();
				backGroundSound.loop ();
				music--;			
			}
		}
	}

	 /*description: the method starts a new game from level1
     * parameter: -
     * return: -
     */
	public void newGame ()
	{
		timerOn = false;
		start = true;
		click = new boolean[5];
		pokemonNum = new int[10];
		currentLevel = 0;
		count = new int[5];
		row = 3;
		column = 6;
		BORDER_SIZE = 150;
		BALL_SIZE = 150;
		TOP_OFFSET = 200;
		pokeballs = new int[4][7];
		for(int r = 1; r<3;r++){
			for(int c = 1; c<6;c++){
				pokeballs[r][c] = -1;
			}
		}
		pokemons = new int[4][7];
		time = 0;
		gameStart = false;
		repaint ();
	}

	 /*description: the method starts a new level
     * parameter: -
     * return: -
     */
	public void newLevel ()
	{
		timerOn = false;
		currentLevel++;
		System.out.println(currentLevel);
		click[currentLevel]=false;
		start = false;
		pokemonNum = new int[total[currentLevel]];
		pokeballs = new int[row+1][column+1];
		for(int r = 1; r<row;r++){
			for(int c = 1; c<column;c++){
				pokeballs[r][c] = -1;
			}
		}
		pokemons = new int[row+1][column+1];
		time = 0;
		repaint ();
	}

	 /*description: the method checks if the current level is completed and change the values for next level if qualified.
     * parameter: -
     * return: It returns a boolean to determine whether the play could go on to next level.
     */
	public boolean checkLevel ()
	{
		boolean next = false;
		if(count[currentLevel]==total[currentLevel]){
			next = true;
			timerOn = false;
			if(currentLevel == 0){
				row = 3;
				column = 7;
				BORDER_SIZE = 80;
				TOP_OFFSET = 200;
			}
			else if(currentLevel == 1){
				row = 4;
				column = 7;
				BORDER_SIZE = 50;
				TOP_OFFSET = 130;
			}
			else if(currentLevel == 2){
				row = 5;
				column = 6;
				BORDER_SIZE = 150;
				TOP_OFFSET = 30;
				BALL_SIZE = 140;
			}
			else if(currentLevel == 3){
				row = 5;
				column = 7;
				BORDER_SIZE = 120;
				TOP_OFFSET = 10;
				BALL_SIZE = 145;
			}

		}
		if(currentLevel == 4 && count[currentLevel]==total[currentLevel]){
			timerOn = false;
			 int n =JOptionPane.showConfirmDialog(
					null,
					"Congratulations! Play again? ",
					"Win",
					JOptionPane.YES_NO_OPTION);

			if(n == JOptionPane.YES_OPTION){
				newGame();
			}
			else {
				JOptionPane.showMessageDialog(null, "GOODBYE");
				System.exit (0);
			}
		}
		return next;
	}

	 /*description: the method checks if the two pokemons are the same. If they are not, the balls would be change back to closed.
     * parameter: -
     * return: -
     */
	public void result()
	{
		if(pokeballs[row1][column1] != pokeballs[row2][column2]){
			pokeballs[row1][column1] = -1;
			pokeballs[row2][column2] = -1;
		}
		ro[1] = 0;
		co[1] = 0;

	}

	 /*description: The method assigns the locations of pokemons.
     * parameter: -
     * return: -
     */
	public void assignPokemon(){

		for(int i = 1; i<row; i++){
			for(int j = 1; j<column; j++){
				do{
					pokemons[i][j] = (int) Math.round(Math.random()*(total[currentLevel]/2-1));
					pokemonNum[pokemons[i][j]]++;
				}
				while(pokemonNum[pokemons[i][j]]>2);
			}
		}
	}

	// MouseListener methods
	public void mouseClicked (MouseEvent e)
	{
		int x, y;
		x = e.getX ();
		y = e.getY ();

		if(start==false && click[currentLevel] == false && x>180 && x<700 && y>200 && y<430){
			timerOn = false;
			timerOn = true;
			timeCount timer = new timeCount ();
			timer.start ();
			click[currentLevel] = true;
			assignPokemon();
			gameStart = false;
			time = 0;
		}
		else if(start==false && click[currentLevel] == true && gameStart == true){
			if(x>BORDER_SIZE && y>TOP_OFFSET)
				handleAction (x, y);
		}
		repaint();
	}


	public void mouseReleased (MouseEvent e)
	{
	}


	public void mouseEntered (MouseEvent e)
	{
	}


	public void mouseExited (MouseEvent e)
	{
	}


	public void mousePressed (MouseEvent e)
	{
	}


	//KeyListener methods
	public void keyPressed (KeyEvent kp)
	{

		if (kp.getKeyCode() == KeyEvent.VK_ENTER && start != false){
			start = false;
		}
		else if (kp.getKeyCode() != KeyEvent.VK_ENTER && start != false){
			start = false;
		}
		repaint();
	}


	public void keyReleased (KeyEvent e)
	{
	}


	public void keyTyped (KeyEvent e)
	{
	}

	 /*description: the method determines the rows and columns of the clicked balls.
     * parameter: the x coordinate, y coordinate
     * return: -
     */
	public void handleAction (int x, int y)
	{

		co[countOpen] = (x - BORDER_SIZE) / BALL_SIZE + 1;
		ro[countOpen] = (y - TOP_OFFSET) / BALL_SIZE + 1;

		if((countOpen == 0 && pokeballs[ro[0]][co[0]]==-1)){
			result();
			pokeballs[ro[0]][co[0]] = pokemons[ro[0]][co[0]];
			row1 = ro[0];
			column1 = co[0];
			countOpen++;
		}

		if(countOpen == 1 && pokeballs[ro[1]][co[1]]==-1){
			co[1] = (x - BORDER_SIZE) / BALL_SIZE + 1;
			ro[1] = (y - TOP_OFFSET) / BALL_SIZE + 1;
			pokeballs[ro[1]][co[1]] = pokemons[ro[1]][co[1]];
			row2 = ro[1];
			column2 = co[1];
			countOpen=0;
			for(int i = 1; i<row; i++){
				for(int j = 1; j<column; j++){
					if(pokeballs[i][j]!=-1){
						count[currentLevel]++;
					};
				}
			}
			nextLevel = checkLevel();
			if(nextLevel == true && count[currentLevel] != 0){
				newLevel();
				timerOn = false;
			}
			
			else{
				count[currentLevel] = 0;
			}
		}
	}
	
	public void update (Graphics g)
	{
		paint (g);
	}


	public void paintComponent (Graphics g)
	{
		super.paintComponent(g);
		if (offScreenBuffer == null)
		{
			offScreenImage = createImage (this.getWidth(), this.getHeight ());
			offScreenBuffer = offScreenImage.getGraphics ();
		}
		if(start){
			offScreenBuffer.drawImage (startpage,0,0,this);
		}
		else if (start == false && click[currentLevel] == false ){
			timerOn = false;
			offScreenBuffer.drawImage(level[currentLevel],0,0,this);
		}
		else if(click[currentLevel] == true){
			gameStart = true;
			offScreenBuffer.drawImage(background, 0,0,this);
			offScreenBuffer.drawImage(timerBG, 10, 8, this);
			offScreenBuffer.setColor(Color.WHITE);
			offScreenBuffer.setFont(new Font("Gill Sans", Font.BOLD, 20)); 			
			offScreenBuffer.drawString ("Time:" + (time / 10.0), 15, 30);

			for (int r = 1 ; r <row ; r++){
				for (int c = 1 ; c < column ; c++)
				{
					int xPos = (c - 1) * BALL_SIZE + BORDER_SIZE;
					int yPos = TOP_OFFSET + (r-1) * BALL_SIZE;
					if(pokeballs[r][c]==-1)
						offScreenBuffer.drawImage(pokeball[currentLevel], xPos, yPos, this);
					else{
						offScreenBuffer.drawImage(open[currentLevel], xPos, yPos, this);
						offScreenBuffer.drawImage(pokemon[pokemons[r][c]], xPos, yPos, this);
					}
				}
			}
		}
		g.drawImage (offScreenImage, 0, 0, this);
	}


    //Timer in threading
	public class timeCount extends Thread 
	{
		public void run ()
		{

			while (timerOn == true) 
			{

				if (time >= timeAllowed[currentLevel] && timerOn == true)
				{
					
					JOptionPane.showMessageDialog (FindYourPokemon.this,
							"Time is Up!", "Timer", JOptionPane.INFORMATION_MESSAGE);
					int n = JOptionPane.showConfirmDialog(
							null,
							"Time is Up! Restart?",
							"Restart",
							JOptionPane.YES_NO_OPTION);
					repaint (60, 10, 50, 20);
					
					if(n == JOptionPane.YES_OPTION){
						newGame();
					}
					else if ( n==JOptionPane.NO_OPTION){
						System.exit (0);
					}
					timerOn = false;
				}
				else if(time<timeAllowed[currentLevel])
				{
					// Increment the time (you could also count down)
					time++;
					// Repaint area around timer display only
					repaint (60, 10, 50, 20);
				}
				try
				{
					sleep (100);
				}
				catch (Exception e)
				{
				}
			}
		}
	}


	public static void main (String[] args)
	{
		frame = new JFrame ("FindYourPokemon");
		FindYourPokemon myPanel = new FindYourPokemon();

		frame.add (myPanel);
		frame.pack ();
		frame.setVisible (true);



	} // main method

}




