package util;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class SerebiiMovesCrawler extends WebCrawler {
	private final static Pattern FILTERS = Pattern
			.compile(".*(\\.(css|js|gif|jpg" + "|png|mp3|mp3|zip|gz))$");
	private final static Set<String> recoilMoves = new HashSet<String>(Arrays.asList("Take Down", "Double-Edge", "Submission", "Volt Tackle", "Flare Blitz", "Brave Bird", "Wood Hammer", "Head Smash", "Wild Charge", "Head Charge", "Light of Ruin", "Jump Kick", "High Jump Kick"));
	private final static Set<String> multihitMoves = new HashSet<String>(Arrays.asList("Arm Thrust", "Barrage", "Bone Rush", "Bullet Seed", "Comet Punch", "Double Slap", "Fury Attack", "Fury Swipes", "Icicle Spear", "Pin Missile", "Rock Blast", "Spike Cannon", "Tail Slap", "Water Shuriken"));

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
				&& href.matches("http://www.serebii.net/attackdex-xy/.*?\\.shtml");
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
			String text = htmlParseData.getHtml();
			Pattern moveRegex = Pattern.compile("<td class=\"cen\">(.*?)</td>", Pattern.DOTALL);
			Matcher ma = moveRegex.matcher(text);
			String name = "";
			if(ma.find()){
				try {
					StringBuilder moveLine = new StringBuilder();
					for(int i = 0; i<6;i++){
						//get the first 5 "td.cen" which are: name, type, spectrum, basepower and accuracy
						switch(i){
							//Name
							case(0): if(ma.group(1).contains("<br />")){
										name = ma.group(1).split("<br />")[0].trim();
										moveLine.append(name);}
									else{
										System.out.println("Fehler: " + ma.group(1));
										return;
										};break;
							//type
							case(1): moveLine.append(" | " + ma.group(1).split("type\\/")[1].split("\\.gif")[0]);break;
							//phys/spec/other
							case(2): moveLine.append(" | " + ma.group(1).split("type\\/")[1].split("\\.png")[0]);break;
							//pp
							case(3): moveLine.append(" | " + ma.group(1).trim());break;
							//strength
							case(4): moveLine.append(" | " + ma.group(1).trim());break;
							//accu
							case(5): moveLine.append(" | " + ma.group(1).trim());break;
						}
						//finde den nächsten passenden eintrag
						if(!ma.find()){
							if(i==5){
								//okay!
								
								
								
							} else {
								//keine echte attacke -> skip it
								return;
							}
						} 
						
					}
					//ma.group(1) -> effect rate, if this is not -- % or 100 % -> sideeffect = true for this move
					String chance = ma.group(1).trim();
					moveLine.append(" | " + ((chance.equals("-- %") || chance.equals("100 %"))?false:true));
					//this finds the tm -> unwanted information
					ma.find();
					//this finds the prioritylevel
					ma.find();
					moveLine.append(" | " + ma.group(1).trim());
					//ignore the next 4 because they are irrelevant
					for(int i = 0; i<4; i++)
						ma.find();
					//this finds the contact flag: yes -> true, else -> false
					ma.find();
					moveLine.append(" | " + (ma.group(1).trim().equals("Yes")?true:false));
					//this finds sound moves
					ma.find();
					moveLine.append(" | " + (ma.group(1).trim().equals("Yes")?true:false));
					//this finds punch moves
					ma.find();
					moveLine.append(" | " + (ma.group(1).trim().equals("Yes")?true:false));
					//bite, recoil, launch, multihit
					moveLine.append(" | " + (name.equals("Bite")||name.equals("Crunch")||name.toLowerCase().contains("fang")));
					moveLine.append(" | " + recoilMoves.contains(name));
					moveLine.append(" | " + (name.toLowerCase().contains("pulse")||name.toLowerCase().contains("aura")));
					moveLine.append(" | " + multihitMoves.contains(name));
					moveLine.append("\n");
					//this results to the pattern "name|type|spectrum|pp|basepower|accuracy|sideefect|priority|contact|sound|punch\n"
					File f = new File("movestest.txt");
					FileWriter fw = new FileWriter(f, true);
					BufferedWriter bw = new BufferedWriter(fw);
					
					fw.append(countLines("movestest.txt")+1 + " | " +  moveLine.toString());
					fw.close();
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}
	}
	
	/**
	 * convenience method that gets the number of lines from a file
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	private int countLines(String filename) throws IOException {
	    InputStream is = new BufferedInputStream(new FileInputStream(filename));
	    try {
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars = 0;
	        boolean empty = true;
	        while ((readChars = is.read(c)) != -1) {
	            empty = false;
	            for (int i = 0; i < readChars; ++i) {
	                if (c[i] == '\n') {
	                    ++count;
	                }
	            }
	        }
	        return (count == 0 && !empty) ? 1 : count;
	    } finally {
	        is.close();
	    }
	}
}
