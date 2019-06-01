package de.l3s.hits.shs.client;

/**
 * @author vdkhoi
 */
public class OpCodes {
    // For Web Client
    public static final int ClientGetTemporalPageLinks = 0xF1000042;
    public static final int ClientBatchGetTemporalPageLinks = 0xF1000043;
    public static final int ClientGetTemporalLinks = 0xf1000044;
    public static final int ClientBatchGetTemporalLinks = 0xf1000045;
    public static final int ClientUrlToUid = 0xf1000046;
    public static final int ClientUidToUrl = 0xf1000047;
    public static final int ClientBatchUrlToUid = 0xf1000048;
    public static final int ClientBatchUidToUrl = 0xf1000049;
    public static final int ClientGetAllCaptures = 0xf1000050;
    public static final int ClientBatchGetAllCaptures = 0xf1000051;
}
