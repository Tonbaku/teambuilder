package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MegaParser {
	public static String url = "http://www.serebii.net/omegarubyalphasapphire/megaevolutions.shtml";
	
	public static void main(String[] args){
		List<String> megas = new ArrayList<String>();
		try {
			Document doc = Jsoup.connect(url).get();
			Elements tabs = doc.getElementsByClass("tab");
			//each row has 12 colums, which means we can iterate through the rows and know that the 13th entry is for a new pokemon
			int i = 1;
			for(Element entry : tabs){
				String megaTupel = "";
				for(Element info : entry.getElementsByClass("fooinfo")){
					System.out.println("Gefundener Eintrag " + i + ": " + info.text());
					switch(i){
						//name Mega Latias ??????? Mega Latias was coded into X & Y but not available before now.
						case(3):
							String[] splits = info.text().split(" ");
							megaTupel+=splits[1]+" "+(splits[2].length()==1?splits[2]:"")+" | ";break;
						//Ability
						case(4):megaTupel+= info.text() + " | ";break;
						//Types
						case(5):
							for(Element img : info.getElementsByTag("img")){
								megaTupel+= img.attr("src").replace("/pokedex-bw/type/", "").split("\\.")[0]+" # ";
							}
							megaTupel+=" | ";
							break;
						//KP
						case(6):megaTupel+=info.text()+" | ";break;
						//Angr
						case(7):megaTupel+=info.text()+" | ";break;
						//Vert
						case(8):megaTupel+=info.text()+" | ";break;
						//SpA
						case(9):megaTupel+=info.text()+" | ";break;
						//SpV
						case(10):megaTupel+=info.text()+" | ";break;
						//Init
						case(11):megaTupel+=info.text();break;
					}
					i++;
					if(i==13){
						megas.add(megaTupel);
						megaTupel = "";
						i=1;
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writeAbilities(megas);
	}
	
	public static void writeAbilities(List<String> megas){
		File f = new File("megas.txt");
		f.delete();
		try {
			f.createNewFile();
			FileWriter fw = new FileWriter(f);
			for(String mega : megas){
				fw.append(mega);
				fw.append("\n");
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
