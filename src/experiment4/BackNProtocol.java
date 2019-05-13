package experiment4;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.LinkedList;

import org.apache.logging.log4j.core.jmx.Server;

import experiment4.interfaces.ILinkLayer;

public class BackNProtocol implements ILinkLayer{
	private static final int ATTEMPT_NUM = 5;
	private static final int WINDOW_SIZE = 7;
	private boolean alive = true;
	private int timeout = 1000;
	private Thread sendThread;
	private boolean isSendThreadSleep;
	private Thread receiveThread;
	private PysicalLayer connect;
	private MyQueue queue;
	public BackNProtocol(int port) {
		// TODO Auto-generated constructor stub
		connect = new PysicalLayer(port);
		queue = new MyQueue();
		sendThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (alive){
					while (true) {
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						synchronized (queue) {
							if(queue.isEmpty()) ;
							else {
								timer();
							}
						}
					}
				}
			}
			private synchronized void timer() {
				StrSyn syn = queue.getFirst();
				if(System.currentTimeMillis()-syn.time>timeout) {
					queue.resend();
				} else {
					try {
						Thread.sleep(40);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		receiveThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					receive();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		receiveThread.start();
		sendThread.start();
	}
	/**
	 * 输入一个长比特串，将长比特串分帧打包发送
	 * @param 长比特串
	 * @return 是否发送成功
	 * @throws InterruptedException 
	 */
	@Override
	public boolean send(String bitStream) throws InterruptedException {
		// TODO Auto-generated method stub
		Log1.info.println("发送信息——" + bitStream);
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
	int fnum;
	@Override
	public boolean send(char[]bits) throws InterruptedException {
		// TODO Auto-generated method stub
		String bitstream = new String(bits);
		Log1.info.println("发送信息——" +  bitstream);
		try {
			InputStream is =new ByteArrayInputStream(bitstream.getBytes());
			Reader reader = new InputStreamReader(is);
			char[] cbuf = new char[DataFrame.MAX_CONTENT_LENGTH];
			int readnum;
			int framenum = 0;
			fnum = 0;
			//发送第0帧标志开始
        	_send(DataFrame.FRAME_0,"");
			//循环发送字串内容
			while ((readnum=reader.read(cbuf))>0) {
				framenum ++;
	        	String src = new String(cbuf,0,readnum);
	        	System.out.println(src.length());
	        	_send(framenum,src);
			}
			//发送0xffff帧标志结束
        	_send(DataFrame.FRAME_END,"");
        	fnum = DataFrame.FRAME_END;
        	flag = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	boolean flag =false;
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
		while (!flag) {
			String bitstream = connect.listen();
			DataFrame frame = new DataFrame(bitstream);
			if(frame.status==DataFrame.IS_ACK) {
				//接收到ACK信息,发送下一帧
				queue.pop();
				synchronized (Log1.frame) {
					Log1.frame.printSepln("recieve a ACK");
					Log1.frame.println("type: "+ frame.status);
					Log1.frame.println("CRC校验：" + (frame.status>0));
					Log1.frame.println("接收帧序号："+frame.frameSerial);
					Log1.frame.println("返回ACK值："+frame.frameSerial);
					Log1.frame.printSepln();
				}
				Log1.BNP_0.println("收到ACK"+frame.getFrameSerial());
				if(flag) break;
			} else if (frame.status==DataFrame.NORMAL) {
				//接收对方传来的普通帧
				if (frame.frameSerial==frame_expected) {
					//收到预期帧，返回ACK
					if(frame_expected!=0) 
						content += frame.content;
					DataFrame ack_frame = new DataFrame(frame_expected, "");
					Log1.BNP_0.println("收到预期帧——返回ACK"+frame_expected);
					synchronized (Log1.frame) {
						Log1.frame.printSepln("recieve a frame");
						Log1.frame.println("type: "+ frame.status);
						Log1.frame.println("CRC校验：" + (frame.status>0));
						Log1.frame.println("接收帧序号："+frame.frameSerial);
						Log1.frame.println("预期帧序号："+frame_expected);
						Log1.frame.println("返回ACK值："+frame.frameSerial);
						Log1.frame.printSepln();
					}
					connect.send(ack_frame.toParentBitStream(true));//是ACK
					frame_expected ++;
				} else if(frame.frameSerial==DataFrame.FRAME_END) {
					//收到结束帧，停止接收
					DataFrame ack_frame = new DataFrame(frame.frameSerial, "");
					Log1.BNP_0.println("收到结束帧——返回ACK"+frame.getContent());
					synchronized (Log1.frame) {
						Log1.frame.printSepln("recieve end tag");
						Log1.frame.println("type: "+ frame.status);
						Log1.frame.println("CRC校验：" + (frame.status>0));
						Log1.frame.println("接收帧序号："+frame.frameSerial);
						Log1.frame.println("预期帧序号："+frame_expected);
						Log1.frame.println("返回ACK值："+frame.frameSerial);
						Log1.frame.printSepln();
					}
					connect.send(ack_frame.toParentBitStream(true));//是ACK
					Log1.info.println("收到信息——"+ content);
				} 
			} //传输错误的帧，丢弃
			else {
				synchronized (Log1.frame) {
					Log1.frame.printSepln("recieve fault");
					Log1.frame.println("type: "+ frame.status);
					Log1.frame.println("CRC校验：" + (frame.status>0));
					Log1.frame.println("接收帧序号："+frame.frameSerial);
					Log1.frame.println("预期帧序号："+frame_expected);
					Log1.frame.printSepln();
				}
			}
		}
		System.out.println("停止监听");
		return content;
	}
	public void setSoTimeout(int time) { 
		timeout = time;
	}
	/**
	 * 尝试、等待与重发机制
	 * @param framenum
	 * @param msg
	 * @throws InterruptedException 
	 */
	private synchronized void _send(int framenum, String msg) throws InterruptedException {
		try {
			Thread.sleep(50);
		} catch (InterruptedException e1) {
		}
		while (framenum==DataFrame.FRAME_END&&!queue.isEmpty());

		DataFrame frame = new DataFrame(framenum,msg);
	    String str = frame.toParentBitStream();
	    StrSyn syn = new StrSyn(false,str,framenum);
//		if(framenum==fnum) {
			queue.push(syn);
//			fnum ++;
//		}
	}
	/**
	 * 自定义的一个窗口队列
	 */
	@SuppressWarnings("serial")
	class MyQueue extends LinkedList<StrSyn>{
		int send_next = 0;//下一个发送的帧索引值
		public synchronized void push(StrSyn str){
			try {
			    if(isSendThreadSleep) 
			    	isSendThreadSleep = false;
			    addFirst(str);
			    if(isFull()) this.wait();
			    while (sendnext());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		public synchronized boolean sendnext() {
			synchronized (queue) {
			if(send_next==size()) return false;
				StrSyn syn = get(size()-1-send_next);
				send_next ++;
				Log1.BNP_0.println("BNP协议——发送第 " + syn.framenum +"帧");
				synchronized (Log1.frame) {
					Log1.frame.printSepln("send a frame");
					Log1.frame.println("正在发送帧序号："+syn.framenum);
					Log1.frame.println("下一发送帧序号："+send_next);
					Log1.frame.println("预期收到的ACK："+syn.framenum);
					Log1.frame.printSepln();
				}
				syn.time = System.currentTimeMillis();
				syn.last_attempt_num --;
				connect.send(syn.string);
				return true;
			}
		}
		public synchronized void resend() {
			send_next = 0;
			while (sendnext())
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				};
		}
		public synchronized StrSyn pop(){
			synchronized (this) {
				StrSyn string = removeLast();
				send_next --;
				if(!isEmpty())this.notify();
				return string;
			}
		}
		public synchronized boolean isFull() {
			return size()>=WINDOW_SIZE;
		}
	}
	class StrSyn {
		long time = 0;
		int framenum = 0;
		int last_attempt_num = ATTEMPT_NUM;
	    boolean count = false;//信号
	    StrSyn(){}
	    StrSyn(Boolean cnt,String str,int framenum){
	    	count=cnt;
	    	string=str;
	    	this.framenum=framenum;
    	}
	    public String string;
	}
	@SuppressWarnings("deprecation")
	public void close() {
		sendThread.stop();
		receiveThread.stop();
		alive = false;
	}
}
