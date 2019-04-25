package experiment3;

public class DataLinkLayer {
	public static byte START_END_FLAG = 0x7E;
	
	public static final byte[] ACCEPT = {0x01};
	public static final byte[] CRC_SUM_ERROR = {0x10};
}
