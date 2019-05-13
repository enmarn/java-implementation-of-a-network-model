package datalinklayer;

public class BitStream{
	public byte[] bytes;
	public int len;
	public BitStream() {
		len = 0;
	}
	public BitStream(byte[]bits) {
		bytes = bits;
		len = bits.length*8;
	}
	public BitStream(byte[]bits,int length) {
		bytes = bits;
		len = length;
	}
	public String toString() {
		String string = "";
		for(int i=0;i<len;i++) {
			string += get(i);
		}
		return string;
	}
	public void setIndex(byte bit, int index) {
		try {
			if(index>=len)	throw new Exception("比特流越界");
//			System.out.println(bytes.length);
			bytes[index/8] |= (bit<<7) >> (index%8);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public byte get(int index) {
		try {
			if(index>=len)	throw new Exception("比特流越界");
			if((bytes[index/8]&(0b10000000>>(index%8)))>0) return 1;
			return 0;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	public void append(byte a) {
		len ++;
		setIndex(a, len-1);
	}
	public void pop() {
		setIndex((byte) 0, len-1);
		len --;
	}
	public static String getBitStringByBytes(byte[] bytes) {
			try {
				if(bytes==null) throw new Exception("接收错误");
				String a = "";
				BitStream stream = new BitStream(bytes);
				int tmp = bytes.length*8;
				for(int i=0;i<tmp;i++)
					a += (stream.get(i));
				return a;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return "";
	}
	public static byte[] getBytesByBitString(String string) {
		BitStream bStream = new BitStream();
		bStream.bytes = new byte[string.length()/8+((string.length()%8)>0?1:0)];
		for(int i=0;i<string.length();i++) {
			bStream.append((byte) (string.charAt(i)-'0'));
//			System.out.println(bStream);
		}
		int tmp = bStream.bytes.length*8-bStream.len;
		for(int i=0;i<tmp;i++) {
			bStream.append((byte) 0);
//			System.out.println(bStream);
		}
		return bStream.bytes;
	}
	public static String getBitStringByBytes(byte[] bytes, int len) {
		// TODO Auto-generated method stub
		String a = "";
		BitStream stream = new BitStream(bytes);
		int tmp = len*8;
		for(int i=0;i<tmp;i++)
			a += (stream.get(i));
		return a;
	}
}