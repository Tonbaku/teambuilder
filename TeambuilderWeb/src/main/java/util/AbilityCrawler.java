package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AbilityCrawler {
	private static final String abilityUrl = "http://www.serebii.net/abilitydex/";
	private Set<String> abilities = new HashSet<String>();
	
	public static void main(String args[]){
		AbilityCrawler main = new AbilityCrawler();
		main.getAbilities();
		main.writeAbilities();
	}
	
	public void getAbilities(){
		try {
			Document doc = Jsoup.connect(abilityUrl).get();
			Elements abilitySelects = doc.getElementsByAttributeValue("name", "ability");
			abilitySelects.addAll(doc.getElementsByAttributeValue("name", "ability2"));
			for(Element form : abilitySelects){
				Element select = form.children().first();
				for(Element option : select.children()){
					abilities.add(option.text());
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeAbilities(){
		File f = new File("abilities.txt");
		f.delete();
		try {
			f.createNewFile();
			FileWriter fw = new FileWriter(f);
			for(String ability : abilities){
				fw.append(ability);
				fw.append(" | ");
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
