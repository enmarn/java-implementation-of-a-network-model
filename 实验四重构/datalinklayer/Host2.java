package datalinklayer;

import java.io.FileInputStream;
import java.io.InputStream;

import log.Log;


public class Host2 {
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Log.who = "HOST2";
		DataLinkLayer layer = new DataLinkLayer(8999);
//		InputStream in = new FileInputStream("input/test1");
		InputStream in = new FileInputStream("input/why.jpg");		
		layer.send(in);
//		byte[]bytes=layer.receive();
//		Log.info.println("这是收到的信息\n"+BitStream.getBitStringByBytes(bytes));
	}
}