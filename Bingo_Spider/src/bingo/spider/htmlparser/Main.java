package bingo.spider.htmlparser;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import bingo.spider.dao.Dao;
import bingo.spider.file.ContentSave;

/**
 * ��������html�ļ��ķ���
 * @author BingYue
 *
 */
public class Main {
	
	private Connection conn = null;//���ݿ������

	private Statement stat = null;//���ݿ�����

	private ResultSet rs = null;//���ݿⷵ�صĽ����

	private Dao dao = null;//���ݷ��ʶ���
    
	/**
	 * Main����
	 * @param args
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void main(String args[]) throws SQLException, IOException {
		new Main().start();
	}
	
	/**
	 * ���캯��
	 * ��ʼ�����ݿ�����
	 */
	public Main() {
		initDatabase();
    }
    
	/**
	 * �������ݿ�ĳ�ʼ������
	 */
	public void initDatabase() {
		dao = new Dao();
		conn = dao.getConnection();
		stat = dao.getStatement(conn);
		rs = dao.getResuleSet(stat);
	}
    
	 /**
	  * ����Main��
	  */
	public void start() throws SQLException, IOException {
		while (rs.next()) {
			String link = rs.getString("link");
			parser(link);
		}
		//δʹ��PreparedStatement
		dao.closeAll(conn, null,stat, rs);
		quit();
	}
	
	public void parser(String link) throws IOException {
		String html = HtmlParser.getWebPage(link);
		String title = HtmlParser.getTitle(html);
		String keywords = HtmlParser.getKeywords(html);
		String description = HtmlParser.getDes(html);
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		
		//���浽�����ļ�ϵͳ
		ContentSave.SaveHTML(title, html);
		ContentSave.SaveTxt(date, title, link, keywords, description);
	}
	
	/**
	 * �������н���ʱ�Ƴ�����
	 */
	public void quit() {
		System.out.println("��ҳ�Ѿ��������,�����˳�!");
		System.exit(0);
	}

	

}
