package theatre;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Scene extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int x ;
	public int addx;
	private Image img;
	public static Scene ourscene;
	public Image sga;
	public Image mar;
	String pars;
	
	
	
	public Scene(String pars){
		setFocusable(true);
		requestFocus();
		ImageIcon i = new ImageIcon(getClass().getResource("scene.jpg"));
		img = i.getImage();
		ImageIcon i1 = new ImageIcon(getClass().getResource("sga.png"));
		sga = i1.getImage();
		ImageIcon i2 = new ImageIcon(getClass().getResource("mar.png"));
		mar = i2.getImage();
		
		x=-100;
		addx=0;
		ourscene= this;
		this.pars =pars;
		Thread t = new Thread(new Move());
		t.start();
		/*try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		//this.addx=1;
	}
	
	 
	
	
	
	public void paint(Graphics g){
		super.paint(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(img, x, 0,null);
		g2.drawImage(sga, 220, 280,null);
		g2.drawImage(mar, 700, 280,null);
		//g2.drawRect(43, 242, 201, 82);
		
		g2.drawImage(sga, 1000, 520,null);
		g2.drawString(pars,0,530);
	}
	public void updatePosition(){
		x=x-addx;
	}
}

