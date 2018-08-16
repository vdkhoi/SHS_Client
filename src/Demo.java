/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import Library.SHS_Client;
/**
 *
 * @author vdkhoi
 */
public class Demo {
    public static void main(String[] agrs) {
        System.out.println("Demo running");
        String[] sample = { "bild.de/", "angela-merkel.de/"};
        String firstTime = "2000-01-01";
        String lastTime = "2013-01-15";
        //String leader = "master03.ib";
        String leader = "DESKTOP-IKH61D8";
        //String storeID = "a2076350e64b4aa89eb75449ed263117";
        String storeID = "9b41b01ab89742e6816d72d3fd03508b";

        if (agrs.length == 2) {
            leader = agrs[0];
            storeID = agrs[1];
        }
        
        System.out.println("Start connection...");
        SHS_Client client = new SHS_Client(leader, storeID, Boolean.TRUE);
        
        System.out.println("UrlToUid: " + sample[0]);
        long uid = client.ClientUrlToUid(sample[0]);
        System.out.println("UrlToUid=" + uid);
        
        System.out.println("UidToUrl: " + uid);
        String url = client.ClientUidToUrl(uid);
        System.out.println("UidToUrl=" + url);
      
//        System.out.println("WebGetTemporalPageLinks: ");
//        String[] urls = client.WebGetTemporalPageLinks(sample[0], firstTime, lastTime, SHS_Client.FORWARD);
//        for (String url1 : urls) {
//            System.out.println(url1);
//        }
//        System.out.println("WebGetTemporalPageLinks");
        
//        System.out.println("WebBatchGetTemporalPageLinks...");
//        String[] outLinks = client.ClientBatchGetTemporalPageLinks(sample, firstTime, lastTime, SHS_Client.FORWARD);
//        for (String outLink : outLinks) {
//            System.out.println(outLink);
//        }
//        System.out.println("Found " + outLinks.length + " links ");
//        System.out.println("WebBatchGetTemporalPageLinks.");
        
//        System.out.println("BatchUrlToUid...");
//        long[] uids = client.BatchUrlToUid(sample);
//        for (int u = 0; u < uids.length; u ++) {
//            System.out.println(uids[u]);
//        }
//        System.out.println("BatchUrlToUid.");
//        
//        System.out.println("BatchUidToUrl: ");
//        urls = client.BatchUidToUrl(uids);
//        for (int u = 0; u < urls.length; u ++) {
//            System.out.println(urls[u]);
//        }
//        System.out.println("BatchUidToUrl.");
//        
//        System.out.println("WebGetTemporalLinks");
//        long[] uids_1 = client.WebGetTemporalLinks(uid, firstTime, lastTime, SHS_Client.FORWARD);
//        for (int u = 0; u < uids_1.length; u ++) {
//            System.out.println(uids_1[u]);
//        }
//        System.out.println("WebGetTemporalLinks");
        
//        System.out.println("WebBatchGetTemporalLinks");
//        long[][] outLinks_1 = client.WebBatchGetTemporalLinks(uids, firstTime, lastTime, SHS_Client.BACKWARD);
//        for (long[] outLinks_11 : outLinks_1) {
//            for (int k = 0; k < outLinks_11.length; k ++) {
//                System.out.println(outLinks_11[k]);
//            }
//        }
//        System.out.println("WebBatchGetTemporalLinks");
        
        uid = client.ClientUrlToUid("bild.de/");
        System.out.println("UID = " + uid);
        System.out.println("WebBatchGetTemporalPageLinks...");
        long[][] outLinks = client.ClientGetAllCaptures(uid, SHS_Client.FORWARD);
        System.out.println("Found " + outLinks.length + " links ");
        for (int i = 0; i < outLinks.length; i++) {
            
            System.out.print("Time: " + client.ClientConvertDateFromNum((int)outLinks[i][0]) + "\t");
            for (int j = 1; j < outLinks[i].length; j++) {
                System.out.print(outLinks[i][j] + " ");
            }
            System.out.println();
        }
        
        System.out.println("WebBatchGetTemporalPageLinks.");
    }
}