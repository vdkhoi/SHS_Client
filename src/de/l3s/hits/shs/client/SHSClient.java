package de.l3s.hits.shs.client;

import java.util.*;

/**
 * @author vdkhoi
 */
public class SHSClient {
    public static final String DEFAULT_LEADER = "master.ib";
    public static final String DEFAULT_BROKER = "node28.ib";
    public static final int DEFAULT_PORT = 10001;
    public static final String DEFAULT_STORE_ID = "ab24faa863874d3ab4fb12e92e7829b2";

    private String leader;
    private String broker;
    private int port;
    private String storeID;
    private Boolean revert;
    private Channel channel;

    public SHSClient() {
        this(DEFAULT_LEADER, DEFAULT_BROKER, DEFAULT_PORT, DEFAULT_STORE_ID, true);
    }

    public SHSClient(String broker) {
        this(DEFAULT_LEADER, broker, DEFAULT_PORT, DEFAULT_STORE_ID, true);
    }

    public SHSClient(String leader, String broker, int port, String storeID) {
        this(leader, broker, port, storeID, true);
    }

    public SHSClient(String leader, String broker, int port, String storeID, Boolean revert) {
        this.leader = leader;
        this.broker = broker;
        this.port = port;
        this.storeID = storeID;
        this.revert = revert;
    }

    public static long getTimestampFromDays(long days) {
        // (number of days between 1970-01-01 and 1998-01-01 + days) * seconds in day * milliseconds in second
        return (10227 + days) * 86400 * 1000;
    }

    /**
     * @param url A url as it stored in index (without protocol and www prefix)
     * @return A UID of the url in SHS storage
     */
    public long getUid(String url) {
        Channel channel = getChannel();
        channel.WriteUInt32(OpCodes.ClientUrlToUid);
        channel.WriteString(leader);
        channel.WriteString(storeID);
        channel.WriteString(url);
        channel.Flush();

        long uid = channel.ReadInt64();
        return uid;
    }

    /**
     * @param urls Array of urls as they stored in index (without protocol and www prefix)
     * @return Array of UIDs which belongs to given urls
     */
    public long[] getUid(String[] urls) {
        Channel channel = getChannel();
        channel.WriteUInt32(OpCodes.ClientBatchUrlToUid);
        channel.WriteString(leader);
        channel.WriteString(storeID);
        channel.WriteInt32(urls.length);
        for (String url : urls) {
            channel.WriteString(url);
        }
        channel.Flush();

        long[] uids = new long[urls.length];
        for (int i = 0; i < urls.length; i++) {
            uids[i] = channel.ReadInt64();
        }
        return uids;
    }

    /**
     * @param uid A UID of the url
     * @return An original url retried from SHS storage by given UID
     */
    public String getUrl(long uid) {
        Channel channel = getChannel();
        channel.WriteUInt32(OpCodes.ClientUidToUrl);
        channel.WriteString(leader);
        channel.WriteString(storeID);
        channel.WriteInt64(uid);
        channel.Flush();

        String url = channel.ReadString();
        return url;
    }

    /**
     * @param uids Array of UIDs
     * @return Array of urls retried from SHS storage by given UIDs
     */
    public String[] getUrl(long[] uids) {
        Channel channel = getChannel();
        channel.WriteUInt32(OpCodes.ClientBatchUidToUrl);
        channel.WriteString(leader);
        channel.WriteString(storeID);
        channel.WriteInt32(uids.length);
        for (long uid : uids) {
            channel.WriteInt64(uid);
        }
        channel.Flush();

        String[] urls = new String[uids.length];
        for (int i = 0; i < uids.length; i++) {
            urls[i] = channel.ReadString();
        }
        return urls;
    }

    /**
     * @param url A url as it stored in index (without protocol and www prefix)
     * @param firstTime A time from
     * @param lastTime A time to
     * @param direction A direction to lookup
     * @return An array of all incoming/outgoing links from given url that was captured between given time
     */
    public String[] getTemporalLinks(String url, String firstTime, String lastTime, Direction direction) {
        Channel channel = getChannel();
        channel.WriteUInt32(OpCodes.ClientGetTemporalPageLinks);
        channel.WriteString(leader);
        channel.WriteString(storeID);
        channel.WriteString(url);
        channel.WriteString(firstTime);
        channel.WriteString(lastTime);
        channel.WriteInt32(direction.getValue());
        channel.Flush();

        int n = channel.ReadInt32();
        String[] urls = null;
        if (n > -1) {
            urls = new String[n];
            for (int i = 0; i < n; i++) {
                urls[i] = channel.ReadString();
            }
        }
        return urls;
    }

