package HDuCode.HDuCode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;



import javax.measure.quantity.Length;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;

import com.uwyn.jhighlight.tools.StringUtils;

public class app {

	public static void genererxml(  Document dossier){

		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(dossier);
			StreamResult result = new StreamResult(new File("file.xml"));

			final StreamResult sortie1 = new StreamResult(System.out);
			transformer.setOutputProperty(OutputKeys.VERSION, "1.0");

			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.setOutputProperty(OutputKeys.STANDALONE,"yes");

			try {
				transformer.transform(source, result);
				transformer.transform(source, sortie1);
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	public static Element ouvrirBalise(Document dossier,Element et,String nomBalise, String attribut, String text){

		Element e = dossier.createElement(nomBalise);
		et.appendChild(e);

		if(nomBalise!=null){
			Attr at = dossier.createAttribute("attribut");
			at.setValue(attribut);
			e.setAttributeNode(at);

		}

		if(text!=null){
			e.appendChild(dossier.createTextNode(text));

		}
		return e;
	}
	
	
	

	public static void main(String[] args) throws Exception {

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		DOMSource source = new DOMSource(doc);
		Element rootElement = doc.createElement("piece");
		doc.appendChild(rootElement);
		Element acte = null;
		Element scene;
		Element personnage ;

		//assume rtf is in your current directory
		File file = new File("MOLIERE-Le_medecin_malgre_lui-[Atramenta.net].rtf");//

		//Instantiating tika facade class 
		Tika tika = new Tika();

		//detecting the file type using detect method
		String filetype = tika.detect(file);
		System.out.println("le type de fichier est "+filetype);


		//another one
		//Instantiating Tika facade class
		//      String filecontent = tika.parseToString(file);
		//      System.out.println("Extracted Content: " + filecontent);

		//another one

		//      Parser parser = new AutoDetectParser();
		//      BodyContentHandler handler = new BodyContentHandler();
		//      Metadata metadata = new Metadata();
		//      FileInputStream inputstream = new FileInputStream(file);
		//      ParseContext context = new ParseContext();
		//      
		//      //parsing the file
		//      parser.parse(inputstream, handler, metadata, context);
		//      System.out.println("File content : " + handler.toString());



		Parser parser = new AutoDetectParser();
		BodyContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		FileInputStream inputstream = new FileInputStream(file);
		ParseContext context = new ParseContext();

		parser.parse(inputstream, handler, metadata, context);
		//  System.out.println(handler.toString());

		//getting the list of all meta data elements 
		//      String[] metadataNames = metadata.names();
		//      System.out.println("**************");
		//      for(String name : metadataNames) {		        
		//         System.out.println(name + ": " + metadata.get(name));
		//         
		//   }
		//      String lines[] = handler.toString().split("[\r\n]+");
		//      System.out.println(lines.length);
		//      for(int i = 0 ; i<lines.length ; i++){
		//      System.out.println(lines[i]);
		//      }

		String[] messageAparser = handler.toString().split("\n");
		int i = 0;
		String[] lignemot = null;
		String intro = "";
		int k = 0;
		do{
			
			lignemot=messageAparser[i].split(" ");
			if(lignemot[0].equals("Introduction")){
				i++;
				lignemot=messageAparser[i].split(" ");
				while(lignemot.length!=2){
					for(int taille = 0;taille<lignemot.length;taille++){
						intro+=lignemot[taille];
						intro+=" ";
					}
					i++;
					lignemot=messageAparser[i].split(" ");
				
				}
				
				if(k==0){
				Element introd = ouvrirBalise(doc, rootElement, "Introduction", null, intro);}
				k++;

			}
			//if(lignemot.length == 2)
			{
				if(lignemot[0].equals("Acte")){
					System.out.println("******ACTE******");
					String numacte = lignemot[1];
					System.out.println(lignemot[0]+" "+numacte);
					acte =ouvrirBalise(doc, rootElement, "acte", numacte, null);

				}
				if(lignemot[0].equals("Scène")){
					System.out.println("******scene******");
					String scenee = lignemot[1];
					
					System.out.println(lignemot[0]+" "+scenee);	
					boolean yes = false;
					String description="";

					i++;
					while(yes==false){
						lignemot= messageAparser[i].split(" ");

						if(lignemot[0].endsWith(",")||lignemot[0].endsWith(".")){
							for(int taille =0;taille<lignemot.length;taille++){
								description+=lignemot[taille];
								description+=" ";
							}personnage = ouvrirBalise(doc, acte, "scène", scenee, description);
						}else{
							yes = true;
						}
						i++;}System.out.println("description "+description);

						lignemot= messageAparser[i].split(" ");
						boolean trouve = false;
//						while(trouve==false){
//							//for(int taille = 0; taille < lignemot.length;taille++)
//							{
//								if(lignemot[0].equals("Scène")){
//									System.out.println("------->scene");
//									trouve = true;
//								}
//								if(lignemot[0].equals("Acte")){
//									System.out.println("ACTE "+lignemot[1]);
//									trouve = true;
//									
//								}
//								if(lignemot[0].equals("FIN")){
//									System.out.println("------->fin");
//									trouve = true;
//									
//								}
//								
//								if(lignemot[0].length()==2){System.out.println(lignemot[0]+lignemot[1]);}
//								if(lignemot[0].equals(lignemot[0].toUpperCase())&&lignemot[0]!=null&&lignemot[0].length()>=2){
//									if(lignemot[0].equals("M.")){System.out.println(lignemot[0]+lignemot[1]);}
//									System.out.println(lignemot[0]);
//								}
//							}
//							System.out.println();
//							i++;
//							if(i<messageAparser.length)
//							{
//								lignemot= messageAparser[i].split(" ");}
////							else
////							{
////								break;}
//						}

						//on va chercher à récupérer les personnages
						//						boolean yess = false;
						//						String personage="";
						//
						//
						//						while(yess==false){
						//							lignemot= messageAparser[i].split(" ");
						//							System.out.println("["+lignemot[0]+"]");
						//							System.out.println("["+lignemot[0].length()+"]");
						//
						//							if(lignemot[0].length()!=1){
						//
						//								if((lignemot[0].equals(lignemot[0].toUpperCase()))){
						//									personage = lignemot[0];
						//									System.out.println("personage "+personage);
						//								}else if(lignemot[0].equals("M.")){
						//									personage = lignemot[0]+lignemot[1];System.out.println("personage "+personage);
						//
						//								}else if(lignemot[0].endsWith(",")){
						//									lignemot[0]=lignemot[0].substring(0, lignemot[0].length()-1);
						//									System.out.println("mot substring "+lignemot[0]);
						//									if(lignemot[0].equals(lignemot[0].toUpperCase())){
						//										personage  = lignemot[0];
						//										System.out.println("personage "+personage);
						//									}
						//
						//								}
						//								}
						//
						//
						//							
						//						if(lignemot[0].equals("Scène")||lignemot[0].equals("Acte")||lignemot[0].equals("FIN")){yess = true;}
						//
						//							i++;
						//
						//						}
						//	System.out.println("description "+description);















						//						lignemot= messageAparser[i].split(" ");
						//						boolean ligne = true;
						//						String[] desordre = null;

						//						while(ligne ==true){
						//							desordre = lignemot[0].split(",");
						//						if(desordre.length ==1 ){
						//							if(desordre[0].equals(desordre[0].toUpperCase()))
						//							{System.out.println(desordre[0]);}
						//							i++;
						//							lignemot= messageAparser[i].split(" ");
						//						}else if(desordre[0].endsWith(",")){
						//							desordre[0].substring(0, desordre[0].length()-1);
						//							if(desordre[0].equals(desordre[0].toUpperCase()))
						//							{System.out.println(desordre[0]);}
						//							i++;
						//						}
						//						
						//						else{
						//							ligne = false;
						//						}
						//						}
						



				}

			}






			i++;

			if(i>=messageAparser.length){break;}
		}while(i<messageAparser.length);

		{genererxml(doc);
			System.out.println("******INTRODUCTION******");
			System.out.println(intro);

		}
	}
}