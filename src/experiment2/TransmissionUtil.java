package experiment2;

import utils.BitMap;

public class TransmissionUtil {

	public BitMap toTransparentBitSet(BitMap originalInfo) {
		BitMap transparentInfo = new BitMap();
		int stat=0;
		for(int i=0;i<originalInfo.len;i++) {
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
		return transparentInfo;
	}
	public BitMap toOriginalBitSet(BitMap transparentInfo) {
		BitMap originalInfo = new BitMap();
		int stat=0;
		for(int i=0;i<transparentInfo.len;i++) {
			if(transparentInfo.get(i)) {
				stat ++;
				originalInfo.append(true);
				if(stat==5) {
					i++;
					stat = 0;
				}
			} else {
				originalInfo.append(false);
				stat = 0;
			}
		}
		return originalInfo;
	}

}
