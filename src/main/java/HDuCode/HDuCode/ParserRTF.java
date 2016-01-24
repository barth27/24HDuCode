package HDuCode.HDuCode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.pkg.RarParser;
import org.apache.tika.parser.rtf.RTFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;
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

public class ParserRTF {
	Set<String> listepersonnages=new HashSet<String>();



	//----------------------------------------------------------------------------------------------/

	public void RecupererListePersonnage(String[]line){
		boolean exist=false;
		for(int i=0;i<line.length;i++){
			String[] LigneActuelle=line[i].split(" ");
			if(StringUtils.isAllUpperCase(LigneActuelle[0])&& LigneActuelle[0].length()>4){
				listepersonnages.add(LigneActuelle[0]);
			}
			if(LigneActuelle.length>=2&&LigneActuelle[0].equals("M.")&&  StringUtils.isAllUpperCase(LigneActuelle[1])){
				String[] Mister=new String[LigneActuelle.length];
				Mister[0]=LigneActuelle[0];
				Mister[1]=LigneActuelle[1];
				String Misterstring=Mister[0]+Mister[1];
				listepersonnages.add(Misterstring);
			}
		}
		/*for(String pers:listepersonnages){
			System.out.println("personnage: "+pers);

		}*/
	}

	//----------------------------------------------------------------------------------/
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

	//-------------------------------------------------------------------------------/

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


	//---------------------------------------------------------------------------------------------/

	public void parserpourxml(String[]line){
		try{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			DOMSource source = new DOMSource(doc);
			Element rootElement = doc.createElement("piece");
			doc.appendChild(rootElement);
			Element acte;
			Element scene;
			Element personnage ;
			String messageintro=null;
			String[]LigneActuelle=null;
			int i=0;
			//LigneActuelle=line[i].split(" ");
			//do{

			for( i=0;i<line.length;i++){	
				LigneActuelle=line[i].split(" ");
				//recherche et récupération de l'intro
				if(LigneActuelle[0].equals("Introduction")){
					int j=i+1;
					String[]l;
					do{
						l=line[j].split(" ");
						if(!l[0].equals("Acte")){
							messageintro=messageintro+line[j];
							j++;

						}
					}while(!l[0].equals("Acte"));
					i=j;
					Element intro = ouvrirBalise(doc, rootElement, "Introduction", null, messageintro);
					break;

				}
				else{continue;}
			}
			//	}while(LigneActuelle[0].equals("Introduction")==false);
			if(LigneActuelle[0].equals("FIN")==false){
				LigneActuelle=line[i].split(" ");
			}
			//recherche et récupération de l'acte
			do{
				if(LigneActuelle[0].equals("Acte")){
					String numeroActe=LigneActuelle[1];
					System.out.println(numeroActe);
					System.out.println("on est dans un nouvel acte");
					acte = ouvrirBalise(doc, rootElement, "Acte", numeroActe, null);
					
					do{
						LigneActuelle=line[i].split(" ");
						//recherche et récupération de la description de chaque scene

						if(LigneActuelle[0].equals("Scène")){
							System.out.println("nous sommes dans une nouvelle scene");
							String numeroscene=LigneActuelle[1];
							System.out.println("numero scene: "+numeroscene);
							String descriptionscene="";
							boolean exit=false;
							i++;
							LigneActuelle=line[i].split(" ");
							
							do{
								descriptionscene+=line[i];
								LigneActuelle=line[i].split(" ");	
								if(LigneActuelle[0].equals("FIN")==true){
								i++;
								}
								if(LigneActuelle.length>=2&&LigneActuelle[0].equals("M.")&&  StringUtils.isAllUpperCase(LigneActuelle[1])){
									exit=true;
								}
							}while(StringUtils.isAllUpperCase(LigneActuelle[0])==false&&exit==false&&LigneActuelle[0].equals("M.")==false);

							scene = ouvrirBalise(doc, acte, "Scène", numeroscene, descriptionscene);
							LigneActuelle=line[i].split(" ");
							//Traitement du dialogue 
							int p=0;
							do{
								LigneActuelle=line[i].split(" ");
								String nomacteur=null;
								String script="";
								
								if(StringUtils.isAllUpperCase(LigneActuelle[0])){
									nomacteur=LigneActuelle[0];
									System.out.println(nomacteur);
									 script="";
									 p=i+1;
									String[]verif=line[i].split(" ");
									LigneActuelle=line[p].split(" ");
									do{
										if(verif[0].equals("FIN")==false&&StringUtils.isAllUpperCase(LigneActuelle[0])==false&&LigneActuelle[0].equals("M.")==false){
											script+=line[p];
											p++;
											i++;
											verif=line[i].split(" ");
											LigneActuelle=line[p].split(" ");
										}

										
									}while(StringUtils.isAllUpperCase(LigneActuelle[0])==false&&LigneActuelle[0].equals("FIN")==false&&LigneActuelle[0].equals("M.")==false);
								}
								else if(LigneActuelle[0].equals("M.")){
									nomacteur=LigneActuelle[0]+LigneActuelle[1];
									System.out.println(nomacteur);
									 script="";
									 p=i+1;
									String[]verif=line[i].split(" ");
									LigneActuelle=line[p].split(" ");
									do{
										if(verif[0].equals("FIN")==false&&StringUtils.isAllUpperCase(LigneActuelle[0])==false&&LigneActuelle[0].equals("M.")==false){
											script+=line[p];
											p++;
											i++;
											verif=line[i].split(" ");
											LigneActuelle=line[p].split(" ");
										}

										
									}while(StringUtils.isAllUpperCase(LigneActuelle[0])==false&&LigneActuelle[0].equals("FIN")==false&&LigneActuelle[0].equals("M.")==false);
								}


									i=p;
									System.out.println(script);
									personnage = ouvrirBalise(doc, scene, "Personnage", nomacteur, script);
								
							}while(LigneActuelle[0].equals("FIN")&&LigneActuelle[0].equals("Scène")&&LigneActuelle[0].equals("Acte")&&StringUtils.isAllUpperCase(LigneActuelle[0])&&LigneActuelle[0].equals("M."));
							
						}else{i++;continue;}
						LigneActuelle=line[i].split(" ");
					}while(LigneActuelle[0].equals("Scène")==false&&LigneActuelle[0].equals("FIN")==false&&LigneActuelle[0].equals("Acte")==false);
				}
				}while(LigneActuelle[0].equals("FIN")==false);
			
			System.out.println("je suis sorti");
			genererxml(doc);

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	//--------------------------------------------------------------------------------------------------/

	public static void main(String[] args)throws IOException,SAXException,TikaException {
		// TODO Auto-generated method stub

		File file=new File("MOLIERE-Le_medecin_malgre_lui-[Atramenta.net].rtf");
		ParserRTF parserRTF=new ParserRTF();

		//parser methods parameters
		Parser parser= new AutoDetectParser();
		BodyContentHandler handler= new BodyContentHandler();
		Metadata metadata=new Metadata();
		FileInputStream inputstream=new FileInputStream(file);
		ParseContext context= new ParseContext();

		//parsing the file
		parser.parse(inputstream, handler,metadata, context);
		//System.out.println("file content:"+handler.toString());

		String[] line=handler.toString().split("\n");
		parserRTF.RecupererListePersonnage(line);
		parserRTF.parserpourxml(line);

		/*for(int i=0;i<line.length;i++){
			System.out.println(line[i]);
		}*/

	}

}
