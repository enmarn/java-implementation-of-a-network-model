package experiment2;

import experiment3.DataLinkLayer;
import utils.BitMap;

public class TransmissionUtil {

	public static BitMap toTransparentBitMap(BitMap originalInfo) {
		BitMap transparentInfo = new BitMap();
		transparentInfo.appendByte(DataLinkLayer.START_END_FLAG);
		int stat=0;
		for(int i=0;i<originalInfo.length();i++) {
			if(originalInfo.get(i)) {
				stat ++;
				transparentInfo.append(true);
				if(stat==5) {
					transparentInfo.append(false);
					stat = 0;
				}
			} else {
				transparentInfo.append(false);
				stat = 0;
			}
		}
		transparentInfo.appendByte(DataLinkLayer.START_END_FLAG);
		return transparentInfo;
	}
	public static BitMap toOriginalBitMap(BitMap transparentInfo) {
		BitMap originalInfo = new BitMap();
		int stat=0;
		int startEndFlag=0;
		for(int i=0;i<transparentInfo.length();i++) {
			if(transparentInfo.get(i)) {
				stat ++;
				originalInfo.append(true);
				if(stat==5&&!transparentInfo.get(i+1)) {
					i++;
					stat = 0;
				}else if(stat==5&&transparentInfo.get(i+1)&&!transparentInfo.get(i+2)){
					i += 2;
					startEndFlag ++;
					int tmp = 6;
					while(tmp-->0)originalInfo.clear();;
					stat = 0;
					if(startEndFlag>=2) return originalInfo;
				}
			} else {
				originalInfo.append(false);
				stat = 0;
			}
		}
		return originalInfo;
	}

}
