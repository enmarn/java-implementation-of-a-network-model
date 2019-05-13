package log;


public class Log {
	public static String who = "";
	public static LogUtil err   = new LogUtil("[error_] ",false, System.err);
	public static LogUtil Physic= new LogUtil("[physic] ",false, System.err);
	public static LogUtil frame = new LogUtil("[frame_] ",false, System.err);
	public static LogUtil BNP_0 = new LogUtil("[BNP___] ",false, System.err);
	public static LogUtil Linked= new LogUtil("[linked] ",false, System.err);
	public static LogUtil info  = new LogUtil("[info__] ",false, System.out);
	static {
		err.show = true;
//		Physic.show = true;
//		frame.show = true;
		BNP_0.show = true;
//		Linked.show= true;
		info.show = true;
	}
}

