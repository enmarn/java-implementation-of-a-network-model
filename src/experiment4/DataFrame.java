package experiment4;

import experiment3.DataLinkLayer;

public class DataFrame {
	public static final int SERIAL_NUMBER_LENGTH = 16;
	public static final int FRAME_0 = 0;	
	public static final int FRAME_END = 0x7fff;
	
	public static final int MAX_CONTENT_LENGTH = 32;	
	
	public static final int CHECK_CRC_ERROR = 0;
	public static final int SUCCEED = 1;
	public static final int NORMAL = 1;
	public static final int IS_ACK = 2;
	int frameSerial;
	String content;
	int status = 1;
	/**
	 * @param 物理链路上传输的透明的比特流
	 */
	public DataFrame(String parentbitStream) {
		// TODO Auto-generated constructor stub
		//去透明化与CRC
		Log.err.println("原串"+parentbitStream);
		String src = DataLinkLayerUtil.toOriginalBitStream(parentbitStream);
		Log.err.println("去透明化串"+src);
		status = DataLinkLayerUtil.checkCRC(src)?1:0;
		src = DataLinkLayerUtil.removeCRC(src);
		Log.err.println("去CRC串"+src);
		//反序列化
		String framenum = src.substring(0, SERIAL_NUMBER_LENGTH);
		Log.err.println("framenum"+framenum);
		int num = 0;
		for(int i=0;i<framenum.length();i++) {
			num <<= 1;
			num += framenum.charAt(i) - '0';
		};
		boolean bool = (num & 0x00008000)>0;
		this.frameSerial = num & FRAME_END;
		if(this.frameSerial==16) {
			int i=0;
			i++;
		}
		Log.err.println(Integer.toBinaryString(this.frameSerial));
		Log.err.println(Integer.toBinaryString(num));
		content = src.substring(SERIAL_NUMBER_LENGTH);
		if(status == SUCCEED) if(bool) status = IS_ACK;
//		Log.debug.println(toString());
	}
	/**
	 * @return 将Frame帧序列化后，拼接CRC并进行透明化的比特流
	 */
	public String toParentBitStream() {
//		Log.debug.println(toString());
		//序列化
		String tmpSeq = Integer.toBinaryString(frameSerial);
		String num = "";
		for(int i=DataLinkLayer.SERIAL_NUMBER_LENGTH-tmpSeq.length();i>0;i--) num += "0";
    	num = num + tmpSeq;
//    	System.out.println("发送者——发送第 " + frameSeq + " 帧——内容:" +src);
    	String src = num + content;
    	//CRC与透明化
    	src = DataLinkLayerUtil.appendCRC(src);
    	src = DataLinkLayerUtil.toParentTransparentBitStream(src);
		if(this.frameSerial==16) {
			int i=0;
			i++;
		}
		return src;
	}
	/**
	 * @param 是否是Ack
	 * @return 将Frame帧序列化后，拼接CRC并进行透明化的比特流
	 */
	public String toParentBitStream(boolean isAck) {
		if(isAck) frameSerial = frameSerial|0x8000;
		return toParentBitStream();
	}
	
	public DataFrame(int frameSerial, String content) {
		// TODO Auto-generated constructor stub
		this.frameSerial = frameSerial;
		this.content = content;
	}
	public DataFrame() { }
	
	public String toString() {
		String stat = status==2?"ACK":
				frameSerial==FRAME_END?"结束标志":
					status==1?"正常":"校验错误";
		return "第" + (frameSerial&FRAME_END) + "帧  " +"状态" + stat
				+ "	" + content;
	}
	
	public int getFrameSerial() {
		return frameSerial;
	}
	public void setFrameSerial(int frameSerial) {
		this.frameSerial = frameSerial;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}