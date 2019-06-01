package de.l3s.hits.shs.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author vdkhoi
 */
public class Channel {

    private String name;  // may be null
    private SocketChannel socket;
    private ByteBuffer sendBuf;
    private ByteBuffer recvBuf;
    private ByteBuffer serBuf;
    private int sendPos;
    private int recvPos;
    private int recvLen;
    public boolean needRevert = true;

    private static SocketChannel MakeSocket(String hostName, int port) {
        SocketChannel s = null;
        try {
            s = SocketChannel.open(new InetSocketAddress(hostName, port));
        } catch (IOException ex) {
            Logger.getLogger(Channel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return s;
    }

    public Channel(String name, int port) {
        this(MakeSocket(name, port));
        this.name = name;
    }

    public Channel(SocketChannel s) {
        this.socket = s;
        try {
            this.socket.socket().setSoLinger(true, 1);
            this.socket.socket().setSendBufferSize(1 << 23);
            this.socket.socket().setReceiveBufferSize(1 << 23);
            this.socket.socket().setTcpNoDelay(false);
            this.serBuf = ByteBuffer.allocate(8);
            this.recvBuf = ByteBuffer.allocate(this.socket.socket().getReceiveBufferSize());
            this.sendBuf = ByteBuffer.allocate(this.socket.socket().getSendBufferSize());
            this.sendPos = 0;
            this.recvPos = 0;
            this.recvLen = 0;
        } catch (SocketException ex) {
            Logger.getLogger(Channel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public SocketChannel getSocket() {
        return this.socket;
    }

    public String getName() {
        return this.name;
    }

    public void WriteBoolean(Boolean x) {
        if (this.sendPos + 1 < this.sendBuf.capacity()) {
            this.sendBuf.put((byte) (x ? 1 : 0));
        } else {
            this.WriteNumBytes(this.serBuf, 1);
        }
        this.serBuf.rewind();
    }

    public Boolean ReadBoolean() {
        if (this.recvPos + 1 <= this.recvLen) {
            recvPos += 1;
            return this.recvBuf.get(recvPos - 1) > 0;
        } else {
            this.ReadNumBytes(this.serBuf, 1);
            return this.serBuf.get(0) > 0;
        }
    }

    public void WriteInt32(int x) {
        this.serBuf = this.serBuf.putInt(x);
        RevertBuffer(serBuf, 0, 4);
        if (this.sendPos + 4 < this.sendBuf.limit()) {
            this.sendBuf.put(this.serBuf.array(), 0, 4);
            this.sendPos += 4;
        } else {
            this.WriteNumBytes(this.serBuf, 4);
        }
        this.serBuf.rewind();
    }

    public int ReadInt32() {
        if (this.recvPos + 4 <= this.recvLen) {
            recvPos += 4;
            RevertBuffer(this.recvBuf, recvPos - 4, 4);
            return this.recvBuf.getInt(recvPos - 4);
        } else {
            this.serBuf.rewind();
            int pos = this.serBuf.position();
            this.ReadNumBytes(this.serBuf, 4);
            RevertBuffer(serBuf, 0, 4);
            int val = this.serBuf.getInt(pos);
            this.serBuf.rewind();
            return val;
        }
    }

    public void WriteInt64(long x) {
        this.serBuf = this.serBuf.putLong(x);
        RevertBuffer(serBuf, 0, 8);
        if (this.sendPos + 8 < this.sendBuf.limit()) {
            this.sendBuf.put(this.serBuf.array(), 0, 8);
            this.sendPos += 8;
        } else {
            this.WriteNumBytes(this.serBuf, 8);
        }
        this.serBuf.rewind();
    }

    public long ReadInt64() {
        if (this.recvPos + 8 <= this.recvLen) {
            recvPos += 8;
            RevertBuffer(this.recvBuf, recvPos - 8, 8);
            return this.recvBuf.getLong(recvPos - 8);
        } else {
            this.serBuf.rewind();
            int pos = this.serBuf.position();
            this.ReadNumBytes(this.serBuf, 8);
            RevertBuffer(serBuf, 0, 8);
            long val = this.serBuf.getLong(pos);
            this.serBuf.rewind();
            return val;
        }
    }

    public void WriteUInt64(long x) {
        WriteInt64(x);
    }

    public long ReadUInt64() {
        return ReadInt64();
    }

    public void WriteUInt32(int x) {
        WriteInt32(x);
    }

    public int ReadUInt32() {
        return ReadInt32();
    }

    public void WriteString(String s) {
        this.WriteBytes(ByteBuffer.wrap(s.getBytes(StandardCharsets.UTF_8)));
    }

    public String ReadString() {
        ByteBuffer bytes = null;
        try {
            bytes = this.ReadBytes();
        } catch (Exception ex) {
            Logger.getLogger(Channel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bytes == null ? null : new String(bytes.array());
    }

    public void WriteBytes(ByteBuffer buf) {
        if (buf == null) {
            this.WriteInt32(-1);
        } else {
            int n = buf.capacity();
            this.WriteInt32(buf.capacity());
            int i = 0;
            while (i < n) {
                int cnt = Math.min(n, i + this.sendBuf.capacity() - this.sendPos);
                this.sendBuf.put(buf.array(), 0, cnt);
                i += cnt;
                this.sendPos += cnt;
                if (this.sendBuf.limit() == this.sendPos) {
                    this.FlushSendBuffer();
                }
            }
        }
    }

    public ByteBuffer ReadBytes() throws Exception {
        int n = this.ReadInt32();
        if (n == -1) {
            return null;
        }
        if (n > 0 && this.recvLen == 0) {
            throw new Exception("stream too short");
        }
        ByteBuffer buf = ByteBuffer.allocate(n);

        while (n > 0) {
            if (this.recvPos == this.recvLen) {
                this.FillRecvBuffer();
            }
            int cnt = Math.min(n, this.recvLen - this.recvPos);
            buf.put(this.recvBuf.array(), this.recvPos, cnt);
            this.recvPos += cnt;
            n -= cnt;
        }
        return buf;
    }

    public void WriteNumBytes(ByteBuffer b, int num) {
        while (num > 0) {
            int cnt = Math.min(num, this.sendBuf.capacity() - this.sendPos);
            this.sendBuf.put(b.array(), 0, cnt);
            this.sendPos += cnt;
            num -= cnt;
            if (this.sendBuf.capacity() == this.sendPos) {
                this.FlushSendBuffer();
            }
        }
    }

    public void ReadNumBytes(ByteBuffer b, int num) {
        int cnt = 0;
        try {
            while (num > 0) {
                if (this.recvPos >= this.recvLen) {
                    this.FillRecvBuffer();
                }
                if (this.recvLen < 0) return;
                cnt = Math.min(num, this.recvLen - this.recvPos);

                b = b.put(this.recvBuf.array(), this.recvPos, cnt);

                this.recvPos += cnt;
                num -= cnt;
            }
        }
        catch (IndexOutOfBoundsException e) {
            System.out.println("" + this.recvBuf.array().length + " : " + this.recvPos + " : " + cnt + " : " + this.recvLen + " : " + this.recvBuf.remaining());
            throw e;
        }
    }

    public void Close() {
        try {
            this.FlushSendBuffer();
            this.socket.socket().shutdownInput();
            this.socket.socket().shutdownOutput();
            this.socket.socket().close();
        } catch (IOException se) {
            Logger.getLogger(Channel.class.getName()).log(Level.SEVERE, null, se);
        }
        this.socket = null;
    }

    public void Flush() {
        try {
            // Disable Nagle to ensure flushed data is sent right away
            this.socket.socket().setTcpNoDelay(true);
            this.FlushSendBuffer();
            // Re-enable Nagle to fully utilize MTUs
            this.socket.socket().setTcpNoDelay(false);
        } catch (SocketException ex) {
            Logger.getLogger(Channel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void FlushSendBuffer() {
        try {
            this.socket.write(ByteBuffer.wrap(this.sendBuf.array(), 0, this.sendPos));
            this.sendBuf.rewind();
            this.sendPos = 0;
        } catch (IOException ex) {
            Logger.getLogger(Channel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void FillRecvBuffer() {
        this.recvPos = 0;
        this.recvBuf = (ByteBuffer)this.recvBuf.rewind();
        try {
            this.recvLen = this.socket.read(this.recvBuf);
            if (this.recvLen == 0) {
                throw new SocketException();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(Channel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void RevertBuffer(ByteBuffer buf, int offset, int num_revert) {
        if (!this.needRevert) {
            return;
        }
        byte[] bytes = buf.array();
        for (int left = offset, right = offset + num_revert - 1; left < right; ++left, --right) {
            byte temp = bytes[left];
            bytes[left] = bytes[right];
            bytes[right] = temp;
        }
    }
}
