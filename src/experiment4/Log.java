package experiment4;

import java.io.PrintStream;

public class Log {
	public static String who = "";
	public static LogUtil err   = new LogUtil("[error] ",false, System.err);
	public static LogUtil BNP_0 = new LogUtil("[BNP__] ",false, System.err);
	public static LogUtil frame = new LogUtil("[frame] ",true, System.err);
	public static LogUtil info  = new LogUtil("[info_] ",true, System.out);
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
		if(show) stream.println(head + Log.who + " " +(System.currentTimeMillis())+"	 " + msg);
	}
	public synchronized void println(String who, String msg){
		if(show) stream.println(head + who + " " +(System.currentTimeMillis()) + "	 " + msg);
	}
	/**
	 * 以该符号输出一行分隔行
	 * @param separater
	 */
	public synchronized void printSepln(){
		if(!show) return;
		char[] a = new char[54];
		for (int j = 0; j < a.length; j++) {
			a[j] = '-';
		}
		stream.println(new String(a));
		stream.println();
	}
	public synchronized void printSepln(String name){
		if(!show) return;
		String a = "-------------* " + String.format("%16s", name) + "		" + "*-------------";
		stream.println(new String(a));
	}
}
