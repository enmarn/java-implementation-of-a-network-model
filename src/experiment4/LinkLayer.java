package experiment4;

import experiment4.interfaces.ILinkLayer;

public class LinkLayer implements ILinkLayer{
	
	/**
	 * 输入一个长比特串，将长比特串分帧打包发送
	 * @param 长比特串
	 * @return 是否发送成功
	 */
	@Override
	public boolean send(String bitStream) {
		// TODO Auto-generated method stub
		
		return true;
	}
	/**
	 * 接收多个帧组合成一个长比特串
	 * @return 长比特串
	 */
	@Override
	public String receive() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean send(char[] bitstream) {
		// TODO Auto-generated method stub
		return false;
	}

}
