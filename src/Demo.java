import Library.SHSClient;

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

        String broker = "DESKTOP-IKH61D8";  // machine to run broker
        String leader = "DESKTOP-R1US14S";  // machine to run leader server
        // String storeID = "a2076350e64b4aa89eb75449ed263117";
        // String storeID = "80d2b061d2aa42c5a32a67c1a7559790";
        String storeID = "5f8a21aa29fb4e23a3980faac6910350";

        if (agrs.length == 3) {
            broker = agrs[0];
            leader = agrs[1];
            storeID = agrs[2];
        }

        System.out.println("Start connection...");
        SHSClient client = new SHSClient(broker, leader, storeID);

        long uid = client.ClientUrlToUid(sample[0]);
        long uid2 = client.ClientUrlToUid(sample[1]);
        System.out.println("UrlToUid=" + uid);
        System.out.println("UrlToUid=" + uid2);

        // System.out.println("UidToUrl: " + uid);
        // String url = client.ClientUidToUrl(uid);
        // System.out.println("UidToUrl=" + url);
        //
        // System.out.println("WebGetTemporalPageLinks: ");
        // String[] urls = client.WebGetTemporalPageLinks(sample[0], firstTime, lastTime, SHSClient.FORWARD);
        // for (String url1 : urls) {
        //    System.out.println(url1);
        // }
        // System.out.println("WebGetTemporalPageLinks");
        //
        // System.out.println("WebBatchGetTemporalPageLinks...");
        // String[] outLinks = client.ClientBatchGetTemporalPageLinks(sample, firstTime, lastTime, SHSClient.FORWARD);
        // for (String outLink : outLinks) {
        //    System.out.println(outLink);
        // }
        // System.out.println("Found " + outLinks.length + " links ");
        // System.out.println("WebBatchGetTemporalPageLinks.");
        //
        // System.out.println("BatchUrlToUid...");
        // long[] uids = client.BatchUrlToUid(sample);
        // for (int u = 0; u < uids.length; u ++) {
        //    System.out.println(uids[u]);
        // }
        // System.out.println("BatchUrlToUid.");
        //
        // System.out.println("BatchUidToUrl: ");
        // urls = client.BatchUidToUrl(uids);
        // for (int u = 0; u < urls.length; u ++) {
        //    System.out.println(urls[u]);
        // }
        // System.out.println("BatchUidToUrl.");
        //
        // System.out.println("WebGetTemporalLinks");
        // long[] uids_1 = client.WebGetTemporalLinks(uid, firstTime, lastTime, SHSClient.FORWARD);
        // for (int u = 0; u < uids_1.length; u ++) {
        //    System.out.println(uids_1[u]);
        // }
        // System.out.println("WebGetTemporalLinks");
        //
        // System.out.println("WebBatchGetTemporalLinks");
        // long[][] outLinks_1 = client.WebBatchGetTemporalLinks(uids, firstTime, lastTime, SHSClient.BACKWARD);
        // for (long[] outLinks_11 : outLinks_1) {
        //    for (int k = 0; k < outLinks_11.length; k ++) {
        //        System.out.println(outLinks_11[k]);
        //    }
        // }
        // System.out.println("WebBatchGetTemporalLinks");
        //
        // uid = client.ClientUrlToUid("bild.de/");
        // System.out.println("UID = " + uid);
        // System.out.println("WebBatchGetTemporalPageLinks...");
        // long[][] outLinks = client.ClientGetAllCaptures(uid, SHSClient.FORWARD);
        // System.out.println("Found " + outLinks.length + " links ");
        // for (int i = 0; i < outLinks.length; i++) {
        //
        //    System.out.print("Time: " + client.ClientConvertDateFromNum((int)outLinks[i][0]) + "\t");
        //    for (int j = 1; j < outLinks[i].length; j++) {
        //        System.out.print(outLinks[i][j] + " ");
        //    }
        //    System.out.println();
        // }
        //
        // System.out.println("WebBatchGetTemporalPageLinks.");
        //
        // Long[] uids = client.ClientAllUids();
        // for (Long u: uids) {
        //     if (u % 10000 == 0) {
        //         System.out.println("Capture of uid = " + u);
        //         long [][] captures = client.ClientGetAllCaptures(u, SHSClient.FORWARD);
        //         for (int i = 0; i < captures.length; i ++) {
        //             for(int j = 0; j < captures[i].length; j++) {
        //                 System.out.print("\t" + captures[i][j]);
        //             }
        //             System.out.println();
        //         }
        //     }
        // }
    }
}