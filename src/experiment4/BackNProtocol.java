package experiment4;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.LinkedList;

import experiment4.interfaces.ILinkLayer;

public class BackNProtocol implements ILinkLayer{
	private static final int ATTEMPT_NUM = 5;
	private static final int WINDOW_SIZE = 7;
	private boolean alive = true;
	private int timeout = 1000;
	private PysicalLayer connect;
	private MyQueue queue;
	public BackNProtocol(int port) {
		// TODO Auto-generated constructor stub
		connect = new PysicalLayer(port);
		queue = new MyQueue();
	}
	/**
	 * 输入一个长比特串，将长比特串分帧打包发送
	 * @param 长比特串
	 * @return 是否发送成功
	 */
	@Override
	public boolean send(String bitStream) {
		// TODO Auto-generated method stub
		Log.info.println("发送信息——" + bitStream);
		try {
			InputStream is =new ByteArrayInputStream(bitStream.getBytes());
			Reader reader = new InputStreamReader(is);
			char[] cbuf = new char[DataFrame.MAX_CONTENT_LENGTH];
			int readnum;
			int framenum = 0;
			//发送第0帧标志开始
        	_send(DataFrame.FRAME_0,"");
			//循环发送字串内容
			while ((readnum=reader.read(cbuf))>0) {
				framenum ++;
	        	String src = new String(cbuf,0,readnum);
	        	_send(framenum,src);
			}
			//发送0xffff帧标志结束
        	_send(DataFrame.FRAME_END,"");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	@Override
	public boolean send(char[]bits) {
		// TODO Auto-generated method stub
		String bitstream = new String(bits);
		Log.info.println("发送信息——" +  bitstream);
		try {
			InputStream is =new ByteArrayInputStream(bitstream.getBytes());
			Reader reader = new InputStreamReader(is);
			char[] cbuf = new char[DataFrame.MAX_CONTENT_LENGTH];
			int readnum;
			int framenum = 0;
			//发送第0帧标志开始
        	_send(DataFrame.FRAME_0,"");
			//循环发送字串内容
			while ((readnum=reader.read(cbuf))>0) {
				framenum ++;
	        	String src = new String(cbuf,0,readnum);
	        	_send(framenum,src);
			}
			//发送0xffff帧标志结束
        	_send(DataFrame.FRAME_END,"");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	/**
	 * 接收多个帧组合成一个长比特串
	 * @return 长比特串
	 * @throws IOException 
	 */
	@Override
	public String receive() throws Exception {
		// TODO Auto-generated method stub
		int frame_expected = 0;
		String content = "";
		while (true) {
			String bitstream = connect.listen();
			DataFrame frame = new DataFrame(bitstream);
			if(frame.status==DataFrame.IS_ACK) {
				//接收到ACK信息,发送下一帧
				queue.pop().count = true;
				Log.debug.println("收到ACK"+frame.getFrameSerial());
			} else if (frame.status==DataFrame.NORMAL) {
				//接收对方传来的普通帧
				if (frame.frameSerial==frame_expected) {
					//收到预期帧，返回ACK
					if(frame_expected!=0) 
						content += frame.content;
					DataFrame ack_frame = new DataFrame(frame_expected, "");
					Log.debug.println("收到预期帧——返回ACK"+frame.getContent());
					connect.send(ack_frame.toParentBitStream(true));//是ACK
					frame_expected ++;
				} else if(frame.frameSerial==DataFrame.FRAME_END) {
					//收到结束帧，停止接收
					DataFrame ack_frame = new DataFrame(frame.frameSerial, "");
					Log.debug.println("收到结束帧——返回ACK"+frame.getContent());
					connect.send(ack_frame.toParentBitStream(true));//是ACK
					break;
				}
			} //传输错误的帧，丢弃
		}
		Log.info.println("收到信息——"+ content);
		return content;
	}
	public void setSoTimeout(int time) { 
		timeout = time;
	}
	/**
	 * 尝试、等待与重发机制
	 * @param framenum
	 * @param msg
	 */
	private synchronized void _send(int framenum, String msg) {
		try {
			Thread.sleep(10);
		} catch (InterruptedException e1) {
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				DataFrame frame = new DataFrame(framenum,msg);
			    String str = frame.toParentBitStream();
			    StrSyn syn = new StrSyn(false,str);
				queue.push(syn);
				int last_attempt_num = ATTEMPT_NUM;
				while (!syn.count&&last_attempt_num>0) {
					last_attempt_num--;
					Log.debug.println("BNP协议——发送第 " + framenum +"帧");
					connect.send(str);
					noExceptionSleep(timeout);
					if(!syn.count) Log.debug.println("BNP协议——发送失败——剩余重发次数 "+last_attempt_num);
				}
			}
			public void noExceptionSleep(int timeout) {
				try {
					Thread.sleep(timeout);
				} catch (Exception e) {
				}
			}
		}).start();
		
	}
	/**
	 * 自定义的一个窗口队列
	 */
	@SuppressWarnings("serial")
	class MyQueue extends LinkedList<StrSyn>{
		public synchronized void push(StrSyn str){
			try {
			    addFirst(str);
			    if(isFull()) this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		public synchronized StrSyn pop() {
			StrSyn string = removeLast();
			if(!isEmpty())this.notify();
			return string;
		}
		public synchronized boolean isFull() {
			return size()>=WINDOW_SIZE;
		}
	}
	class StrSyn {
	    boolean count = false;//信号
	    StrSyn(){}
	    StrSyn(Boolean cnt,String str){count=cnt;string=str;}
	    public String string;
	}
}