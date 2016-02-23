package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import model.CounterInformation;
import model.MetaPokemon;
import model.Metadata;

public class MetadataProvider {
	private Metadata<MetaPokemon> data;
	
	@PostConstruct
	public void initMetadata(){
		data = new Metadata<MetaPokemon>();
		Calendar today = Calendar.getInstance();
		String url = "http://www.smogon.com/stats/%YEAR%-%MONTH%/moveset/ou-1825.txt";
		url=url.replace("%YEAR%", today.get(Calendar.YEAR)+"");
		url=url.replace("%MONTH%", today.get(Calendar.MONTH)>10?(today.get(Calendar.MONTH)-1)+"":"0"+(today.get(Calendar.MONTH)-1));
		
		try {
			URL smogon = new URL(url);
			URLConnection connection = smogon.openConnection();
			File f = new File("metadata.txt");
			if(f.exists())
				f.delete();
			f.getAbsolutePath();
			FileWriter fw = new FileWriter(f);
			BufferedReader in = new BufferedReader(new InputStreamReader(
                     connection.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) 
				fw.write(inputLine+"\n");
			fw.close();
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getMetadata() throws IOException{
		/* 
 +----------------------------------------+ 
 | %NAME%                           	  | 
 +----------------------------------------+ 
 | Raw count: 811891                      | 
 | Avg. weight: 0.00789203749869          | 
 | Viability Ceiling: 91                  | 
 +----------------------------------------+ 
 | Abilities                              | 
 | %NAME% %PERCENT%                       | 
 +----------------------------------------+ 
 | Items                                  | 
 | %NAME% %PERCENT%                   	  |  
 | Ignore Other!						  |
 +----------------------------------------+ 
 | Spreads                                | 
 | %NATURE%:%EVS% %PERCENT%               | 
 | Ignore Other!                          | 
 +----------------------------------------+ 
 | Moves                                  | 
 | %NAME% %PERCENT%                   	  |  
 | Ignore Other!		                  | 
 +----------------------------------------+ 
 | Teammates                              | 
 | %NAME% %ABOVEAVG%					  |
 +----------------------------------------+ 
 | Checks and Counters                    | 
 | %NAME% %RATIO% (%IGNORE%) (%KO'D% [a-z] / %SWITCHED% [a-z])| 
 +----------------------------------------+ 
 +----------------------------------------+ 
  */
		Pattern seperator = Pattern.compile("\\+\\-*?\\+");
		Pattern namePattern = Pattern.compile("\\|(.*?) *?\\|");
		Pattern abilitiesPattern = Pattern.compile("\\|(.*?) ([0-9\\.]*?)% *?\\|");
		Pattern itemsPattern = Pattern.compile("\\|(.*?) ([0-9\\.]*?)% *?\\|");
		Pattern spreadsPattern = Pattern.compile("\\|(.*?):([0-9/]*?) ([0-9\\.]*?)% *?\\|");
		Pattern movesPattern = Pattern.compile("\\|(.*?) ([0-9\\.]*?)% *?\\|");
		Pattern matesPattern = Pattern.compile("\\|(.*?) ([\\+\\-0-9\\.]*?)% *?\\|");
		Pattern countersPattern = Pattern.compile("\\| (\\w*?) ([0-9\\.]*?) \\(.*?\\) [ \\n\\t\\r|]*? \\(([0-9\\.]*?)% KOed \\/ ([0-9\\.]*?)%"
				+ " switched out\\) *?\\|", Pattern.DOTALL);
		
		Matcher mat;
		File f = new File("metadata.txt");
		FileReader fr = new FileReader(f);
		StringBuilder fullBlock = new StringBuilder(); 
		int buffer;
		int count = 0;
		long start = System.currentTimeMillis();
		while((buffer = fr.read())!=-1 && count < 50){
			if(buffer!='+'){
				fullBlock.append((char)buffer);	
			} else if(fullBlock.toString().endsWith("+ \n ")){
				if(!fullBlock.toString().startsWith("+"))
					fullBlock.insert(0,"+");
				mat = seperator.matcher(fullBlock.toString());
				Matcher matBlock;
				MetaPokemon metamon = new MetaPokemon();
				mat.find();
				for(int i = 0; i<8;i++){
					int lastEnd = mat.start();
					if(!mat.find()){
						break;
					}
					int nextStart = mat.start();
					String blockContent = fullBlock.substring(lastEnd, nextStart);
					switch(i){
					//Set name
					case(0):matBlock = namePattern.matcher(blockContent);matBlock.find();
					metamon.setName(matBlock.group(1).trim());break;
					//skip, unwanted information
					case(1):break;
					//set abilities
					case(2):matBlock = abilitiesPattern.matcher(blockContent);while(matBlock.find()){
						metamon.getAbilities().put(matBlock.group(1), Double.parseDouble(matBlock.group(2)));
					}break;
					//set items
					case(3):matBlock = itemsPattern.matcher(blockContent);
					while(matBlock.find()){
						metamon.getItems().put(matBlock.group(1), Double.parseDouble(matBlock.group(2)));
					}break;
					//set spreads
					case(4):matBlock = spreadsPattern.matcher(blockContent);
					while(matBlock.find()){
						String nature = matBlock.group(1);
						HashMap<String, HashMap<String[], Double>> spreadMap = metamon.getSpreads();
						if(spreadMap.containsKey(nature)){
							spreadMap.get(nature).put(matBlock.group(2).split("/"), Double.parseDouble(matBlock.group(3)));
						} else {
							HashMap<String[], Double> spreadEntry = new HashMap<String[], Double>();
							spreadEntry.put(matBlock.group(2).split("/"), Double.parseDouble(matBlock.group(3)));
							spreadMap.put(nature,spreadEntry);
						}
						metamon.setSpreads(spreadMap);
					}break;
					//set moves
					case(5):matBlock = movesPattern.matcher(blockContent);
					while(matBlock.find()){
						metamon.getMoves().put(matBlock.group(1), Double.parseDouble(matBlock.group(2)));
					}break;
					//set mates
					case(6):matBlock = matesPattern.matcher(blockContent);
					while(matBlock.find()){
						metamon.getMates().put(matBlock.group(1), Double.parseDouble(matBlock.group(2)));
					}break;
					//set counters
					case(7):matBlock = countersPattern.matcher(blockContent);
					while(matBlock.find()){
						CounterInformation cinfo = new CounterInformation();
						cinfo.setWinRatio(Double.parseDouble(matBlock.group(2)));
						cinfo.setKoRatio(Double.parseDouble(matBlock.group(3)));
						cinfo.setSwitchRatio(Double.parseDouble(matBlock.group(4)));
						metamon.getCounters().put(matBlock.group(1), cinfo);
					}break;
					}
				}
				this.data.add(metamon);
				fullBlock = new StringBuilder();
				count++;
				System.out.println(count + ". took " + (System.currentTimeMillis() - start));
			} else {
				fullBlock.append((char)buffer);	
			}
			
		}
		System.out.println("1000 chars took " + (System.currentTimeMillis() - start));
		this.debugMetadata();
	}
	
	public Metadata<MetaPokemon> provideData(){
		if(data==null || data.isEmpty()){
			try {
				getMetadata();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return data;
	}
	
	public void debugMetadata(){
		File f = new File("E:\\metadataDebug.txt");
		try {
			FileWriter fw = new FileWriter(f);
			for(MetaPokemon p : this.data){
				for(Entry<String, CounterInformation> e : p.getCounters().entrySet()){
					fw.write(e.getKey() + " | " + e.getValue() + " \n");
				}
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
