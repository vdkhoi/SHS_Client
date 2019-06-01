package de.l3s.hits.shs.client;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * @author vdkhoi
 */
public class Demo {
    public static void main(String[] agrs) {
//        String[] sample = { "sueddeutsche.de/",
//                            "bild.de/",
//                            "angela-merkel.de/", 
//                            "spiegel.de/", 
//                            "zdf.de/",
//                            "geileseite.erotic6sex.de/erotikliste/sexzeitung/moepse.html",
//                            "krankenversicherungen-vergleich-neu.de/weihnachtsgeschenke/alte-digitaltechnik.htm",
//                            "zensiert.pg4u.de/schwarzes_board_show.php?sid=ggb222jt14uvu90p79gct83ff2&bewerten=ok&note=3&id=10010",
//                            "del.icio.us/post?v=4&noui&jump=close&url=http://garmisch-partenkirchen.markt.de/templates/default.jsp?category=3&title=immobilien",
//                            "gymnasium-blomberg.de/index.php?tag=musik",
//                            "sedoparking.com/parking.php4?domain=liga-racing.de&s=5f64528f74af703c2ade&task=search&language=e&registrar=&keyword=boxing&tracked=&partnerid=&language=e",
//                            "philips.de/medizin/workshop/wissensbasis/ws_drg_tools.html",
//                            "uj9632613.auktion-hq.de/et1025717.html",
//                            "rees-aktuell.de/index.php?kid=&vid=&email=&go_search=1&search_str=adidas&lang=ger&portal=3"
//                            };
        String[] sample = {"sueddeutsche.de/"};
        String firstTime = "2013-01-01";
        String lastTime = "2013-02-15";

        System.out.println("Start connection...");
        //SHSClient client = new SHSClient("master.ib", "master03.ib", "ab24faa863874d3ab4fb12e92e7829b2");
        SHSClient client = new SHSClient();

//        System.out.println("UrlToUid: " + sample[0]);
//        long uid = client.getUid(sample[0]);
//        System.out.println("UrlToUid=" + uid);

        // long uid = client.getUid("heise.de/");

        // final long startTime = System.currentTimeMillis();
        // Map<Long, List<Long>> outgoingLinksBild = client.getAllCapturesGroupByLinks(uid, Direction.FORWARD);
        // final long elapsedTime = System.currentTimeMillis() - startTime;
        // System.out.println("Total links " + outgoingLinksBild.size());
        // System.out.println("Elapsed time: " + elapsedTime + " ms.");
        // // System.out.println("Found UID: " + uid);
        //
        // long uid = client.getUid("bild.de/");
        // long uid2 = client.getUid("angela-merkel.de/");

        long debugUid = 34543864987L;
        //System.out.println("getAllCaptures philips.de/medizin/workshop/wissensbasis/ws_drg_tools.html");
        //long philipsUid = client.getUid("philips.de/medizin/workshop/wissensbasis/ws_drg_tools.html");
        //System.out.println("getAllCaptures..." + philipsUid);
        
        long startTime = System.nanoTime();
        
        long[][] outLinksPhilips = client.getAllCaptures(debugUid, Direction.BACKWARD);
        
        long backTime = System.nanoTime();
        
        long numLinks = 0;
        for (long[] rev : outLinksPhilips) {
            numLinks += (rev.length - 1);
        }
        long backLinks = numLinks;
        backTime = backTime - startTime;
        
        startTime = System.nanoTime();
        long[][] inLinksPhilips = client.getAllCaptures(debugUid, Direction.FORWARD);
        
        long forwTime = System.nanoTime();
        numLinks = 0;
        for (long[] rev : inLinksPhilips) {
            numLinks += (rev.length - 1);
        }
        long forwLinks = numLinks;
        forwTime = forwTime - startTime;
        
        System.out.println("Backward time: " + backTime);
        System.out.println("Backward links: " + backLinks);
        System.out.println("Forward time: " + forwTime);
        System.out.println("Forward links: " + forwLinks);
        //System.out.println("Found " + outLinksPhilips.length + " links ");
        //for (long[] longs : outLinksPhilips) {
        //    System.out.print("Time: " + TimeUtils.getFormattedDate(SHSClient.getTimestampFromDays(longs[0])) + "\t");
        //    System.out.println("Links: " + longs.length);
//            for (int j = 1; j < longs.length; j++) {
//                System.out.print(longs[j] + " ");
//            }
//            System.out.println();
//        }
//        System.out.println("getAllCaptures philips.de/medizin/workshop/wissenbasic/ws_drg_tools.html done.");
//        
//        long[] uids = client.getUid(sample);
//        System.out.println("Process sample: " + Arrays.toString(sample));
//        System.out.print(Arrays.toString(uids));
//        System.out.println();
//        for (long u : uids) {
//            if (u >= 0) {
//                long[][] sampleLinks = client.getAllCaptures(u, Direction.FORWARD);
//                for(long[] cap : sampleLinks) {
//                    System.out.println(TimeUtils.getFormattedDate(cap[0]) + ": " + (cap.length - 1) + " forward revisions");
//                }
//                System.out.println();
//                sampleLinks = client.getAllCaptures(u, Direction.BACKWARD);
//                for(long[] cap : sampleLinks) {
//                    System.out.println(TimeUtils.getFormattedDate(cap[0]) + ": " + (cap.length - 1) + " backward revisions");
//                }
//                System.out.println();
//            }
//        }

        
//        System.out.println("UrlToUid: " + Arrays.toString(sample));
//        long[] uids = client.getUid(sample);
//        for (long uid1 : uids) {
//            System.out.println(uid1);
//        }
//        System.out.println("UrlToUid done.");
//
//
//        System.out.println("UidToUrl: " + uid);
//        String url = client.getUrl(uid);
//        System.out.println("UidToUrl=" + url);
//
//        System.out.println("UidToUrl: " + Arrays.toString(uids));
//        String[] urls = client.getUrl(uids);
//        for (String url1 : urls) {
//            System.out.println(url1);
//        }
//        System.out.println("UidToUrl done.");
//
//        for(int i = 0; i < sample.length; i ++) {
//            System.out.println("GetTemporalLinks: forward for" + sample[i]);
//            urls = client.getTemporalLinks(sample[i], firstTime, lastTime, Direction.FORWARD);
//            System.out.println(Arrays.toString(urls));
//            System.out.println("GetTemporalLinks: backward for" + sample[i]);
//            urls = client.getTemporalLinks(sample[i], firstTime, lastTime, Direction.BACKWARD);
//            System.out.println(Arrays.toString(urls));
//        }
//        System.out.println("GetTemporalLinks done.");
//
//        System.out.println("GetTemporalLinks: for " + Arrays.toString(sample));
//        String[][] outLinksMap = client.getTemporalLinks(sample, firstTime, lastTime, Direction.FORWARD);
//        long count = 0;
//        for (String[] outLinks : outLinksMap) {
//            count += outLinks.length;
//            System.out.println(Arrays.toString(outLinks));
//        }
//        System.out.println("Found " + outLinksMap.length + " links for " + sample.length + " urls.");
//        System.out.println("GetTemporalLinks done.");
//
//
//        System.out.println("GetTemporalLinks: for " + uid);
//        long[] uids_1 = client.getTemporalLinks(uid, firstTime, lastTime, Direction.FORWARD);
//        for (long l : uids_1) {
//            System.out.println(l);
//        }
//        System.out.println("GetTemporalLinks done.");
//
//
//        System.out.println("GetTemporalLinks: for " + Arrays.toString(uids));
//        long[][] outLinks_1 = client.getTemporalLinks(uids, firstTime, lastTime, Direction.BACKWARD);
//        long count2 = 0;
//        for (long[] outLinks : outLinks_1) {
//            count2 += outLinks.length;
//            System.out.println(Arrays.toString(outLinks));
//        }
//        System.out.println("Found " + count2 + " links for " + outLinks_1.length + " uids.");
//        System.out.println("GetTemporalLinks done.");

        

//        long bildUid = client.getUid("bild.de/");
//        System.out.println("getAllCaptures...");
//        long[][] outLinksBild = client.getAllCaptures(bildUid, Direction.FORWARD);
//        System.out.println("Found " + outLinksBild.length + " links ");
//        for (long[] longs : outLinksBild) {
//            System.out.print("Time: " + TimeUtils.getFormattedDate(SHSClient.getTimestampFromDays(longs[0])) + "\t");
//            for (int j = 1; j < longs.length; j++) {
//                System.out.print(longs[j] + " ");
//            }
//            System.out.println();
//        }
//        System.out.println("getAllCaptures done.");
//
//        System.out.println("getAllCapturesGroupByLinks for " + bildUid);
//        Map<Long, List<Long>> outgoingLinksBild = client.getAllCapturesGroupByLinks(bildUid, Direction.FORWARD);
//        System.out.println("Found " + outgoingLinksBild.size() + " links");
//        for (Map.Entry<Long, List<Long>> entry : outgoingLinksBild.entrySet()) {
//            System.out.print("Uid: " + entry.getKey() + "\t");
//
//            for (Long value : entry.getValue()) {
//                System.out.print(TimeUtils.getFormattedDate(value) + " ");
//            }
//            System.out.println();
//        }
//        System.out.println("getAllCapturesGroupByLinks done.");

        

        
//        System.out.println("Activate all partitions");
//        for(int i = 0; i < 29; i ++) {
//            uids = client.getSampleUidsAtPartition(i, 5);
//            outLinks_1 = client.getTemporalLinks(uids, firstTime, lastTime, Direction.FORWARD);
//            count2 = 0;
//            for (long[] outLinks : outLinks_1) {
//                count2 += outLinks.length;
//                System.out.println(Arrays.toString(outLinks));
//            }
//            System.out.println("Found " + count2 + " forward links for " + outLinks_1.length + " uids.");
//            
//            outLinks_1 = client.getTemporalLinks(uids, firstTime, lastTime, Direction.BACKWARD);
//            count2 = 0;
//            for (long[] outLinks : outLinks_1) {
//                count2 += outLinks.length;
//                System.out.println(Arrays.toString(outLinks));
//            }
//            System.out.println("Found " + count2 + " backward links for " + outLinks_1.length + " uids.");
//        }
//        System.out.println("All partitions activated.");

    }
}