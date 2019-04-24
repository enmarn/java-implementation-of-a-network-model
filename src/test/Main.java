package test;

import utils.BitMap;
import experiment2.TransmissionUtil;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		CRCUtil crcUtil = new CRCUtil();
		
	}
	
	static void testExper2() {
		TransmissionUtil transmissionUtil = new TransmissionUtil();
		BitMap orginal = new BitMap();
		orginal.valueOf("11111111111110111111111111111111");
		BitMap parent = transmissionUtil.toTransparentBitSet(orginal);
		BitMap info = transmissionUtil.toOriginalBitSet(parent);
		System.out.println(orginal.toString());
		System.out.println(parent.toString());
		System.out.println(info.toString());
	}

}
