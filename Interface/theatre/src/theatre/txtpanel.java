package theatre;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class txtpanel extends JPanel{
	private static final long serialVersionUID = 1L;
	public int x ;
	public int addx;
	public static txtpanel ourtxtp;
	private Image sga;
	private Image mar;
	
	public txtpanel(){
		setFocusable(true);
		requestFocus();
		ImageIcon i1 = new ImageIcon(getClass().getResource("sga.png"));
		sga = i1.getImage();
		ImageIcon i2 = new ImageIcon(getClass().getResource("mar.png"));
		mar = i2.getImage();
		x=0;
		addx=0;
		ourtxtp= this;
		ourtxtp.setAlignmentX(0);
		ourtxtp.setAlignmentY(520);
		Thread t = new Thread(new Move());
		t.start();
	}
	public void paint(Graphics g){
		super.paint(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(sga, 1000,520 ,null);
		//g2.drawImage(mar, 700, 280,null);
		
	}
	public void updatePosition(){
		x=x-addx;
	}
}