    /**
     * TODO: this method works not as expected.
     *
     * @param urls An array of urls as they stored in index (without protocol and www prefix)
     * @param firstTime A time from
     * @param lastTime A time to
     * @param direction A direction to lookup
     * @return An array of all incoming/outgoing links for each given url that was captured between given time
     */
    public String[][] getTemporalLinks(String[] urls, String firstTime, String lastTime, Direction direction) {
        Channel channel = getChannel();
        channel.WriteUInt32(OpCodes.ClientBatchGetTemporalPageLinks);
        channel.WriteString(leader);
        channel.WriteString(storeID);
        channel.WriteInt32(direction.getValue());
        channel.WriteString(firstTime);
        channel.WriteString(lastTime);
        channel.WriteInt32(urls.length);
        for (String url : urls) {
            channel.WriteString(url);
        }
        channel.Flush();

        //int foundList = channel.ReadInt32();
        String[][] links = new String[urls.length][];
        for (int u = 0; u < urls.length; u++) {
            int n = channel.ReadInt32();
            links[u] = new String[n];
            for (int i = 0; i < n; i++) {
                links[u][i] = channel.ReadString();
            }
        }

//        int foundList = channel.ReadInt32();
//        String[] links = new String[foundList];
//        for (int u = 0; u < foundList; u++) {
//            links[u] = channel.ReadString();
//        }
        return links;
    }

    /**
     * @param uid A UID of the url
     * @param firstTime A time from
     * @param lastTime A time to
     * @param direction A direction to lookup
     * @return An array of all incoming/outgoing UIDs (links) from given UID (link) that was captured between given time
     */
    public long[] getTemporalLinks(long uid, String firstTime, String lastTime, Direction direction) {
        Channel channel = getChannel();
        channel.WriteUInt32(OpCodes.ClientGetTemporalLinks);
        channel.WriteString(leader);
        channel.WriteString(storeID);
        channel.WriteInt64(uid);
        channel.WriteString(firstTime);
        channel.WriteString(lastTime);
        channel.WriteInt32(direction.getValue());
        channel.Flush();

        int n = channel.ReadInt32();
        long[] uids = null;
        if (n > -1) {
            uids = new long[n];
            for (int i = 0; i < n; i++) {
                uids[i] = channel.ReadInt64();
            }
        }
        return uids;
    }

    /**
     * @param uids Array of UIDs of the urls
     * @param firstTime A time from
     * @param lastTime A time to
     * @param direction A direction to lookup
     * @return An array of all incoming/outgoing UIDs (links) for each given UIDs (links) that was captured between given time
     */
    public long[][] getTemporalLinks(long[] uids, String firstTime, String lastTime, Direction direction) {
        Channel channel = getChannel();
        channel.WriteUInt32(OpCodes.ClientBatchGetTemporalLinks);
        channel.WriteString(leader);
        channel.WriteString(storeID);
        channel.WriteInt32(direction.getValue());
        channel.WriteString(firstTime);
        channel.WriteString(lastTime);
        channel.WriteInt32(uids.length);
        for (long uid : uids) {
            channel.WriteInt64(uid);
        }
        channel.Flush();
        //int positiveLinks = channel.ReadInt32();
        long[][] links = new long[uids.length][];
        for (int u = 0; u < uids.length; u++) {
            int n = channel.ReadInt32();
            links[u] = new long[n];
            for (int i = 0; i < n; i++) {
                links[u][i] = channel.ReadInt64();
            }
        }
        return links;
    }

    /**
     * @param uid A UID of the url
     * @param direction A direction to lookup
     * @return
     */
    public long[][] getAllCaptures(long uid, Direction direction) {
        Channel channel = getChannel();
        channel.WriteUInt32(OpCodes.ClientGetAllCaptures);
        channel.WriteString(leader);
        channel.WriteString(storeID);
        channel.WriteInt32(direction.getValue());
        channel.WriteInt64(uid);
        channel.Flush();

        int numOfRevs = channel.ReadInt32();
        long[][] revisions = new long[numOfRevs][];
        for (int i = 0; i < numOfRevs; i++) {
            int linkLength = channel.ReadInt32();
            revisions[i] = new long[linkLength];
            for (int j = 0; j < linkLength; j++) {
                revisions[i][j] = channel.ReadInt64();
            }
        }
        return revisions;
    }

