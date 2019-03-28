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

public class timer extends JPanel implements MouseListener
{
	
	Timer timer;
	boolean timerOn;
	int time;
	int timeAllowed;
	
	JFrame frame; 
	JPanel myPanel;
	AudioClip backGroundSound;
	AudioClip beep;
	AudioClip hello;
	AudioClip goodbye; 
	JLabel label;

	public timer ()
	{

		setPreferredSize(new Dimension(400, 400));

		timerOn = false;
		time = 0;
		timeAllowed = 100000;   //  10 seconds in this example
		timer = new Timer (100, new TimerEventHandler ());

		addMouseListener (this);
		
		
	}

	
	private class TimerEventHandler implements ActionListener
	{
		public void actionPerformed (ActionEvent event)
		{
			
			if (time >= timeAllowed && timerOn)
			{
				timerOn = false;
				timer.stop ();
				JOptionPane.showMessageDialog (timer.this,
						"Time is Up! Press NEW button from the menu"
						+ "to restart", "Timer", JOptionPane.INFORMATION_MESSAGE);
			}
			else
			{
				
				time++;
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
		// Toggle the timer on and off with left mouse button
		else if (timerOn)
		{
			timerOn = false;
			timer.stop ();
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

		g.drawString ("Time: " + (time / 10.0), 100, 100);
	} 
	
	public void keyPressed (KeyEvent kp) {
			
	}

	public static void main (String [] args)
	{
		new timer ();
		int num = 0;
		
		Music m = new Music("");
		
		do
		{
			m.Play();
		} while (num!=0);
		
		
		JFrame frame = new JFrame ("Timer");
		timer myPanel = new timer ();

		frame.add(myPanel);
		frame.pack();
		frame.setVisible(true);	
		
	} 
} 


