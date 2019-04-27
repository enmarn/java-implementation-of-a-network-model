package experiment4.interfaces;

import java.io.IOException;

public interface IPysicalLayer {
	/**
	 * 在一根物理介质上，发送一个比特流，物理层无目的地，因此模拟的物理层也不需要目的地
	 * @param bitstream
	 */
	public void send(String bitstream);
	/**
	 * 监听一根物理介质上的比特流，返回物理层读取的比特流。
	 * @return 监听到的比特流
	 * @throws IOException 
	 */
	public String listen() throws IOException;
}
