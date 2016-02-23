package util;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Normalizer;
import java.time.LocalTime;

public class SpriteCrawler {
	private static String pokemon;
	
	public static void main(String args[]){
		long start = System.currentTimeMillis();
		Connection c;
		try {
			c = DriverManager.getConnection("jdbc:sqlite:E:\\Program Files (x86)\\SQLite\\Pokemon.db");
			Statement stmt = c.createStatement();
	      	ResultSet rs = stmt.executeQuery( "SELECT name FROM pokemon;");
	      	while(rs.next()){
	      		pokemon = rs.getString(1);
	      		pokemon = Normalizer.normalize(pokemon, Normalizer.Form.NFD);
	      		//System.out.println("Got " + pokemon + " from sqlite.");
	      		if(pokemon.endsWith(".")){
	      			pokemon = pokemon.substring(0, pokemon.length()-1);
	      		}
	      		if(pokemon.contains(" ")){
	      			pokemon = pokemon.replace(" ", "_");
	      		}
	      		if(pokemon.contains("?")){
	      			pokemon = pokemon.replace("?", "");
	      		}
	      		
	      		//System.out.println("Try to save sprite for " + pokemon + ".\n Index: " + rs.getRow());
	      		saveImage("http://www.pkparaiso.com/imagenes/xy/sprites/animados/"+ pokemon.toLowerCase() +".gif", "Sprites/" + pokemon + ".gif");
	      	}
	      	rs.close();
	      	System.out.println("Step 1 took " + LocalTime.ofSecondOfDay((System.currentTimeMillis()-start)/1000));
	      	for(int i = 1; i<722;i++){
	      		String number = i>99?i+"":i>9?"0"+i:"00"+i;
	      		rs = stmt.executeQuery( "SELECT name FROM pokemon where nummer="+i+";");
	      		saveImage("http://www.greenchu.de/sprites/icons/"+number+".gif","Icons/" + rs.getString(1) +".gif");
	      	}
	      	rs.close();
	      	System.out.println("Step 2 took " + LocalTime.ofSecondOfDay((System.currentTimeMillis()-start)/1000));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void saveImage(String imageUrl, String destinationFile){
	    try {
	    	URL url = new URL(imageUrl);
		    InputStream is = url.openStream();
		    File file = new File(destinationFile);
		    file.getParentFile().mkdirs();
		    file.createNewFile();
		    OutputStream os = new FileOutputStream(destinationFile );
	
		    byte[] b = new byte[2048];
		    int length;
	
		    while ((length = is.read(b)) != -1) {
		        os.write(b, 0, length);
		    }
	
		    is.close();
		    os.close();
	    } catch (IOException e) {
			// TODO Auto-generated catch block
	    	System.out.println("Error for getting: " + imageUrl);
			e.printStackTrace();
			if(imageUrl.endsWith(".gif")){
				//try png
				saveImage(imageUrl.replace(".gif", ".png"), destinationFile.replace(".gif", ".png"));
			}
		}
	    //System.out.println("Saved " + imageUrl + " as " + destinationFile);
	}
}
