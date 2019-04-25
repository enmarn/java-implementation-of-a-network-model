package test;

import experiment1.CRCUtil;
import experiment2.TransmissionUtil;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		CRCUtil crcUtil = new CRCUtil();
		testExper1();
		
		
//		bitSet.clear();
	}
	static void testExper1() {
		String orginal = "000000000000000101010101111110101010111111110101";
		String CRCGEN = CRCUtil.appendCRC(orginal);
		String info = CRCUtil.removeCRC(CRCGEN);
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
