package log;

import java.io.PrintStream;

public class LogUtil{
	public boolean show = true;
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