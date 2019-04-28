package experiment4;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;

public class Host2 {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Log.who = "HOST2";
		Log.info.println("创建");
		BackNProtocol backNProtocol = new BackNProtocol(9001);
        File testfile = new File("input/test1");
        @SuppressWarnings("resource")
		Reader reader = new FileReader(testfile);
        char[] bits = new char[1024];
        int i = reader.read(bits);
		backNProtocol.send(new String(bits, 0, i));
//		backNProtocol.close();
	}

}
