package de.l3s.hits.shs.test;

import de.l3s.hits.shs.client.Direction;
import de.l3s.hits.shs.client.SHSClient;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class RunnerLight {
//    //protected static final Logger logger = LogManager.getLogger(RunnerLight.class);
//
//    private static String inputFile = "links_1000000";
//
//    private static SHSClient shsClient;
//
//    public static void main(String[] args) throws SQLException {
//        shsClient = new SHSClient();
//
//        //logger.info("Starting task...");
//        File input = new File(inputFile);
//
//        while (processInput(input) > 0) {}
//    }
//
//    private static int processInput(File inputFile){
//        try {
//            Scanner scanner = new Scanner(inputFile);
//            //logger.warn("BRKN Reading input file from start...");
//
//            String urls[] = new String[10000];
//            int i = 0;
//            while (scanner.hasNextLine()) {
//                String url = scanner.nextLine();
//                if (url == null) continue;
//                urls[i++] = url;
//                
//                if (i == 9999) {
//                    try {
//                        processUrl(urls);
//                    } catch (Throwable e) {
//                        //logger.error("Can't process url: {}", url);
//                        throw e;
//                    }
//                    i = 0;
//                }
//            }
//            return 0;
//        } catch (Throwable e) {
//            //logger.error("An error occurred: ", e);
//            return 1;
//        } finally {
//            shsClient.close();
//        }
//    }
//
//    private static void processUrl(String url) throws Throwable {
//        //logger.info("Processing '{}'...", url);
//
//        Long retUid = null;
//
//        if (url != null ) {
//            retUid = shsClient.getUid(url);
//        }
//
//        if (retUid != null && retUid >= 0) {
//            String retUrl = shsClient.getUrl(retUid);
//            Map<Long, List<Long>> retIn = shsClient.getAllCapturesGroupByLinks(retUid, Direction.BACKWARD);
//            Map<Long, List<Long>> retOut = shsClient.getAllCapturesGroupByLinks(retUid, Direction.FORWARD);
//        }
//    }
}
