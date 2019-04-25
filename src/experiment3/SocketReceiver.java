package experiment3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import experiment2.TransmissionUtil;
import utils.BitMap;

public class SocketReceiver {
	private static final int PORT=8889;
	private static DatagramSocket server; 
	private static final int size = 1024;
	private static byte[] recv_buf = new byte[size];
	private static DatagramPacket data = new DatagramPacket(recv_buf, size);
	public static void main(String[] args) throws Exception {
	    server = new DatagramSocket(PORT);
	    System.out.println("接收端开始监听");
	    while (true) {
			receive(data);	
		}
//	    server.close();
	}
	public static void send(DatagramPacket data) throws IOException {
	    server.send(data);
	}
	
	public static void receive(DatagramPacket data) throws IOException {
	    server.receive(data);
	    BitMap recv_bit_stream = BitMap.valueOf(data.getData());
	    BitMap info = TransmissionUtil.toOriginalBitMap(recv_bit_stream);
	    System.out.println(info.toString());
	    //处理
	    byte[] stat = DataLinkLayer.ACCEPT;
	    DatagramPacket response = new DatagramPacket(stat,stat.length,data.getAddress(),data.getPort());
	    server.send(response);
	}
}
