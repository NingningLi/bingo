package bingo.spider.spider;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;

import bingo.spider.dao.Dao;

/**
 * ����������
 * @author BingYue
 *
 */
public class Spider {

	Connection conn = null;
	Statement stat = null;
	Dao dao = null;
	//����������� ����
	protected Collection<Object> workloadError = new ArrayList<Object>(3);
	//����ȴ���ȡ������
	protected Collection<Object> workloadWaiting = new ArrayList<Object>(3);
	//��������ȡ������
	protected Collection<Object> workloadProcessed = new ArrayList<Object>(3);

	/**
	 * ����IspiderReportable�ӿڵ�ʵ����
	 */
	protected ISpiderReportable report;

	/**
	 * ȡ����ȡ����
	 */
	protected boolean cancel = false;

	/**
	 * ������
	 * @param report ������ȡ������Ϣ
	 */
	public Spider(ISpiderReportable report) {
		this.report = report;
		initDatabase();
	}
	
	/**
	 * ��ʼ�����ݿ⹤������
	 */
	public void initDatabase() {
		dao = new Dao();
		conn = dao.getConnection();
		stat = dao.getStatement(conn);
	}

	/**
	 * workloadWaiting��get����
	 * ��ʼֵ���û�����
	 * @return url����
	 */
	public Collection<Object> getWorkloadWaiting() {
		return workloadWaiting;
	}

	/**
	 * workloadProcessed��get����
	 * @return
	 */
	public Collection<Object> getWorkloadProcessed() {
		return workloadProcessed;
	}

	/**
	 * workloadError��get����
	 * @return 
	 */
	public Collection<Object> getWorkloadError() {
		return workloadError;
	}

	/**
	 * ������ж���
	 * ȷ������ʼʱURL�б�Ϊ��
	 */
	public void clear() {
		getWorkloadError().clear();
		getWorkloadWaiting().clear();
		getWorkloadProcessed().clear();
	}

	/**
	 * cancel��������cancel��־����Ϊ��
	 */
	public void cancel() {
		cancel = true;
		//�ر�ȫ�����ݿ�����
//		dao.closeAll();
		
	}

	/**
	 * ���url����
	 * @param url
	 */
	public void addURL(URL url) {
		
		//ȷ������ʼʱurl����Ϊ��
		if (getWorkloadWaiting().contains(url))
			return;
		if (getWorkloadError().contains(url))
			return;
		if (getWorkloadProcessed().contains(url))
			return;
		
		log("����ӵ����ݿ���: " + url);
		getWorkloadWaiting().add(url);
		Date now = new java.util.Date();
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = dateformat.format(now);
		String urlStr = url.toString();
		String sqlstmt = "insert into test.link_info values(" + "0" + ",'"
				+ urlStr + "','" + date + "')";
       dao.saveData(stat, sqlstmt);
	}

	/**
	 * URL���ݼ�����HTML
	 * @param url
	 */
	public void processURL(URL url) {
		try {
			//����log����������̨��̬�����Ϣ
			log("��ȡ��: " + url);
			//����URLConnection����
			/**
			 * html�ṹ
			 * http-equiv="Content-Type" content="text/html; charset=gb2312"
			 */
			URLConnection connection = url.openConnection();
			if ((connection.getContentType() != null)&& !connection.getContentType().toLowerCase().startsWith("text/")) {
				getWorkloadWaiting().remove(url);
				getWorkloadProcessed().add(url);
				log("Not processing because content type is: "
						+ connection.getContentType());
				return;
			}
			//��������
			InputStream is = connection.getInputStream();
			Reader r = new InputStreamReader(is);
			HTMLEditorKit.Parser parse = (new HtmlHandle()).getParser();
			parse.parse(r, new Parser(url), true);
		} catch (IOException e) {
			getWorkloadWaiting().remove(url);
			getWorkloadError().add(url);
			log("����: " + url);
			report.spiderURLError(url);
			return;
		}
		getWorkloadWaiting().remove(url);
		getWorkloadProcessed().add(url);
		log("�����ȡ: " + url);

	}

	/**
	 * ��ʼ�������
	 */
	public void begin() {
		cancel = false;
		while (!getWorkloadWaiting().isEmpty() && !cancel) {
			Object list[] = getWorkloadWaiting().toArray();
			for (int i = 0; (i < list.length) && !cancel; i++)
				processURL((URL) list[i]);
		}
	}

	/**
	 * �ж���������
	 * ��֪ͨspider
	 */
	protected class Parser extends ParserCallback {
		protected URL base;

		public Parser(URL base) {
			this.base = base;
		}

		public void handleStartTag(Tag tag, MutableAttributeSet attribute,
				int pos) {
			String href = (String) attribute.getAttribute(HTML.Attribute.HREF);

			if ((href == null) && (tag == HTML.Tag.FRAME))
				href = (String) attribute.getAttribute(HTML.Attribute.SRC);

			if (href == null)
				return;
			int i = href.indexOf('#');
			if (i != -1)
				href = href.substring(0, i);

			if (href.toLowerCase().startsWith("mailto:")) {
				report.spiderFoundEMail(href);
				return;
			}

			handleLink(base, href);

		}

		protected void handleLink(URL base, String str) {
			try {
				URL url = new URL(base, str);
				if (report.spiderFoundURL(base, url))
					addURL(url);
			} catch (MalformedURLException e) {
				log("Found malformed URL: " + str);
			}
		}
	}

	/**
	 * ����̨�����Ϣ
	 * @param entry
	 *            The information to be written to the log.
	 */
	public void log(String entry) {
		System.out.println((new Date()) + ":" + entry);
	}

}
