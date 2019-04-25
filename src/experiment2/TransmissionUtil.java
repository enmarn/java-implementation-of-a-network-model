package experiment2;

import experiment3.DataLinkLayer;

public class TransmissionUtil {
	/**
	 * @param 原始信息的比特串
	 * @return 透明传输的比特串
	 */
	public static String toParentTransparentBitString(String originalInfo) {
		
		int stat=0;
		String transparentInfo = DataLinkLayer.START_END_FLAG;
		
		for(int i=0;i<originalInfo.length();i++) {
			
			if(originalInfo.charAt(i)==DataLinkLayer.BIT_1) {
				
				stat ++;
				transparentInfo += DataLinkLayer.BIT_1;
				
				if(stat==5) {
					stat = 0;
					transparentInfo += DataLinkLayer.BIT_0;
				}
				
			} else {
				stat = 0;
				transparentInfo += DataLinkLayer.BIT_0;
			}
		}
		
		transparentInfo += DataLinkLayer.START_END_FLAG;
		return transparentInfo;
	}
	/**
	 * @param 线路传输的比特串
	 * @return 解码后的比特串
	 */
	public static String toOriginalBitString(String transparentInfo) {
		
		int stat=0;
		int startEndFlag=0;
		String originalInfo = "";
		
		for(int i=0;i<transparentInfo.length();i++) {
			
			if(transparentInfo.charAt(i)==DataLinkLayer.BIT_1) {
				
				stat ++;
				originalInfo += DataLinkLayer.BIT_1;
				
				if(stat==5&&transparentInfo.charAt(i+1)==DataLinkLayer.BIT_0) {
					i++;
					stat = 0;
				}else if(stat==5
						&&transparentInfo.charAt(i+1)==DataLinkLayer.BIT_1
						&&transparentInfo.charAt(i+2)==DataLinkLayer.BIT_0){
					i += 2;
					stat = 0;
					startEndFlag ++;
					originalInfo = originalInfo.substring(0, originalInfo.length()-6);
					if(startEndFlag>=2) return originalInfo;
				}
				
			} else {
				
				originalInfo += DataLinkLayer.BIT_0;
				stat = 0;
				
			}
		}
		
		return originalInfo;
	}

}
