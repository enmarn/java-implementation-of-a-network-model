package datalinklayer;

import datalinklayer.DataLinkLayerUtil;
import log.Log;

public class DataFrame {
	public static final String START_END_FLAG = "01111110";
	public static final char BIT_0 = '0';
	public static final char BIT_1 = '1';
	
	public static final byte INIT = 0b01111111;
	public static final byte ACK = 0b00000001;
	public static final byte FRAME_END = 0b00000010;
	public static final byte CRC_SUM_ERROR = 0b00000010;
	
	public static final int SERIAL_NUMBER_LENGTH = 16;
	public static final int CONTROL_LENGTH = 8;
	public static final int FRAME_0 = 0;	
	
	public static final int MAX_CONTENT_LENGTH = 128*8;	
	
	int frameSerial;
	byte control;
	String content;
	boolean CRCCheck = true;
	/**
	 * @param 物理链路上传输的透明的比特流
	 * @throws Exception 
	 */
	public DataFrame(String parentbitStream) throws Exception {
		String src = DataLinkLayerUtil.toOriginalBitStream(parentbitStream);
		CRCCheck = DataLinkLayerUtil.checkCRC(src);
		src = DataLinkLayerUtil.removeCRC(src);

		String framenum = src.substring(0, SERIAL_NUMBER_LENGTH);
		frameSerial = 0;
		for(int i=0;i<framenum.length();i++) {
			frameSerial <<= 1;
			frameSerial += framenum.charAt(i) - '0';
		};
		String ctrl = src.substring(SERIAL_NUMBER_LENGTH, SERIAL_NUMBER_LENGTH+CONTROL_LENGTH);
		control = 0;
		for(int i=0;i<ctrl.length();i++) {
			control <<= 1;
			control += ctrl.charAt(i) - '0';
		};
		content = src.substring(SERIAL_NUMBER_LENGTH+CONTROL_LENGTH);
    	Log.frame.println("数据帧处理程序——————接收来自物理层——————比特流转帧结构体\n" + toString());
	}
	/**
	 * @return 将Frame帧序列化后，拼接CRC并进行透明化的比特流
	 */
	public String toParentBitStream() {
		String tmpSeq = Integer.toBinaryString(frameSerial);
		String num = "";
		for(int i=SERIAL_NUMBER_LENGTH-tmpSeq.length();i>0;i--) num += "0";
    	num = num + tmpSeq;
    	tmpSeq = Integer.toBinaryString(Byte.toUnsignedInt(control));
		for(int i=CONTROL_LENGTH-tmpSeq.length();i>0;i--) num += "0";
		num = num + tmpSeq;
    	String src = num + content;
    	//CRC与透明化
    	src = DataLinkLayerUtil.appendCRC(src);
    	src = DataLinkLayerUtil.toParentTransparentBitStream(src);
    	Log.frame.println("数据帧处理程序——————接收来自数据链路层——————帧结构体转比特流\n" + toString());
//    	System.err.println(src);
		return src;
	}
	
	public DataFrame(int frameSerial,byte Ctrl, String content) {
		// TODO Auto-generated constructor stub
		this.frameSerial = frameSerial;
		this.control = Ctrl;
		this.content = content;
	}
	public DataFrame() { }
	
	public String toString() {
		String stat = (control==(ACK|FRAME_END))?"结束ACK":
				(control&ACK)!=0?"ACK":
			!CRCCheck?"校验错误":
			(control&FRAME_END)!=0?"结束标志":"正常";
		return "第 " + (frameSerial) + " 帧	" +"状态：" + stat
				+ "\n" + content;
	}
	
	public int getFrameSerial() {
		return frameSerial;
	}
	public void setFrameSerial(int frameSerial) {
		this.frameSerial = frameSerial;
	}
	public void setControl(byte control) {
		this.control = control;
	}
	public byte getControl() {
		return control;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public boolean isCRC() {
		return CRCCheck;
	}
	public boolean isFrameEnd() {
		return (control&FRAME_END)>0;
	}
}