    /**
     * @param uid A UID of the url
     * @return
     */
    public long[] getFirstAndLastIncomingLinks(long uid) {
        long[][] allCaptures = getAllCaptures(uid, Direction.INCOMING);

        long[] captures = new long[2];
        captures[0] = getTimestampFromDays(allCaptures[0][0]);
        captures[1] = getTimestampFromDays(allCaptures[allCaptures.length - 1][0]);
        return captures;
    }

    /**
     * @param uid A UID of the url
     * @return
     */
    public int getPageAgeInSeconds(long uid) {
        long[][] allCaptures = getAllCaptures(uid, Direction.INCOMING);

        if (allCaptures.length < 2)
            return 0;

        return Math.toIntExact((getTimestampFromDays(allCaptures[allCaptures.length - 1][0]) - getTimestampFromDays(allCaptures[0][0])) / 1000);
    }

    /**
     * @param uid A UID of the url
     * @param direction A direction to lookup
     * @return
     */
    public Map<Long, List<Long>> getAllCapturesGroupByLinks(long uid, Direction direction) {
        Channel channel = getChannel();
        channel.WriteUInt32(OpCodes.ClientGetAllCaptures);
        channel.WriteString(leader);
        channel.WriteString(storeID);
        channel.WriteInt32(direction.getValue());
        channel.WriteInt64(uid);
        channel.Flush();

        try {
            Map<Long, List<Long>> map = new HashMap<>();
            int numOfRevs = channel.ReadInt32();
            for (int i = 0; i < numOfRevs; i++) {
                int numOfLinks = channel.ReadInt32() - 1;
                long rev = getTimestampFromDays(channel.ReadInt64());

                for (int j = 0; j < numOfLinks; j++) {
                    long link = channel.ReadInt64();
                    if (link != uid) { // remove links to the same page
                        if (!map.containsKey(link)) {
                            map.put(link, new ArrayList<>());
                        }
                        map.get(link).add(rev);
                    }
                }
            }
            return map;
        } catch (Exception e) {
            System.out.println(uid);
        }

        return null;
    }

    private Channel getChannel() {
        if (channel == null) {
            channel = new Channel(broker, port);
            channel.needRevert = revert;
        }

        return channel;
    }

    public void close() {
        channel.Close();
        channel = null;
    }

    /**
     * @param uid The UID of a page from/to which we need links
     * @param direction Is a FORWARD/BACKWARD (outgoing/incoming) value
     * @param limit The max number of links that should be returned (selected randomly)
     *              if `null` given, no limit should be set, return all links
     * @param startDate Filter by date, only links that existed in period since `startDate` and until `endDate`,
     *                  if `null` given then only endDate should be checked
     * @param endDate Filter by date, works in pair with startDate,
     *                if `null` given then only startDate should be checked
     * @param includeInternal If set to `false`, then links from same domain should not be included,
     *                        default is `true` and means include links from any domain
     * @return An array of UID of incoming/outgoing links
     */
    public long[] getCapturedLinks(long uid, Direction direction, Long limit, Date startDate, Date endDate, boolean includeInternal) {
        return null;
    }

    /**
     * @param uids The same method as above but supports butch of UIDs
     * @param direction Is a FORWARD/BACKWARD (outgoing/incoming) value
     * @param limit The max number of links that should be returned (selected randomly)
     *              if `null` given, no limit should be set, return all links
     * @param startDate Filter by date, only links that existed in period since `startDate` and until `endDate`,
     *                  if `null` given then only endDate should be checked
     * @param endDate Filter by date, works in pair with startDate,
     *                if `null` given then only startDate should be checked
     * @return An array of arrays of UIDs of incoming/outgoing links
     * @param includeInternal If set to `false`, then links from same domain should not be included,
     *                        default is `true` and means include links from any domain
     */
    public long[][] getCapturedLinks(long uids[], Direction direction, Long limit, Date startDate, Date endDate, boolean includeInternal) {
        return null;
    }

    /**
     * As above but without direction, return both incoming and outgoing links
     *
     * @param uids The same method as above but supports butch of UIDs
     * @param limit The max number of links that should be returned (selected randomly)
     *              if `null` given, no limit should be set, return all links
     * @param startDate Filter by date, only links that existed in period since `startDate` and until `endDate`,
     *                  if `null` given then only endDate should be checked
     * @param endDate Filter by date, works in pair with startDate,
     *                if `null` given then only startDate should be checked
     @param includeInternal If set to `false`, then links from same domain should not be included,
     *                        default is `true` and means include links from any domain
     * @return Returns array with two values, in first value an array of arrays of incoming links, second value is for outgoing links
     */
    public long[][][] getCapturedLinks(long uids[], Long limit, Date startDate, Date endDate, boolean includeInternal) {
        return null;
    }
}