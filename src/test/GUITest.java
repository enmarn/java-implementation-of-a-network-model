package test;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

public class GUITest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame jf = new JFrame("计算机网络实验");
		jf.setLayout(new BorderLayout());
        jf.setVisible(true);        // 设置窗体可视
        jf.setSize(1000, 500);        // 设置窗体大小
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);        // 设置窗体关闭方式
		JPanel panel1 = new JPanel();
		panel1.setSize(new Dimension(400, 500));
		JTextArea textArea1 = new JTextArea("dsdfghjly7t68rftcghvbhuftucjgvhbgiytfrdcfvgbhygtfrdtcfggvbgtfrcgvtfdbsdfvshdvksdjvgsdvjshdfvgsjhdgvsdvgfhvfcc");
		textArea1.setSize(new Dimension(400, 500));
		JTextArea textArea2 = new JTextArea("dsszzsdfghjly7t68rftcghvbhuftucjgvhbgiytfrdcfvgbhygtfrdtcfggvbgtfrcgvtfdbsdfvshdvksdjvgsdvjshdfvgsjhdgvsdvgfhvfcc");
		panel1.add(textArea1);
		JPanel panel3 = new JPanel();
		panel3.setSize(new Dimension(400, 500));
		panel3.add(textArea2);
        jf.add(panel1,BorderLayout.WEST);
        jf.add(panel3,BorderLayout.EAST);
        
        jf.setVisible(true);
	}

}
