package util;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class SerebiiCrawler extends WebCrawler {
	private final static Pattern FILTERS = Pattern
			.compile(".*(\\.(css|js|gif|jpg" + "|png|mp3|mp3|zip|gz))$");
	
	//Used for correcting data inconsistencies
	//key: wrong entry -> value: correct entry
	private final static HashMap<String, String> moveDictionary;
	static
    {
		//Special handling for "Mud-slap", "Double-edge", "Soft-boiled", "Wake-up Slap", 
		// "Baby-doll Eyes", "Will-o-wisp", "Self-destruct", "Lock-on" (fucking inconsistencies)
        moveDictionary = new HashMap<String, String>();
        moveDictionary.put("Mud-slap", "Mud-Slap");
        moveDictionary.put("Double-edge", "Double-Edge");
        moveDictionary.put("Soft-boiled", "Soft-Boiled");
        moveDictionary.put("Wake-up Slap", "Wake-Up Slap");
        moveDictionary.put("Baby-doll Eyes", "Baby-Doll Eyes");
        moveDictionary.put("Will-o-wisp", "Will-O-Wisp");
        moveDictionary.put("Self-destruct", "Self-Destruct");
        moveDictionary.put("Lock-on", "Lock-On");
    }

	/**
	 * This method receives two parameters. The first parameter is the page in
	 * which we have discovered this new url and the second parameter is the new
	 * url. You should implement this function to specify whether the given url
	 * should be crawled or not (based on your crawling logic). In this example,
	 * we are instructing the crawler to ignore urls that have css, js, git, ...
	 * extensions and to only accept urls that start with
	 * "http://www.ics.uci.edu/". In this case, we didn't need the referringPage
	 * parameter to make the decision.
	 */
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		return !FILTERS.matcher(href).matches()
				&& href.matches("http://www.serebii.net/pokedex-xy/[0-9]{3}\\.shtml");
	}

	/**
	 * This function is called when a page is fetched and ready to be processed
	 * by your program.
	 */
	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		System.out.println("URL: " + url);

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			//String text = htmlParseData.getText();
			String text = htmlParseData.getHtml();
			Pattern statRegex = Pattern.compile("Base Stats.*?[0-9]{3}.*?([0-9]{1,3}).*?([0-9]{1,3}).*?([0-9]{1,3}).*?([0-9]{1,3}).*?([0-9]{1,3}).*?([0-9]{1,3})", Pattern.DOTALL);
			//Pattern statRegex = Pattern.compile("Base Stats.*?[0-9]{3}.*?");
			Pattern.matches("Base Stats - Total:.*?[0-9]{3}.*?", "Base Stats - Total: 318 	45 	49 	49 	65 	65 	45");
			Matcher ma = statRegex.matcher(text);
			if(ma.find()){
				try {
					File f = new File("stats.txt");
					FileWriter fw = new FileWriter(f, true);
					fw.append(
							htmlParseData.getTitle() + " | KP " + ma.group(1) + " Angriff " + ma.group(2) + " Vert " + ma.group(3) + " SpA " + ma.group(4) + " SpV " + ma.group(5) + " Init " + ma.group(6)
							);
					//Pattern typeRegex = Pattern.compile("<td class=\"tooltabcon\"><a href=\"/pokedex-xy/[a-z]*?\\.shtml\"><img src=\"/pokedex-bw/type/([a-z]*?)\\.gif\" border=\"0\"></a> (<a href=\"/pokedex-xy/[a-z]*?\\.shtml\"><img src=\"/pokedex-bw/type/([a-z]*?)\\.gif\" border=\"0\"></a>)?", Pattern.DOTALL);
					ma.reset();
					Pattern moveRegex = Pattern.compile("<td rowspan=\"2\" class=\"fooinfo\"><a href=\"\\/attackdex-xy\\/.*?\\.shtml\">(.*?)<\\/a>");
					ma.usePattern(moveRegex);
					Set<String> learnset = new HashSet<String>();
					while(ma.find()){
						String move = ma.group(1);
						if(move.contains("<br")){
							move = move.split("<br")[0];
						}
						if(moveDictionary.containsKey(move)){
							move = moveDictionary.get(move);
						}
						learnset.add(move);
					}
					Pattern typeRegex = Pattern.compile("<td class=\"tooltabcon\"><a href=\"\\/pokedex-xy\\/[a-z]*?\\.shtml\".*?img src=\"\\/pokedex-bw\\/type/([a-z]*?)\\.gif\" border=\"0\".*?\\/a>(.*?pokedex-bw\\/type\\/([a-z]*?)\\.gif\")?", Pattern.DOTALL);
					ma.usePattern(typeRegex).find();
					fw.append(
							 " | " + ma.group(1) + (ma.group(2)!=null?" / " + ma.group(3):"") + " | "
							);
					boolean first = true;
					for(String move : learnset){
						if(!first){
							fw.append(" - ");
						} else {
							first = false;
						}
						fw.append(move);
						
					}
					
					Pattern abilityRegex = Pattern.compile("<a href=\"/abilitydex/.*?\\.shtml\"><b>(.*?)</b></a>");
					ma.usePattern(abilityRegex);
					fw.append(" | ");
					ma.reset();
					List<String> abilities = new ArrayList<String>();
					HashMap<String, Boolean> shouldAdd = new HashMap<String, Boolean>();
					while(ma.find()){
						String ability = ma.group(1);
						if(abilities.contains(ability)){
							shouldAdd.put(ability, !shouldAdd.get(ability));
						} else {
							shouldAdd.put(ability, true);
						}
						if(shouldAdd.get(ability)){
							abilities.add(ability);
						}
					}
					int i = 0;
					for(String ability : abilities){
						i++;
						fw.append(ability+(i==abilities.size()?"":" # "));
					}
					
					
					fw.append("\n");
					fw.close();
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("KP " + ma.group(1) + " Angriff " + ma.group(2) + " Vert " + ma.group(3) + " SpA " + ma.group(4) + " SpV " + ma.group(5) + " Init " + ma.group(6));
			};
		}
	}
}
