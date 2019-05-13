package datalinklayer;


public class CRCUtil {
	private static String CRC_GEN = "10001000000100001"; 
	
	public static String getCRC_GEN() {
		return CRC_GEN;
	}

	public static void setCRC_GEN(String cRC_GEN) {
		CRC_GEN = cRC_GEN;
	}
	
	private static String calculateCRC(String InfoString, String GenString)
	{
		int InfoLen = InfoString.length();
		int GenLen = GenString.length();
		
		String Info = InfoString;
		String Gen = GenString;
		
		int sum = InfoLen + GenLen - 1;

		for(int i = 1; i <= GenLen - 1; i++)
			Info += "0";

		for(int i = 1; i <= InfoLen - 1; i++)
			Gen += "0";
		
		char[] info = Info.toCharArray();
		char[] gen = Gen.toCharArray();
		
		//计算冗余码
		for(int i = 0; i < sum - GenLen + 1; i++){
            //判断补零后的帧最高位为1还是零
            if(info[i] != '0') {
            	for(int j = i; j < Info.length(); j++)
            		info[j] = (char) ((info[j] ^ gen[j - i]) + '0');

            	String tmpgen = new String(gen);
            	tmpgen = tmpgen.substring(0, tmpgen.length() - 1);
            	gen = tmpgen.toCharArray();
            }else {

            	String tmpgen = new String(gen);
            	tmpgen = tmpgen.substring(0, tmpgen.length() - 1);
            	gen = tmpgen.toCharArray();
            }
        }
		
		String crc = new String(info);
		crc = crc.substring(sum - GenLen + 1, sum);
        
        return crc;
	}
	/**
	 * 生成CRC码
	 * @param 源字串
	 * @return CRC Code
	 */
	public static String appendCRC(String InfoString, String GenString) {
		String crc = calculateCRC(InfoString, GenString);
		String ans = InfoString + crc;
//		System.out.println("生成的CRC-code：" + crc);
//		System.out.println("带校验的发送帧：" + ans + "\n");
//		
		return ans;
	}
	/**
	 * 生成CRC码
	 * @param 源字串
	 * @return CRC Code
	 */
	public static String appendCRC(String InfoString) {
		return appendCRC(InfoString, CRC_GEN);
	}
	/**
	 * 检测字串传输时是否发生错误
	 * @param 待检测字串
	 * @return 是否正确
	 */
	public static boolean check(String CRCString, String GenString)
	{
		String crc = calculateCRC(CRCString, GenString);
		
//		System.out.println("检查过程中生成的CRC-code：" + crc);
		
		for(int i = 0; i < crc.length(); i++)
		{
			if(crc.charAt(i) == '1')
				return false;
		}
		return true;
	}
	/**
	 * 检测字串传输时是否发生错误
	 * @param 待检测字串
	 * @return 是否正确
	 */
	public static boolean check(String CRCString) {
		return check(CRCString, CRC_GEN);
	}

	public static String removeCRC(String str) {
		if(str.length()>16)return str.substring(0, str.length()-16);
		return str;
	}
}
