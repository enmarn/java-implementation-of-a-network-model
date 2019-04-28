package experiment4.interfaces;

public interface ILinkLayer {
	/**
	 * 输入一个长比特串，将长比特串分帧打包发送
	 * @param 长比特串
	 * @return 是否发送成功
	 * @throws InterruptedException 
	 */
	public boolean send(String bitStream) throws InterruptedException;
	/**
	 * 接收多个帧组合成一个长比特串
	 * @return 长比特串
	 * @throws Exception 
	 */
	public String receive() throws  Exception;
	boolean send(char[] bitstream) throws InterruptedException;
}
