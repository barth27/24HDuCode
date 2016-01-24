package theatre;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame frame = new JFrame("THEATRE");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1200, 750);
		frame.setResizable(false);
		
		
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {
			/*
			 * Etape 2 : cr�ation d'un parseur
			 */
			final DocumentBuilder builder = factory.newDocumentBuilder();

			/*
			 * Etape 3 : cr�ation d'un Document
			 */
			final Document document= builder.parse(new File("Theatre.xml"));

			/*Affiche du prologue
		    System.out.println("*************PROLOGUE************");
		    System.out.println("version : " + document.getXmlVersion());
		    System.out.println("encodage : " + document.getXmlEncoding());		
	         System.out.println("standalone : " + document.getXmlStandalone());*/

			/*
			 * Etape 4 : r�cup�ration de l'Element racine
			 */
			final Element racine = document.getDocumentElement();

			//Affichage de l'�l�ment racine
			//System.out.println("\n*************RACINE************");
			System.out.println(racine.getNodeName());

			/*
			 * Etape 5 : r�cup�ration des personnes
			 */
			final NodeList racineNoeuds = racine.getChildNodes();
			final int nbRacineNoeuds = racineNoeuds.getLength();

			for (int i = 0; i<nbRacineNoeuds; i++) {
				if(racineNoeuds.item(i).getNodeType() == Node.ELEMENT_NODE) {
					final Element act = (Element) racineNoeuds.item(i);

					//Affichage d'une personne
					System.out.println("\n*************PERSONNE************");
					//System.out.println("sexe : " + personne.getAttribute("sexe"));

					/*
					 * Etape 6 : r�cup�ration du nom et du pr�nom
					 * 
					 */
					
					final Element scen = (Element) act.getElementsByTagName("Scene").item(0);
					if (scen!=null){
						String text = scen.getAttribute("Description");
						System.out.println(text);
						Scene scene = new Scene(text);
						scene.setForeground(Color.BLACK);
						frame.getContentPane().add(scene);
						//frame.getContentPane().add(txt);
						scene.setLayout(null);
						frame.setVisible(true); 
					}else{
						
					}
					

					//Affichage du nom et du pr�nom
					//System.out.println("nom : " + nom.getTextContent());
					//System.out.println("pr�nom : " + prenom.getTextContent());

					/*
					 * Etape 7 : r�cup�ration des num�ros de t�l�phone
					 */
					// final NodeList telephones = personne.getElementsByTagName("telephone");
				}
			}

		}catch (final ParserConfigurationException e1) {
			e1.printStackTrace();
		}
		catch (final SAXException e2) {
			e2.printStackTrace();
		}
		catch (final IOException e) {
			e.printStackTrace();
		}		
	}
}
