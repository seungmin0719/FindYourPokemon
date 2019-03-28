/* Name: Riley Zhou and Christine Baek
   Date: January 24, 2017
   Description: This program is a single-player game that the player opens the pokeballs by clicking and
   				finds the two same pokemons inside the balls.
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

//import timer.TimerEventHandler;

public class test2 extends JPanel implements ActionListener, MouseListener, KeyListener
{
	static JFrame frame;

	Image startpage, background, ball, mouse, timerBG, background2;
	Image[] level;
	Image[] pokemon;
	Image[] pokeball;
	Image[] open;
	int[] pokemonNum = new int[12];
	int[][] pokeballs = new int[4][7];
	int[][] pokemons = new int[4][7];
	int row = 3, column = 6;
	int[] co = new int[2];
	int[] ro = new int[2];
	int[] poke = new int[2];
	int countOpen = 0;
	int[] count = new int[5];
	int[] total = {10,12,28,20,24};
	int music = 1;

	int BORDER_SIZE = 100, BALL_SIZE = 150, TOP_OFFSET = 100;
	boolean start = true;
	boolean[] click = new boolean[5];
	boolean openTheBall = false;
	boolean nextLevel = false;
	boolean close = false;
	boolean gameStart = false;
	int currentLevel = 0;

	Timer timer;
	boolean timerOn = false;
	int time = 0;
	int[] timeAllowed = {100,400,500,600,700};

	JPanel myPanel;
	AudioClip backGroundSound, backGroundSound2;
	JLabel label;

	Image offScreenImage;
	Graphics offScreenBuffer;

	boolean win;

	public test2 ()
	{
//		timer = new Timer (100, new TimerEventHandler ());		

		addMouseListener (this);

//		timerBG = Toolkit.getDefaultToolkit().getImage("timer.png");

		mouse = Toolkit.getDefaultToolkit().getImage("cursor.png");
		Point hotspot = new Point (0, 0);
		Toolkit toolkit = Toolkit.getDefaultToolkit ();
		Cursor cursor = toolkit.createCustomCursor (mouse, hotspot, "smile");

		backGroundSound = Applet.newAudioClip (getCompleteURL ("gameBGM.wav"));
		backGroundSound2 = Applet.newAudioClip (getCompleteURL ("gameBGM2.wav"));

		level = new Image [5];
		for (int imageNo = 0 ; imageNo < 5 ; imageNo++)
		{
			String imageFileName = "level" + (imageNo+1) + ".jpg";
			level [imageNo]= Toolkit.getDefaultToolkit().getImage(imageFileName);
		}


		pokeball = new Image [5];
		for (int imageNo = 0 ; imageNo < 5 ; imageNo++)
		{
			String imageFileName = "cp" + 1 + ".gif";
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

		ball = pokeball[currentLevel];

		background = Toolkit.getDefaultToolkit().getImage("background.jpg");
		startpage = Toolkit.getDefaultToolkit().getImage("startpage.gif");
		setPreferredSize (new Dimension (969, 606));	

		pokeballs = new int [4] [7];

		// Set up the Menu
		// Set up the MenuItems
		JMenu gameMenu, levelMenu, helpMenu, settingMenu;
		JMenuItem beginnerOption, intermediateOption, advancedOption, aboutOption;
		JMenuItem newOption, exitOption, backgroundOption;

		// Set up the Game Menu
		gameMenu = new JMenu ("Game");
		newOption = new JMenuItem ("New");
		exitOption = new JMenuItem ("Exit");

		// Set up the Help Menu
		helpMenu = new JMenu ("Help");
		aboutOption = new JMenuItem ("About...");

		// Set up the setting Menu
		settingMenu = new JMenu ("Setting");
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


		setFocusable (true); // Need this to set the focus to the panel in order to add the keyListener
		addKeyListener (this);

		addMouseListener (this);

		frame.setCursor (cursor);

		backGroundSound.play ();
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

	private class TimerEventHandler implements ActionListener
	{
		// The following method is called each time a timer event is
		// generated (every 100 milliseconds in this example)
		// Put your code here that handles this event
		public void actionPerformed (ActionEvent event)
		{
			// Time is up!
			if (time >= timeAllowed[currentLevel] && timerOn)
			{
				timerOn = false;
				timer.stop ();
				JOptionPane.showMessageDialog (test2.this,
						"Time is Up! Press NEW button from the menu"
								+ " to restart", "Timer", JOptionPane.INFORMATION_MESSAGE);
			}
			else
			{
				// Increment the time (you could also count down)
				time++;

				// Repaint area around timer display only
				repaint (130, 80, 50, 20);
			}
		}
	}


	// To handle normal menu items
	public void actionPerformed (ActionEvent event)
	{
		String eventName = event.getActionCommand ();

		if (eventName.equals ("New"))
		{
			newGame ();
		}
		else if (eventName.equals ("Exit"))
		{
			System.exit (0);
		}

		if (eventName.equals ("About"))
			JOptionPane.showMessageDialog (frame, (Object) "The object of the game is to get 4 in a row horizontally, vertically or diagonally. ", "About...", JOptionPane.INFORMATION_MESSAGE);			

		if (eventName.equals ("Change background sound"))
		{
			if (music == 1) {
				backGroundSound.stop ();
				backGroundSound2.play ();
				backGroundSound2.loop ();
				music++;
			}
			else if (music == 2) {
				backGroundSound2.stop ();
				backGroundSound.play ();
				backGroundSound.loop ();
				music--;			
			}

		}

	}

	public void newGame ()
	{
		start = true;
		click = new boolean[5];
		row = 3;
		column = 6;
		pokeballs = new int[4][7];
		pokemons = new int[4][7];
		time = 0;
		repaint ();
	}

	public void newLevel ()
	{
		start = true;
		click = new boolean[5];
		currentLevel++;
		pokeballs = new int[row+1][column+1];
		repaint ();
	}

	public boolean checkLevel (int currentLevel)
	{
		boolean next = false;
		if(time< timeAllowed[currentLevel] && currentLevel<4 && count[currentLevel]==total[currentLevel]){
			next = true;
			currentLevel++;
			if(currentLevel == 1){
				row = 3;
				column = 7;
				BORDER_SIZE = 80;
			}
			else if(currentLevel == 2){
				row = 4;
				column = 7;
				BORDER_SIZE = 80;
				TOP_OFFSET = 30;
			}
			else if(currentLevel == 3){
				row = 5;
				column = 6;
				BORDER_SIZE = 100;
				TOP_OFFSET = 20;
			}
			else if(currentLevel == 4){
				row = 5;
				column = 7;
				BORDER_SIZE = 100;
				TOP_OFFSET = 20;
			}
			AssignPokemon assign = new AssignPokemon();
			assign.start();

			repaint();
		}
		else if(currentLevel == 4 && time< timeAllowed[currentLevel] && count[currentLevel]==total[currentLevel]){
			JOptionPane.showMessageDialog (this, "Congrats",
					"win", JOptionPane.WARNING_MESSAGE);
			next = false;
		}
		return next;
	}

	public void result()
	{
		if(poke[0] == poke[1]){
			count[currentLevel]+=2;
			pokeballs[ro[0]][co[0]]=1;
			pokeballs[ro[1]][co[1]]= 1;
			nextLevel = checkLevel(currentLevel);
			if(nextLevel == true){
				newLevel();
			}
		}
		else{
			close = true;
			repaint();
			close = false;
		}		

	}

	// MouseListener methods
	public void mouseClicked (MouseEvent e)
	{
		int x, y;
		x = e.getX ();
		y = e.getY ();



		if(start==false && click[currentLevel] == false && x>180 && x<700 && y>200 && y<430){
			System.out.println("GAMESTART");
			click[currentLevel] = true;
			AssignPokemon assign = new AssignPokemon();
			assign.start();
		}
		else if(start==false && click[currentLevel] == true && gameStart == true){
			System.out.println("clickball");
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

		if (kp.getKeyCode() == KeyEvent.VK_ENTER){
			start = false;
		}
		else if (kp.getKeyCode() != KeyEvent.VK_ENTER){
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

	public void handleAction (int x, int y)
	{


		if(countOpen == 0){
			co[0] = (x - BORDER_SIZE) / BALL_SIZE + 1;
			ro[0] = (y - BORDER_SIZE) / BALL_SIZE + 1;
			System.out.println(ro[0]);
			System.out.println(co[0]);
			System.out.println(pokemons[2][3]);
			poke[0] = pokemons[ro[0]][co[0]];
			if(pokeballs[ro[0]][co[0]]==0){
				//animatePiece (firstPoke, ro1, co1);
				countOpen++;
				openTheBall = true;
			}
		}
		else if(countOpen == 1){
			co[1] = (x - BORDER_SIZE) / BALL_SIZE + 1;
			ro[1] = (y - BORDER_SIZE) / BALL_SIZE + 1;

			poke[1] = pokemons[ro[1]][co[1]];
			if(pokeballs[ro[1]][co[1]]==0){
				//animatePiece (secondPoke, ro2, co2);
				openTheBall = true;
				repaint();
				countOpen=0;
				result();
			}
		}
	}


	public void animatePiece (int pImageNum, int co, int ro)
	{
		Graphics g = getGraphics ();

		// Find the x and y positions for each row and column
		//int xPos = (4 - 1) * BALL_SIZE + BORDER_SIZE;
		//int yPos = TOP_OFFSET + 0 * BALL_SIZE;
		//offScreenBuffer.clearRect (xPos, yPos, SQUARE_SIZE, SQUARE_SIZE);
		int xPos = (co - 1) * BALL_SIZE + BORDER_SIZE;
		int yPos = TOP_OFFSET + (ro-1) * BALL_SIZE;

		offScreenBuffer.drawImage(pokemon[pImageNum], xPos, yPos, this);
		offScreenBuffer.drawImage(ball, xPos, yPos, this);


		if(countOpen == 2){
			result();
			countOpen = 0;
		}
	}


	public void update (Graphics g)
	{
		paint (g);
	}


	public void paintComponent (Graphics g)
	{
		System.out.println ("PAINT");
		super.paintComponent(g);
		if (offScreenBuffer == null)
		{
			offScreenImage = createImage (this.getWidth (), this.getHeight ());
			offScreenBuffer = offScreenImage.getGraphics ();
		}
		if(start){
			offScreenBuffer.drawImage (startpage,0,0,this);
		}
		else if (start == false && click[currentLevel] == false){
			offScreenBuffer.drawImage(level[currentLevel],0,0,this);
		}
		else if(click[currentLevel] == true){
			gameStart = true;
			System.out.println ("pokie");
			offScreenBuffer.drawImage(background, 0,0,this);
			//timerOn = true;
			offScreenBuffer.drawImage(timerBG, 91, 79, this);
			offScreenBuffer.setColor(Color.WHITE);
			offScreenBuffer.setFont(new Font("Gill Sans", Font.BOLD, 20)); 			
			offScreenBuffer.drawString ("Time:" + (time / 10.0), 100, 100);

			// Redraw the board with current pieces
			for (int r = 1 ; r < row ; r++){
				for (int c = 1 ; c < column ; c++)
				{
					// Find the x and y positions for each row and column
					int xPos = (c - 1) * BALL_SIZE + BORDER_SIZE;
					int yPos = TOP_OFFSET + (r-1) * BALL_SIZE;
					offScreenBuffer.drawImage(pokeball[0], xPos, yPos, this);
				}
			}
			if(openTheBall == true){
				System.out.println("change");
				openTheBall = false;
				for (int r = 1 ; r <ro[countOpen] ; r++){
					for (int c = 1 ; c < column ; c++)
					{
						int xPos = (c - 1) * BALL_SIZE + BORDER_SIZE;
						int yPos = TOP_OFFSET + (r-1) * BALL_SIZE;
						offScreenBuffer.drawImage(pokeball[0], xPos, yPos, this);
					}
				}//above
				for (int r = ro[countOpen] ; r > 1 ; r--){
					for (int c = 1 ; c < column ; c++)
					{
						int xPos = (c - 1) * BALL_SIZE + BORDER_SIZE;
						int yPos = TOP_OFFSET + (r-1) * BALL_SIZE;
						offScreenBuffer.drawImage(pokeball[0], xPos, yPos, this);
					}
				}//below
				for (int c = 1 ; c > co[countOpen] ; c++){
					int xPos = (c - 1) * BALL_SIZE + BORDER_SIZE;
					int yPos = TOP_OFFSET + (ro[countOpen]-1) * BALL_SIZE;
					offScreenBuffer.drawImage(pokeball[0], xPos, yPos, this);
				}//before
				for (int c = co[countOpen]  ; c < 1 ; c--){
					int xPos = (c - 1) * BALL_SIZE + BORDER_SIZE;
					int yPos = TOP_OFFSET + (ro[countOpen]-1) * BALL_SIZE;
					offScreenBuffer.drawImage(pokeball[0], xPos, yPos, this);
				}//before
				int xPos = (co[countOpen] - 1) * BALL_SIZE + BORDER_SIZE;
				int yPos = TOP_OFFSET + (ro[countOpen]-1) * BALL_SIZE;
				offScreenBuffer.drawImage(pokemon[poke[countOpen]], xPos, yPos, this);
				offScreenBuffer.drawImage(open[currentLevel], xPos, yPos, this);

			}
			if(close == true){
				int xPos = (co[0] - 1) * BALL_SIZE + BORDER_SIZE;
				int yPos = TOP_OFFSET + (ro[0]-1) * BALL_SIZE;
				offScreenBuffer.drawImage(pokeball[0], xPos, yPos, this);
				xPos = (co[1] - 1) * BALL_SIZE + BORDER_SIZE;
				yPos = TOP_OFFSET + (ro[1]-1) * BALL_SIZE;
				offScreenBuffer.drawImage(pokeball[0], xPos, yPos, this);
			}
		}		
		g.drawImage (offScreenImage, 0, 0, this);
	}

	/** Purpose: To delay the given number of milliseconds
	 * @param milliSec The number of milliseconds to delay
	 */
	private void delay (int milliSec)
	{
		try
		{
			Thread.sleep (milliSec);
		}
		catch (InterruptedException e)
		{
		}
	}

	public class AssignPokemon extends Thread{
		public void assignPokemon(){

			for(int i = 1; i<row; i++){
				for(int j = 1; j<column; j++){
					do{
						pokemons[i][j] = (int) Math.round(Math.random()*(total[currentLevel]/2));
						pokemonNum[pokemons[i][j]]++;
					}
					while(pokemonNum[pokemons[i][j]]>2);
				}
			}
		}
	}

	public static void main (String[] args)
	{
		frame = new JFrame ("FindYourPokemon");
		test2 myPanel = new test2();
		
		frame.add (myPanel);
		frame.pack ();
		frame.setVisible (true);

	} // main method

}
