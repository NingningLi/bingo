package bingo.spider.spider;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * ����Swing���
 * ����ʵ�� Runnable�ӿڵ���,ʵ������
 * @author BingYue
 *
 */
public class Main extends JFrame implements Runnable,ISpiderReportable{

	/**
	 * Ĭ�ϵ����л�ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * SWing��� ��ϵ��Ԫ��
	 */
	JLabel promoteLabel = new JLabel();
	//��ʼ��ť
	JButton begin = new JButton();
	//������ַ�ı���
	JTextField url = new JTextField();
	//��ǰ���� ��ǩ
	JLabel current = new JLabel();
	//��ȷ���ӣ��������������ӣ�
	JLabel goodLinksLabel = new JLabel();
    //���� ����
	//������ָԭ������������ʧЧ�����ӡ�
	protected int badLinksCount = 0;
	//���� ��ǩ
	JLabel badLinksLabel = new JLabel();
	//�������Ӽ���
	protected int goodLinksCount = 0;
	//�·�������
	JScrollPane errorScroll = new JScrollPane();

	JTextArea badLinksTextArea = new JTextArea();

	protected Thread bgThread;

	protected Spider spider;

	protected URL base;
	
	public Main(){
		//������ ����������巽��
		drawSpiderFrame();
	}
	
	/**
	 * Main���main����
	 * @param args
	 */
	static public void main(String args[]) {
		(new Main()).setVisible(true);
	}
	/**
	 * ������
	 */
	public void drawSpiderFrame() {
		/**
		 * �������
		 */
		setSize(405, 288);
		setVisible(true);
		setTitle("Bingo_Spider");
		//����ڸ�����λ�� null��ʾ��������
//		setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		/**
		 * ����������Ԫ��
		 */
		promoteLabel.setBounds(12, 12, 84, 12);
		promoteLabel.setText("������URL:");
		getContentPane().add(promoteLabel);

		//��ʼ��ť
		begin.setBounds(12, 36, 84, 24);
		begin.setText("��ʼ");
		begin.setActionCommand("Begin");
		getContentPane().add(begin);

		url.setBounds(108, 36, 288, 24);
		getContentPane().add(url);
		//����
		errorScroll.setBounds(12, 120, 384, 156);
		errorScroll.setAutoscrolls(true);
		errorScroll
				.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		errorScroll
				.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		errorScroll.setOpaque(true);
		getContentPane().add(errorScroll);
		
		//
		badLinksTextArea.setEditable(false);
		badLinksTextArea.setBounds(0, 0, 366, 138);
		errorScroll.getViewport().add(badLinksTextArea);

		current.setText("��������: ");
		current.setBounds(12, 72, 384, 12);
		getContentPane().add(current);

		goodLinksLabel.setText("����������Ŀ: 0");
		goodLinksLabel.setBounds(12, 96, 192, 12);
		getContentPane().add(goodLinksLabel);
		badLinksLabel.setText("������Ŀ: 0");
		badLinksLabel.setBounds(216, 96, 96, 12);
		getContentPane().add(badLinksLabel);

		beginningActionListener begActListener = new beginningActionListener();
		begin.addActionListener(begActListener);
	}
	
	/**
	 * �¼�֪ͨ
	 */
	public void addNotify() {
		Dimension size = getSize();

		super.addNotify();

		if (frameSizeAdjusted)
			return;
		frameSizeAdjusted = true;

		Insets insets = getInsets();
		javax.swing.JMenuBar menuBar = getRootPane().getJMenuBar();
		int menuBarHeight = 0;
		if (menuBar != null)
			menuBarHeight = menuBar.getPreferredSize().height;
		setSize(insets.left + insets.right + size.width, insets.top
				+ insets.bottom + size.height + menuBarHeight);
	}

	boolean frameSizeAdjusted = false;

	/**
	 * �ڲ��� �¼�����
	 * ���� ��ʼ/ȡ�� ��ť����
	 */
	class beginningActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			Object object = event.getSource();
			if (object == begin)
				beginningActionPerformed(event);
		}
	}
	/**
	 * @param event
	 */
	void beginningActionPerformed(ActionEvent event) {
		if (bgThread == null) {
			begin.setText("Cancel");
			bgThread = new Thread(this);
			bgThread.start();
			goodLinksCount = 0;
			badLinksCount = 0;
		} else {
			spider.cancel();
		}

	}
	/**
	 * ISpiderReportable�ӿڷ�����ʵ��
	 * ��־���õ�����
	 * @param base
	 * @param url
	 * @return
	 */
	public boolean spiderFoundURL(URL base, URL url) {
		UpdateCurrentStats cs = new UpdateCurrentStats();
		cs.msg = url.toString();
		SwingUtilities.invokeLater(cs);

		if (!checkLink(url)) {
			UpdateErrors err = new UpdateErrors();
			err.msg = url + "(on page " + base + ")\n";
			SwingUtilities.invokeLater(err);
			badLinksCount++;
			return false;
		}

		goodLinksCount++;
		return true;
	}
	/**
	 * ���url�����Ƿ�����
	 * @param url
	 * @return
	 */
	protected boolean checkLink(URL url) {
		try {
			//������������true�����򷵻�false
			URLConnection connection = url.openConnection();
			connection.connect();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	class UpdateErrors implements Runnable {
		public String msg;

		public void run() {
			badLinksTextArea.append(msg);
		}
	}
	
	class UpdateCurrentStats implements Runnable {
		public String msg;

		public void run() {
			current.setText("��ǰ����: " + msg);
			goodLinksLabel.setText("����������Ŀ: " + goodLinksCount);
			badLinksLabel.setText("����: " + badLinksCount);
		}
	}
	
	/**
	 * 
	 */
	@Override
	public void run() {
		try {
			badLinksTextArea.setText("");
			spider = new Spider(this);
			spider.clear();
			base = new URL(url.getText());
			spider.addURL(base);
			spider.begin();
			Runnable doLater = new Runnable() {
				public void run() {
					begin.setText("Begin");
				}
			};
			SwingUtilities.invokeLater(doLater);
			bgThread = null;

		} catch (MalformedURLException e) {
			UpdateErrors err = new UpdateErrors();
			err.msg = "Bad address.";
			SwingUtilities.invokeLater(err);

		}
	}

	/**
	 * �ӿ��еķ���
	 */
	@Override
	public void spiderURLError(URL url) {
		//������������
		badLinksCount++;
		
	}
	/**
	 * �ӿ��еķ���
	 */
	@Override
	public void spiderFoundEMail(String email) {
		//��email��ַ��ʱ��������
		
	}

}
