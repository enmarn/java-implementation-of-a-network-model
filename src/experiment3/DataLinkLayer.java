package experiment3;

public class DataLinkLayer {
	public static final String START_END_FLAG = "01111110";
	
	public static final int SERIAL_NUMBER_LENGTH = 16;
	
	public static final char BIT_0 = '0';
	public static final char BIT_1 = '1';
	
	
	public static final String ACK = "00000001";
	public static final String CRC_SUM_ERROR = "00000010";
	public static final String FINAL_SIGN = "00000100";
	
}
