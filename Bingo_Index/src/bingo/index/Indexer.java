package bingo.index;

import java.io.File;
import java.io.IOException;

import jeasy.analysis.MMAnalyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.LockObtainFailedException;

/**
 * ��ָ�����ļ���������
 * @author BingYue
 */
public class Indexer {
	
	/**
	 * �����ڲ���̬����
	 * ��ʼ������������
	 * ʹ��Lucene�������Ĳ������ͬ������JDBC�������ݿ��ʵ��
	 */
	public static void start(String dataDir, String indexDir){
		
		//Java IO����
		//���ڴ��ﴴ������Ϊpathname��File����
		File dataFile=new File(dataDir);
		File indexFile=new File(indexDir);
		//
		index(dataFile, indexFile);
		//�����������
		quit();
	}
	
	/**
	 * ִ��������������
	 * @param dataFile
	 * @param indexFile
	 */
	private static void index(File dataFile, File indexFile) {
		//��֤�������ļ�Ŀ¼�Ƿ���ȷ
		if (!dataFile.exists() || !dataFile.isDirectory()) {
			System.out.println("Error message:" + dataFile + " does not exist or is not a directory.");
		}
		//����һ���ִ���
		//����ʹ��MMAnalyzer����Ϊ���ķִ����
		Analyzer zh_cnAnalyzer = new MMAnalyzer();
		
		//����������
		//IndexWriter��Lucene��Ҫ���ִ࣬�д��������ȹ���
		
		try {
			//����IndexWriter���ļ�·�����ִ������Ƿ񴴽�����󳤶ȣ�Ĭ�ϲ������ƣ�
			//�˴�iscreateΪtrue,����Ŀ¼���Ƿ����ļ�����������ɾ�����½�
			IndexWriter writer = new IndexWriter(indexFile, zh_cnAnalyzer, true, MaxFieldLength.UNLIMITED);
			//������������
			indexDirectory(writer, dataFile);
			/**
			 * �����Ż�����
			 * Lucene 3.5�Ժ�İ汾�Ѿ������ṩ�������
			 * ȡ����֮����forcemerge
			 */
			writer.optimize();
			//������� �ر�������
			writer.close();
	
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	/**
	 * ��Ŀ¼�µ�txt�ļ�������������
	 * @param writer ������ʵ��
	 * @param dir �������ļ���Ŀ¼��
	 */
	private static void indexDirectory(IndexWriter writer, File dir) {
		
		//��ȡ��ǰ�ļ�Ŀ¼�µ������ļ����ļ���
		File[] files = dir.listFiles();
		/**
		 * ����File[]����
		 * �����ļ��У�ִ�еݹ�������������
		 * txt�ı��ĵ���ִ������indexfile()
		 */
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			//����·������ʾ���ļ��Ƿ���һ��Ŀ¼
			//�����¼�Ŀ¼���� �ݹ����
			if (f.isDirectory()) {
				indexDirectory(writer, f);
			} 
			//String endsWith()����
			//ʵ���ļ��������Ĳ���
			else if (f.getName().endsWith(".txt")) {
				indexFile(writer, f);
			}
		}
	}
    /**
     * ��������ļ�����
     * @param writer
     * @param f
     */
	private static void indexFile(IndexWriter writer, File f) {
		//³���ԣ����ǵ��������
		//�ļ��п��������ػ��߲��ɶ�д���˳�����
		if (f.isHidden() || !f.exists() || !f.canRead()) {
			return;
		}
		//����fileToDocument()
		Document doc = Process.fileToDocument(f);
		try {
			writer.addDocument(doc);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * �˳���������������
	 */
	private static void quit() {
		System.out.println("�����������,�����˳�.");
		System.exit(0);
	}
}

