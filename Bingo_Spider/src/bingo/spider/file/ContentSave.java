package bingo.spider.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * �ļ�IO����
 * �����ȡ�����ĵ���Ϣ
 * @author BingYue
 *
 */
public class ContentSave {
	
	/**
	 * ����html���ݵ������ļ��洢ϵͳ
	 * @param title
	 * @param content
	 */
	public static void SaveHTML(String title,String content){
		File f = new File("D:\\Bingo_Document\\HTMLFile\\" +title + ".html");
		try {
			FileWriter fw = new FileWriter(f,true);
			fw.write(content);
			fw.close();
			
		} catch (IOException e) {
			ContentSave.SaveLog(title + ".html", "IOException");
		}
	}
	/**
	 * ����txt�ı��������Ĵ洢ϵͳ
	 * �˴�������ı��ļ���������Lucene��������
	 * @param title
	 * @param content
	 */
    public static void SaveTxt(String date, String title, String link, String keywords, String description){
    	File f = new File("D:\\Bingo_Document\\TxtFile\\" +title + ".txt");
//    	File f = new File("D:\\Bingo_Document\\TempFile\\" +title + ".txt");
		try {
			FileWriter fw = new FileWriter(f,true);
			fw.write("author:Bingo!" + "\r\n");
			fw.write("date:" + date + "\r\n");
			fw.write("title:" + title + "\r\n");
			fw.write("link:" + link + "\r\n");
			fw.write("keywords:" + keywords + "\r\n");
			fw.write("description:" + description + "\r\n");
			fw.close();
		} catch (IOException e) {
			ContentSave.SaveLog(title + ".txt", "IOException");
		}
	}
    /**
     * ������־�ļ�
     * @param title
     * @param content
     */
    public static void SaveLog(String tar, String errMsg){
    	File f = new File("D:\\Bingo_Document\\LogFile\\" + "log.text");
		try {
			FileWriter fw = new FileWriter(f,true);
			fw.write(tar + "\r\n");
			fw.write(errMsg + "\r\n");
			fw.write("\r\n");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
