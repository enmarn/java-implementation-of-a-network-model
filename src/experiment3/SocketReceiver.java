package experiment3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import experiment1.CRCUtil;
import experiment2.TransmissionUtil;

public class SocketReceiver {
	private static final int PORT=8889;
	private static final int size = 256;
	
	private byte[] recv_buf;
	private DatagramPacket data;
	private DatagramSocket server; 
	public SocketReceiver() throws SocketException {
		server = new DatagramSocket(PORT);
		recv_buf = new byte[size];
		data = new DatagramPacket(recv_buf, size);
	}
	
	public static void main(String[] args) throws Exception {
		SocketReceiver receiver = new SocketReceiver();
	    System.out.println("接收端——开始监听");
	    while (true) {
	    	String recv_str = receiver.receive();
	    	if (recv_str==null) {
	    		System.out.println("接收端——读入错误——继续接收");
	    	} else if(recv_str.equals(DataLinkLayer.FINAL_SIGN)) {
	    		System.out.println("接收端——收到终止信号——停止读入");
	    		break;	
	    	} else {
	    		String framenum = recv_str.substring(0, DataLinkLayer.SERIAL_NUMBER_LENGTH);
	    		int num = 0;
	    		for(int i=0;i<framenum.length();i++) {
	    			num <<= 1;
	    			num += framenum.charAt(i) - '0';
	    		}
	    		String content = recv_str.substring(DataLinkLayer.SERIAL_NUMBER_LENGTH);
				System.out.println("接收端——成功接收第 " + num + " 帧内容：" + content);
			}
		}
	}
	
	public void send(DatagramPacket data) throws IOException {
	    server.send(data);
	}
	
	public String receive() throws IOException {
		//接收到比特流
	    server.receive(data);
	    //线路上传输的比特流
	    String info = new String(data.getData());
		System.err.println("接收者：中间信息——透明传输码 " + info);
	    //去透明化解码
	    info = TransmissionUtil.toOriginalBitString(info);
		System.err.println("接收者：中间信息——去透明化码 " + info);
	    //CRC校验
	    String res;
	    if(CRCUtil.check(info)) res = DataLinkLayer.ACK;
	    else res = DataLinkLayer.CRC_SUM_ERROR;
	    //去CRC校验码
	    info = CRCUtil.removeCRC(info);
	    //回复
	    //CRC生成
	    res = CRCUtil.appendCRC(res);
	    //透明化编码
	    res = TransmissionUtil.toParentTransparentBitString(res);
	    
	    byte[] stat = res.getBytes();
	    DatagramPacket response = new DatagramPacket(stat,stat.length,data.getAddress(),data.getPort());
	    server.send(response);
	    return info;
	}
}
