package experiment4;

import java.io.PrintStream;

public class Log {
	public static String who = "";
	public static LogUtil err   = new LogUtil("[err  ] ",false, System.err);
	public static LogUtil debug = new LogUtil("[debug] ",true, System.err);
	public static LogUtil info  = new LogUtil("[info ] ",true, System.out);
}
class LogUtil{
	private boolean show = true;
	private PrintStream stream;
	String head;
	public LogUtil(String header,boolean show, PrintStream stream) {
		head = header;
		this.show= show;
		this.stream = stream;
	}
	public synchronized void println(String msg){
		if(show) stream.println(head + Log.who + "	 " + msg);
	}
	public synchronized void println(String who, String msg){
		if(show) stream.println(head + who + "	 " + msg);
	}
}
