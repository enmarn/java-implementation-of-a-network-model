package experiment2;

import org.omg.CORBA.SystemException;

public class Main {
	public static void main(String[] args) {
		String string = "10101010101010101010010101010101011010101";
		System.out.println("原字符串："+string);
		string = TransmissionUtil.toParentTransparentBitString(string);
		System.out.println("透明处理的字符串："+string);
		string = TransmissionUtil.toOriginalBitString(string);
		System.out.println("去透明化的字符串："+string);
	}
}
