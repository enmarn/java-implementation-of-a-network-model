package utils;

import java.util.BitSet;

public class BitMap extends BitSet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int len=0;
	public void append(boolean value) {
		set(len, value);
		len ++;
	}
	@Override
	public String toString() {
		String str="";
		for(int i=0;i<len;i++) {
			str = str + (get(i)?1:0);
		}
		return str;
	}
	public void valueOf(String str) {
		for(int i=0;i<str.length();i++) {
			char tmp = str.charAt(i);
			if(tmp=='1')append(true);
			else if(tmp=='0') append(false);
		}
	}
}