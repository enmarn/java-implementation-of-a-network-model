package experiment4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;

public class Host1 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Log.who = "HOST1";
		Log.info.println("创建");
		BackNProtocol backNProtocol = new BackNProtocol(8999);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					backNProtocol.receive();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
        File testfile = new File("input/test1");
        @SuppressWarnings("resource")
		Reader reader = new FileReader(testfile);
        char[] bits = new char[1024];
        reader.read(bits);
		backNProtocol.send(bits);
		
	}

}
