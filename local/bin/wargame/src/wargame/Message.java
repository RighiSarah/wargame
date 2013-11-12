package wargame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

final class Message  implements ActionListener
{
	private static String message;
	private static Timer timer;
	
	protected void dessinerMessage(Graphics g, int x, int y, Color c)
	{
		g.setColor(c);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
