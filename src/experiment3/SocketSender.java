package experiment3;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import experiment1.CRCUtil;
import experiment2.TransmissionUtil;

public class SocketSender {
	private static final int PORT=8888;
	private static final int size = 256;
	
	private byte[] recv_buf;
	private DatagramPacket data;
	private DatagramSocket server; 
	private InetAddress inetAddress;
	
	public SocketSender() throws Exception {
		server = new DatagramSocket(PORT);
//	    server.setSoTimeout(1000);
		recv_buf = new byte[size];
		data = new DatagramPacket(recv_buf, size);
		inetAddress = InetAddress.getLocalHost();
	}
	
	public static void main(String[] args) throws Exception {
	    
		
		SocketSender sender = new SocketSender();
        File testfile = new File("input/test1");
        FileReader reader = new FileReader(testfile);
        char[] fileReadBuf = new char[32];
        int frameSeq = 0;
		//假设一次发送的内容为32位，读出字节个数
        int readnum;
        while ((readnum=reader.read(fileReadBuf))>0) {
			frameSeq ++;
			//源信息串加上帧号
			String tmpSeq = Integer.toBinaryString(frameSeq);
			String num = "";
			for(int i=DataLinkLayer.SERIAL_NUMBER_LENGTH-tmpSeq.length();i>0;i--) num += "0";
        	num = num + tmpSeq;
        	String src = new String(fileReadBuf,0,readnum);
        	System.out.println("发送者——发送第 " + frameSeq + " 帧——内容:" +src);
        	src = num + src;
        	System.err.println("发送者——帧内容： " + src);
        	//发送内容
    	    if(!sender.send(src)) break;
		}
	    //发送终止信号
        sender.send(DataLinkLayer.FINAL_SIGN);
        
	    reader.close();
	    sender.close();
	}
	/**
	 * 加CRC码
     * 打包成透明比特流
     * 发送
	 * @param msg
	 * @throws IOException 
	 */
	public boolean send(String msg) throws IOException {
		//CRC生成
		msg = CRCUtil.appendCRC(msg);
		System.err.println("发送者：中间信息——CRC生成码 " + msg);
		//透明化处理
	    msg = TransmissionUtil.toParentTransparentBitString(msg);

		System.err.println("发送者：中间信息——透明传输码 " + msg);
	    byte[] transByte = msg.getBytes();
	    DatagramPacket packet = new DatagramPacket(transByte,
	    		transByte.length, inetAddress, 8889);
	    return send(packet);
	}

	private boolean send(DatagramPacket msg) throws IOException {
		int attemptNum = 5;
		while (attemptNum>0) {
			server.send(msg);
		    receive();
//			System.out.println(data.getAddress().getHostAddress());
			String response = new String(data.getData());
			response = TransmissionUtil.toOriginalBitString(response);
			response = CRCUtil.removeCRC(response);
			if(response.equals(DataLinkLayer.ACK)) return true;
			System.out.println("发送失败——再次尝试，剩余尝试次数："+attemptNum);
			attemptNum -- ;
		}
		System.out.println("发送失败——停止尝试");
		return false;
	}
	private void receive() {
		try {
			server.receive(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void close() {
		server.close();
	}
}
