package datalinklayer;

import java.io.InputStream;

import log.Log;

public class DataLinkLayer {
	BackNProtocol backNProtocol;
	public DataLinkLayer(int port) {
		// TODO Auto-generated constructor stub
		backNProtocol = new BackNProtocol(port);
	}
	public void send(InputStream in) throws Exception {
		backNProtocol.ackExpected = 0;
		DataFrame frame = new DataFrame(0, (byte) 0, "");
		Log.Linked.println(frame.toString() + "写入缓冲区");
		backNProtocol.writeBuf(frame);
		byte[] bits = new byte[64];
		int i;
		int num = 0;
		while (true){
			num ++;
			i = in.read(bits);
			if(i<=0) break;
			frame = new DataFrame(num, (byte) 0, BitStream.getBitStringByBytes(bits,i));
			Log.Linked.println(frame.toString() + "\n写入缓冲区");
			backNProtocol.writeBuf(frame);
		}
		frame = new DataFrame(num, (byte) DataFrame.FRAME_END, "");
		Log.Linked.println(frame.toString() + "写入缓冲区");
		backNProtocol.writeBuf(frame);
	}
	public byte[]receive() {
		backNProtocol.frameExpected = 0;
		return backNProtocol.waitRecvBuf();
	}
}
