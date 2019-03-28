/** The "SwingTimer" class.
 * Demonstrates a Swing Timer
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.*;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SwingTimer extends JPanel implements MouseListener
{
	Timer timer;
	boolean timerOn;
	int time;
	int timeAllowed;
	
	JFrame frame; 
	JPanel myPanel;
	AudioClip backGroundSound;
	JLabel label;

	public SwingTimer ()
	{

		setPreferredSize(new Dimension(400, 400));

		timerOn = false;
		time = 0;
		timeAllowed = 100;   //  10 seconds in this example

		timer = new Timer (100, new TimerEventHandler ());
		
		backGroundSound = Applet.newAudioClip (getCompleteURL ("gameBGM.wav"));

		addMouseListener (this);

		backGroundSound.play ();
		backGroundSound.loop ();
		
		
	} // Constructor


	// An inner class to deal with the timer events
	private class TimerEventHandler implements ActionListener
	{
		// The following method is called each time a timer event is
		// generated (every 100 milliseconds in this example)
		// Put your code here that handles this event
		public void actionPerformed (ActionEvent event)
		{
			// Time is up!
			if (time >= timeAllowed && timerOn)
			{
				timerOn = false;
				timer.stop ();
				JOptionPane.showMessageDialog (SwingTimer.this,
						"Time is Up", "Timer", JOptionPane.INFORMATION_MESSAGE);
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

	public void mouseClicked (MouseEvent e)
	{
		// If right mouse button is clicked, reset the time
		if (e.isMetaDown ())
		{
			time = 0;
		}

		else
		{
			timerOn = true;
			timer.start ();
		}
		repaint ();

	}
	public void mouseReleased (MouseEvent e) 
	{
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent arg0) {
	}

	public void paintComponent(Graphics g)
	{		
		super.paintComponent(g);

		if (timerOn)
			g.drawString ("Press the left mouse button to start the timer", 10, 150);


		// You should add in a method to display the time in a format
		// with minutes and seconds such as "0:00"  Hint: use / and %
		g.drawString ("Time: " + (time / 10.0), 100, 100);
	} // paint method
	
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

	public static void main (String [] args)
	{
		//new SwingTimer ();
		JFrame frame = new JFrame ("Timer");
		SwingTimer myPanel = new SwingTimer ();

		frame.add(myPanel);
		frame.pack();
		frame.setVisible(true);	

	} // main method
} // SwingTimer class


