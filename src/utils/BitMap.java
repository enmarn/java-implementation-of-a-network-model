package utils;

import java.util.BitSet;
/*
 * 由于计算机中的各种函数都是比较高层的封装，倘若还使用底层的代码，徒增困难
 * 因此代码重构，不再使用比特流，而使用String来模拟比特流
 */
public class BitMap extends BitSet{
	/**
	 * 
	 */
	private int len = 0;
	private static final long serialVersionUID = 1L;
	/**
	 * return : the length of BitMap
	 */
	@Override
	public int length() {
		return len;
	}
	/**
	 * clear the last bit 
	 */
	@Override
	public void clear() {
		super.clear(length()-1);
		len --;
	}
	/**
	 * append a value into the tail of BitMap
	 * @param value
	 */
	public void append(boolean value) {
		set(len, value);
		len ++;
	}

	/**
	 * append a byte
	 * @param aByte
	 */
	public void appendByte(byte aByte) {
		for(int i=0;i<8;i++)
			append(((aByte>>(7-i))&1)>0);
	}
	
	@Override
	public String toString() {
		String str="";
		for(int i=0;i<len;i++) {
			str = str + (get(i)?1:0);
		}
		return str;
	}
	/**
	 * parse a bitstr into a BitMap
	 * @param bitstr
	 */
	public void valueOf(String str) {
		super.clear();
		for(int i=0;i<str.length();i++) {
			char tmp = str.charAt(i);
			if(tmp=='1')append(true);
			else if(tmp=='0') append(false);
		}
	}
	/**
	 * parse a bitstr into a BitMap
	 * @param bitstr
	 * @return A BitMap
	 */
	public static BitMap valueOf(byte[] bytes) {
		BitMap bMap = new BitMap();
		int len = 8*bytes.length;
		for(int i=0;i<len;i++)
			bMap.append((bytes[i/8]&(1<<(7-i%8)))>0);
		return bMap;
	}
	
	
}