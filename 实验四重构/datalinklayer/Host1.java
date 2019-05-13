package datalinklayer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import log.Log;

public class Host1 {
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Log.who = "HOST1";
		DataLinkLayer layer = new DataLinkLayer(9001);
//		InputStream in = new FileInputStream("input/test1");
//		layer.send(in);
		byte[]bytes=layer.receive();
//		OutputStream out = new FileOutputStream("input/output");
		OutputStream out = new FileOutputStream("input/output.jpg");
		out.write(bytes);
		out.flush();
		out.close();
//		Log.info.println("这是收到的信息\n"+BitStream.getBitStringByBytes(bytes));
	}
}
