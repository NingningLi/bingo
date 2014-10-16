package bingo.index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.NumberTools;

/**
 * �����ļ�����
 * @author BingYue
 */
public class Process {
	
	/**
	 * ��datafile�ļ������ֶ��з�
	 * @param dataFile ָ����зֵ��ļ���fileʵ��
	 * @return �зֺ���ļ�
	 */
	public static Document fileToDocument(File dataFile){
		Document doc = new Document();
		//Lucene�е�Field
		doc.add(new Field("title", dataFile.getName(), Store.YES,
				Index.ANALYZED));
		doc.add(new Field("link", reader(dataFile, "link"), Store.YES,
				Index.ANALYZED));
		doc.add(new Field("content", contentReader(dataFile), Store.YES,
				Index.ANALYZED));
		doc.add(new Field("size", NumberTools.longToString(dataFile.length()),
				Store.YES, Index.NOT_ANALYZED));
		doc.add(new Field("path", dataFile.getAbsolutePath(), Store.YES,
				Index.NOT_ANALYZED));
		return doc;	
	}
	
	/**
	 * ��ȡf��field�ֶε�ֵ
	 */
	public static String reader(File f, String field) {
		String content = "null";
		try {
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.startsWith(field)) {
					content = line.replaceAll(field + ":", "");
					break;
				}
			}
			//�ر��ļ���
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}
	
	/**
	 * ��ȡf���ض�������
	 * 
	 * @param f
	 * @return
	 */
	public static String contentReader(File f) {
		StringBuffer sb = new StringBuffer();
		int i = 0;
		try {
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			for (String line = null; (line = br.readLine()) != null;) {
				i++;
				if (i >= 7) {
					sb.append(line);
				}

			}
			//�ر��ļ���
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString().replaceAll("description:", "");
	}

}
