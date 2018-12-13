package Library;

import java.util.*;

/**
 * @author vdkhoi
 */
public class SHSClient {
    private static final String DEFAULT_BROKER = "master03.ib";
    private static final String DEFAULT_LEADER = "master.ib";
    private static final int DEFAULT_PORT = 10001;
    private static final String DEFAULT_STORE_ID = "5f8a21aa29fb4e23a3980faac6910350";

    public static final int FORWARD = 0;
    public static final int BACKWARD = 1;

    private Channel channel;
    private String leader;
    private String storeID;

    public SHSClient() {
        this(DEFAULT_BROKER, DEFAULT_LEADER, DEFAULT_PORT, DEFAULT_STORE_ID, true);
    }

    public SHSClient(String broker, String leader, String storeID) {
        this(broker, leader, DEFAULT_PORT, storeID, true);
    }

    public SHSClient(String broker, String leader, int port, String storeID, Boolean revert) {
        this.leader = leader;
        this.storeID = storeID;
        channel = new Channel(broker, port);
        channel.needRevert = revert;
    }

    public static long getTimestampFromDays(long days) {
        // ((number of days between 1970-01-01 and 1998-01-01) + days) * seconds in day * milliseconds in second
        return (10227 + days) * 86400 * 1000;
    }

    public Date ClientConvertDateFromNum(int num) {
        return new Date(getTimestampFromDays(num));
    }

    public String[] ClientGetTemporalPageLinks(String url, String firstTime, String lastTime, int direction) {
        channel.WriteUInt32(OpCodes.ClientGetTemporalPageLinks);
        channel.WriteString(leader);
        channel.WriteString(storeID);
        channel.WriteString(url);
        channel.WriteString(firstTime);
        channel.WriteString(lastTime);
        channel.WriteInt32(direction);
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

    public String[] ClientBatchGetTemporalPageLinks(String[] urls, String firstTime, String lastTime, int direction) {
        channel.WriteUInt32(OpCodes.ClientBatchGetTemporalPageLinks);
        channel.WriteString(leader);
        channel.WriteString(storeID);
        channel.WriteInt32(direction);
        channel.WriteString(firstTime);
        channel.WriteString(lastTime);
        channel.WriteInt32(urls.length);
        for (int u = 0; u < urls.length; u++) {
            channel.WriteString(urls[u]);
        }
        channel.Flush();
        int foundList = channel.ReadInt32();
        String[] links = new String[foundList];
        for (int u = 0; u < foundList; u++) {
            links[u] = channel.ReadString();
        }
        return links;
    }

    public long[] ClientGetTemporalLinks(long uid, String firstTime, String lastTime, int direction) {
        channel.WriteUInt32(OpCodes.ClientGetTemporalLinks);
        channel.WriteString(leader);
        channel.WriteString(storeID);
        channel.WriteInt64(uid);
        channel.WriteString(firstTime);
        channel.WriteString(lastTime);
        channel.WriteInt32(direction);
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

    public long[][] ClientBatchGetTemporalLinks(long[] uids, String firstTime, String lastTime, int direction) {
        channel.WriteUInt32(OpCodes.ClientBatchGetTemporalLinks);
        channel.WriteString(leader);
        channel.WriteString(storeID);
        channel.WriteInt32(direction);
        channel.WriteString(firstTime);
        channel.WriteString(lastTime);
        channel.WriteInt32(uids.length);
        for (int u = 0; u < uids.length; u++) {
            channel.WriteInt64(uids[u]);
        }
        channel.Flush();
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

    public long ClientUrlToUid(String url) {
        channel.WriteUInt32(OpCodes.ClientUrlToUid);
        channel.WriteString(leader);
        channel.WriteString(storeID);
        channel.WriteString(url);
        channel.Flush();
        long uid = channel.ReadInt64();
        return uid;
    }

    public String ClientUidToUrl(long uid) {
        channel.WriteUInt32(OpCodes.ClientUidToUrl);
        channel.WriteString(leader);
        channel.WriteString(storeID);
        channel.WriteInt64(uid);
        channel.Flush();
        String url = channel.ReadString();
        return url;
    }

    public long[][] ClientGetAllCaptures(long uid, int direction) {
        channel.WriteUInt32(OpCodes.ClientGetAllCaptures);
        channel.WriteString(leader);
        channel.WriteString(storeID);
        channel.WriteInt32(direction);
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

    public long[] ClientBatchUrlToUid(String[] urls) {
        channel.WriteUInt32(OpCodes.ClientBatchUrlToUid);
        channel.WriteString(leader);
        channel.WriteString(storeID);
        channel.WriteInt32(urls.length);
        for (int i = 0; i < urls.length; i++) {
            channel.WriteString(urls[i]);
        }
        channel.Flush();
        long[] uids = new long[urls.length];
        for (int i = 0; i < urls.length; i++) {
            uids[i] = channel.ReadInt64();
        }
        return uids;
    }

    public String[] ClientBatchUidToUrl(long[] uids) {
        channel.WriteUInt32(OpCodes.ClientBatchUidToUrl);
        channel.WriteString(leader);
        channel.WriteString(storeID);
        channel.WriteInt32(uids.length);
        for (int i = 0; i < uids.length; i++) {
            channel.WriteInt64(uids[i]);
        }
        channel.Flush();
        String[] urls = new String[uids.length];
        for (int i = 0; i < uids.length; i++) {
            urls[i] = channel.ReadString();
        }
        return urls;
    }

    public Long[] ClientAllUids() {
        channel.WriteUInt32(OpCodes.ClientGetAllUids);
        channel.WriteString(leader);
        channel.WriteString(storeID);
        channel.Flush();
        ArrayList<Long> uids = new ArrayList<Long>();
        long uid = channel.ReadInt64();
        while (uid >= 0) {
            uids.add(uid);
            uid = channel.ReadInt64();
        }
        Long[] result = new Long[uids.size()];
        result = uids.toArray(result);
        return result;
    }
}