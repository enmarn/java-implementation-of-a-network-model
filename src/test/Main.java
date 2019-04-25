package test;

import utils.BitMap;
import experiment2.TransmissionUtil;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		CRCUtil crcUtil = new CRCUtil();
//		testExper2();
		
		
//		bitSet.clear();
	}
	
	static void testExper2() {
		BitMap orginal = new BitMap();
		orginal.valueOf("0111111111111101111111111111111110");
		BitMap parent = TransmissionUtil.toTransparentBitMap(orginal);
		BitMap info = TransmissionUtil.toOriginalBitMap(parent);
		System.out.println(orginal.toString());
		System.out.println(parent.toString());
		System.out.println(info.toString());
	}

}
