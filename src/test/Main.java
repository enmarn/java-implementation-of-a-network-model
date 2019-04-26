package test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import experiment1.CRCUtil;
import experiment2.TransmissionUtil;

public class Main {
	private static final Logger rootLogger = LogManager.getRootLogger();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		CRCUtil crcUtil = new CRCUtil();
//		testExper1();
		rootLogger.error("Logged by root logger: Hello this is an error");
        rootLogger.info("Logged by root logger: Hello World!");
        rootLogger.debug("Logged by root logger: Hello debug message.");

//		bitSet.clear();
	}
	static void testExper1() {
		String orginal = "000000000000000101010101111110101010111111110101";
		String CRCGEN = CRCUtil.appendCRC(orginal);
		String info = CRCUtil.removeCRC(CRCGEN);
		System.out.println(CRCUtil.check(CRCGEN));
		System.out.println(orginal);
		System.out.println(CRCGEN);
		System.out.println(info);
	}
	static void testExper2() {
		String orginal = "0111111111111101111111111111111110";
		String parent = TransmissionUtil.toParentTransparentBitString(orginal);
		String info = TransmissionUtil.toOriginalBitString(parent);
		System.out.println(orginal);
		System.out.println(parent);
		System.out.println(info);
	}

}
