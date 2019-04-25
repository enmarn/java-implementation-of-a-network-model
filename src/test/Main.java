package test;

import experiment2.TransmissionUtil;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		CRCUtil crcUtil = new CRCUtil();
		testExper2();
		
		
//		bitSet.clear();
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
