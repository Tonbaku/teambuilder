package util;
import java.io.File;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;


public class CrawlingController {
	    public static void main(String[] args) throws Exception {
	        String crawlStorageFolder = "/data/crawl/root";
	        int numberOfCrawlers = 10;

	        CrawlConfig config = new CrawlConfig();
	        config.setCrawlStorageFolder(crawlStorageFolder);

	        /*
	         * Instantiate the controller for this crawl.
	         */
	        PageFetcher pageFetcher = new PageFetcher(config);
	        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
	        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
	        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

	        /*
	         * For each crawl, you need to add some seed urls. These are the first
	         * URLs that are fetched and then the crawler starts following links
	         * which are found in these pages
	         */
	        //controller.addSeed("http://www.serebii.net/pokedex-xy/001.shtml");
	        

	        /*
	         * Start the crawl. This is a blocking operation, meaning that your code
	         * will reach the line after this only when crawling is finished.
	         */
	        
	        //Moves crawling
	        File f;
	        
//	        f = new File("movestest.txt");
//			if(f.exists())
//				f.delete();
//	        controller.addSeed("http://www.serebii.net/attackdex-xy/tackle.shtml");
//	        controller.start(SerebiiMovesCrawler.class, numberOfCrawlers);
//	        System.out.println("######## MOVE-CRAWLING DONE ########");
//	        
	        //Pokemon crawling
	        f = new File("stats.txt");
			if(f.exists())
				f.delete();
	        CrawlController Pcontroller = new CrawlController(config, pageFetcher, robotstxtServer);
	        Pcontroller.addSeed("http://www.serebii.net/pokedex-xy/001.shtml");
	        Pcontroller.start(SerebiiCrawler.class, numberOfCrawlers);
	        System.out.println("######## POKE-CRAWLING DONE ########");
	    }
}
