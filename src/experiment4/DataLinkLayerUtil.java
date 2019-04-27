package experiment4;

import experiment1.CRCUtil;
import experiment2.TransmissionUtil;

public class DataLinkLayerUtil  {
	/**
	 * @param 原始比特流
	 * @return 加了CRC码的比特流
	 */
	public static String appendCRC(String bitstream){
		return CRCUtil.appendCRC(bitstream);
	}
	/**
	 * @param 加了CRC码的比特流
	 * @return 去除了CRC码的比特流
	 */
	public static String removeCRC(String bitstream) {
		return CRCUtil.removeCRC(bitstream);
	}
	/**
	 * @param 对比特流的CRC进行CRC校验
	 * @return 校验结果
	 */
	public static boolean checkCRC(String bitstream) {
		return CRCUtil.check(bitstream);
	}
	public static void setCRCGen(String crc_gen) {
		CRCUtil.setCRC_GEN(crc_gen);
	}
	public static String getCRCGen() {
		return CRCUtil.getCRC_GEN();
	}
	/**
	 * @param 未经处理的任意的比特流
	 * @return 可以由物理层传输的透明的比特流
	 */
	public static String toParentTransparentBitStream(String bitstream) {
		return TransmissionUtil.toParentTransparentBitString(bitstream); 
	}
	/**
	 * @param 由物理层传输的透明的比特流
	 * @return 去透明化的比特流
	 */
	public static String toOriginalBitStream(String bitstream) {
		return TransmissionUtil.toOriginalBitString(bitstream);
	}
}
