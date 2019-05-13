package datalinklayer;

import log.Log;
import physiclayer.IPhysicLayer;
import physiclayer.MockPhysicLayer;

class BackNProtocol{
	CacheArea cache;
	//出错概率n%
	private int errp = 0;
	public int frameExpected = DataFrame.INIT;
	public int ackExpected = 0;
	@SuppressWarnings("unused")
	private static final int ATTEMPT_NUM = 5;
	private static int MAX_WINDOW_SIZE = 7;
	private static int WINDOW_SIZE = 1;
	private int timeout = 2000;
	private boolean alive = true;
	private IPhysicLayer physicLayer;
	
	public BackNProtocol(int port) {
		physicLayer = new MockPhysicLayer(port);
		cache = new CacheArea();
		// TODO Auto-generated constructor stub
		Thread sendThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (alive) {
					if(cache.isSendding.size()<=WINDOW_SIZE) {
						byte[] bytes = null;
						synchronized (cache) {
							if(cache.theNextACK!=null) {
								//有ACK在等待，将ACK发送并从缓冲区中移除
								buffer buf = cache.theNextACK;
//								System.out.println("发送一个ACK"+buf.index);
								Log.BNP_0.printSepln("发送线程");
								Log.BNP_0.println("发送一个ACK帧——————ACK" + buf.index);
								Log.BNP_0.printSepln();
								bytes = buf.content;
								buf.isACK = true;
								buf.timestrap = System.currentTimeMillis();
								cache.isSendding.add(buf);
								cache.theNextACK = null;
							}
							else if(cache.nextSend.size()!=0&cache.isSendding.size()<WINDOW_SIZE) {
								//无ACK等待，将待发送队列首发送
								buffer buf = cache.nextSend.poll();
//								System.out.println("ackexpected "+ackExpected+" 发送一个数据帧"+buf.index);
								Log.BNP_0.printSepln("发送线程");
								Log.BNP_0.println("发送一个数据帧——————第 " + buf.index + "帧");
								Log.BNP_0.println("ACK_EXCEPTED	" + ackExpected);
								Log.BNP_0.printSepln();
								cache.isSendding.add(buf);
								buf.timestrap = System.currentTimeMillis();
								bytes = buf.content;
							}
						}
						if(bytes!=null) {
							physicLayer.toPhysic(bytes);
						}
					} 
					if(!cache.isSendding.isEmpty()) {
						byte[] bytes = null;
						synchronized (cache) {
							buffer buf = cache.isSendding.peek();
							if(buf.isACK) {
								buffer b = cache.isSendding.poll();
//								bytes = b.content;
							} else if(System.currentTimeMillis() - buf.timestrap>timeout) {
								//如果序号大于ackExpected重发,把重发的放到队列尾，更新时间戳
								if(buf.index>=ackExpected) {
//									System.out.println("ackexpected "+ackExpected+" 重发一个数据帧"+buf.index);
									Log.BNP_0.printSepln("发送线程");
									Log.BNP_0.println("重新发送一个数据帧——————第 " + buf.index + "帧");
									Log.BNP_0.println("ACK_EXCEPTED	" + ackExpected);
									Log.BNP_0.printSepln();
									buf.timestrap = System.currentTimeMillis();
									cache.isSendding.add(cache.isSendding.poll());
									bytes = buf.content;
								} else {
									cache.isSendding.poll();
								}
							} 
						}
						if(bytes!=null) {
							physicLayer.toPhysic(bytes);
						}
					}
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		Thread recvThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (alive) {
					
					byte[] bytes = physicLayer.fromPhysic();
					if(bytes==null) continue;
					try {
						DataFrame frame = new DataFrame(BitStream.getBitStringByBytes(bytes));
//	*************************************************************************************************
//												随机出错
//	*************************************************************************************************
						if(errp>0) {
							int rand = (int) (Math.random()*100);
							if(rand<errp) {
								frame.CRCCheck = false;
								Log.err.println("随机出错");	
							}
						}
//	*************************************************************************************************	
						if(!frame.isCRC()) {
							Log.BNP_0.printSepln("接收线程");
							Log.BNP_0.println("收到一帧——————第 " + frame.frameSerial + "帧");
							Log.BNP_0.println("CRC错误——————丢弃");
							Log.BNP_0.printSepln();
							continue;	//CRC错误，跳过
						}
						if((frame.control&DataFrame.ACK)>0) {//ACK，pop掉帧序号小于等于ack的帧
							if(frame.frameSerial>= ackExpected) {
								Log.BNP_0.printSepln("接收线程");
								Log.BNP_0.println("收到一个ACK——————ACK " + frame.frameSerial);
								Log.BNP_0.println("ACK_EXCEPTED " + (frame.frameSerial + 1));
								Log.BNP_0.printSepln();
								/*
								 * 如果收到第0帧ACK，则放大窗口
								 * 如果收到结束帧ACK，则缩小窗口
								 */
								if(frame.frameSerial==0) WINDOW_SIZE = MAX_WINDOW_SIZE;
								else if((frame.control&DataFrame.FRAME_END)>0) WINDOW_SIZE = 1;
								synchronized (cache) {
									while (!cache.isSendding.isEmpty()&&cache.isSendding.peek().index<=frame.frameSerial) {
										cache.isSendding.poll();
									}
									ackExpected = frame.frameSerial + 1;
								}
							}
						} else if ((frame.control&DataFrame.FRAME_END)>0){//结束帧
							frameExpected = frame.frameSerial;
							synchronized (cache) {
								Log.BNP_0.printSepln("接收线程");
								Log.BNP_0.println("收到一个结束帧");
								Log.BNP_0.printSepln();
								synchronized (cache) {
									DataFrame ackFrame = new DataFrame();
									ackFrame.setContent("");
									ackFrame.setControl((byte) (DataFrame.ACK|DataFrame.FRAME_END));
									ackFrame.frameSerial = frame.frameSerial;
									buffer ackbuf = new buffer();
									ackbuf.index = frame.frameSerial;
									ackbuf.isACK = true;
									ackbuf.content = BitStream.getBytesByBitString(ackFrame.toParentBitStream());
									cache.theNextACK = ackbuf;
								}
								byte[] bs = new byte[cache.recvbuf.length];
								int num=0;
								while(!cache.received.isEmpty()) {
									buffer buf = cache.received.poll();
									System.arraycopy(buf.content, 0, bs, num, buf.length);
									num += buf.length;
								}
								cache.recvbuf.content = bs;
								try {
									synchronized (cache.recvLock) {
										Log.Linked.println("对方发送结束，发送缓冲区信号");
										cache.recvLock.notify();
									}
								} catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
								}
							}
						} else if(frame.frameSerial==frameExpected) {//数据帧
							buffer buf = new buffer();
//							System.out.println("收到一个内容帧:" + frame.toString());
							Log.BNP_0.printSepln("接收线程");
							Log.BNP_0.println("收到一个数据帧——————第 " + frame.frameSerial + "帧");
							Log.BNP_0.println("FRAME_EXCEPTED " + (frameExpected+1));
							Log.BNP_0.printSepln();
							if(frame.content.length()%8!=0) 
								throw new Exception("帧内容长度不为8的整数倍！");
							buf.length = frame.content.length()/8;
							buf.index = frame.frameSerial;
							buf.content = BitStream.getBytesByBitString(frame.content);
							cache.recvbuf.length+=buf.length;
							cache.received.add(buf);
							frameExpected ++;
							synchronized (cache) {
								DataFrame ackFrame = new DataFrame(frame.frameSerial, DataFrame.ACK, "");
								buffer ackbuf = new buffer();
								ackbuf.index = frame.frameSerial;
								ackbuf.isACK = true;
								ackbuf.content = BitStream.getBytesByBitString(ackFrame.toParentBitStream());
								cache.theNextACK = ackbuf;
							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		sendThread.start();
		recvThread.start();
	}
	public void writeBuf(DataFrame frame) {
		String str = frame.toParentBitStream();
		byte[] bytes = BitStream.getBytesByBitString(str);
		if(frame.control==DataFrame.ACK) {
			buffer buf = new buffer();
			buf.content = bytes;
			buf.index = frame.frameSerial;
			buf.isACK = true;
			cache.theNextACK = buf; 
		} else {
			buffer buf = new buffer();
			buf.content = bytes;
			buf.index = frame.frameSerial;
			buf.isACK = false;
			cache.nextSend.add(buf);
		}
	}
	public byte[]waitRecvBuf() {
		try {
			synchronized(cache.recvLock) {
				Log.Linked.println("阻塞等待接收缓冲区信号");
				cache.recvLock.wait(0);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cache.recvbuf.content;
	}
}
