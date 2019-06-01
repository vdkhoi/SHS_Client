package de.l3s.hits.shs.client;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


/**
 * @author vdkhoi
 */
public class LoadPartitions {
    public static void main(String[] agrs) throws Exception{
//        String[] sample = { "aev-forum.de/spiele-tipps-f81/2-pre-play-off-spiel-2008-09-grizzly-t32902/seite4.html",
//                            "adfarm.mediaplex.com/ad/ck/707-1170-4560-4/?redirectenter&partner=35087&loc=http://cgi.ebay.de/ws/ebayisapi.dll%3fviewitem%26item=2050588731",
//                            "ailingen.de/test/index.php?module=postcalendar&func=view&tplview=&viewtype=day&date=20050106&pc_username=&pc_category=&pc_topic=&print=1",
//                            "agriaffaires.it/usato/trattori-agricoli/835995/massey-ferguson-2640.html",
//                            "adsdeck.de/autos/go/6449307?source=70&robot=1",
//                            "arbeitsschutz.nrw.de/staefa/essen/impressum.html",
//                            "admcity.jp/bodystocking-10/3651167.shtml",
//                            "80.252.100.176/cgi-bin/ebtc?http://cgi.ebay.de/ws/ebayisapi.dll?viewitem&category=2509&item=6915734653&rd=1&rcl=af1",
//                            "4-color.com/emu.info.htm",
//                            "autohaendler-in-deutschland.de/tankstellen/chemnitz_2.html",
//                            "anonym.to/?http://www.pennergame.de/change_please/6274374/",
//                            "autoanzeigen.de/isa/autoanzeigen/showarticle?article=753525&fcms=6b1b5828eeea4679b6948c53bdc362ea",
//                            "ads.vrm.de/www/delivery/ck.php?n=1373214787000",
//                            "adserver.adtech.de/adlink%7c3.0%7c59%7c1935563%7c0%7c225%7cadtech;loc=300;grp=1339211853",
//                            "alldir.de/reisen-touristik/hotels/page-2.html",
//                            "aktportrait.slippinn.de/",
//                            "alfaprague-uver.czechtrade.es/",
//                            "anal.tuerk.eroticseiten.de/ab-50.htm",
//                            "anime.paar-sucht-ihn.slippi.de/gaybilder.htm",
//                            "az-online.de/lokales/altmarkkreis-salzwedel/salzwedel/index-10.html",
//                            "ariasat.de/shop.php/sid/42f5478870b3034e7ea2d6a3b6afd880/cl/alist/cnid/b174045a75ccc35e8.17967041",
//                            "aeg.rv.bw.schule.de/seminar/98/freiheit/noframes/linkliste_schunter.html",
//                            "ad.de.doubleclick.net/jump/oms.express.de/nationalnews/welt;oms=nationalnews/welt;nielsen=2;sz=728x90;tile=1;ord=1289686087?",
//                            "an-auto.de/anstart_turkish.html",
//                            "adult-spider.de/behaartefrauen.html",
//                            "alabamahalle.de/gallery/album62/dsc09777?full=0",
//                            "amazon.de/gp/product/3894495863%3ftag=preispiraten-21%26link_code=xm2%26camp=2025%26dev-t=d11ybkvmjzu5le",
//                            "augsburger-allgemeine.de/freizeit/veranstaltungen/querbeet-st-sixtus-reisensburg-id21131086.html",
//                            "preisknaller.de/2009/04/wp-auctions/"};
        //List<String> readURL = Files.readAllLines(Paths.get("E:\\OneDrive - VietNam National University - HCM INTERNATIONAL UNIVERSITY\\Other_Projects\\SHSClient\\dist\\initial_urls.txt"));
        
        List<String> readURL = Files.readAllLines(Paths.get("initial_urls.txt"));
        List<String> urlList = new ArrayList();
        List<String> errorServer = new ArrayList();
        for(int r = 0; r < readURL.size(); r++) {
            String[] iURL = readURL.get(r).split("\t");
            
            for (int i = 0; i < 500; i++) {
                //if (r == 24)
                //    errorServer.add(iURL[i]);
                //else
                    urlList.add(iURL[i]);
            }
        }
        String[] sample = new String[urlList.size()];
        sample = urlList.toArray(sample);
        String firstTime = "2010-01-01";
        String lastTime = "2013-02-15";

        System.out.println("Start connection...");
        //SHSClient client = new SHSClient("master.ib", "master03.ib", "ab24faa863874d3ab4fb12e92e7829b2");
        //SHSClient client = new SHSClient("master.ib", "localhost", "ab24faa863874d3ab4fb12e92e7829b2");
        SHSClient client = new SHSClient();

        System.out.println("Sample size: " + sample.length);
        long[] uids = client.getUid(sample);
        String[] urls = client.getUrl(uids);
        System.out.println("Received sample: " + Arrays.toString(urls));
        System.out.print(Arrays.toString(uids));
        System.out.println();
//        for (long uid : uids) {
//            long[] tlink = client.getTemporalLinks(uid, firstTime, lastTime, Direction.FORWARD);
//            System.out.println(Arrays.toString(tlink));
//        }
        long[][] forward = client.getTemporalLinks(uids, firstTime, lastTime, Direction.FORWARD);
        System.out.println("Forward links:");
        for (long[] result: forward) {
            if (result != null) {
                if (result.length > 0)
                    System.out.println("At time " + result[0] + ": " + (result.length - 1) + " links");
            }
        }
        System.out.println("Backward links:");
        long[][] backward = client.getTemporalLinks(uids, firstTime, lastTime, Direction.BACKWARD);
        for (long[] result: backward) {
            
            if (result != null) {
                if (result.length > 0)
                    System.out.println("At time " + result[0] + ": " + (result.length - 1) + " links");
            }
        }
        for (long[] result: forward) {
            if (result != null) {
                if (result.length > 0) {
                    String[] test = client.getUrl(result);
                    System.out.println(Arrays.toString(test));
                }
            }
        }
        
        for (long[] result: backward) {
            if (result != null) {
                if (result.length > 0) {
                    String[] test = client.getUrl(result);
                    System.out.println(Arrays.toString(test));
                }
            }
        }
    }
}