package bingo.index;

/**
 * ����indexer��ִ����������
 * @author BingYue
 *
 */
public class Main {
	
	/**
	 * ��ں���
	 * @param args
	 */
	public static void main(String[] args) {
		
		/**
		 * ������Ŀ¼
		 */
//     	String dataFile="D:\\Bingo_Document\\TempFile\\";
    	String dataFile="D:\\Bingo_Document\\TxtFile\\";
		//�������Ŀ¼
    	String indexFile="D:\\Bingo_Index\\";
//		String indexFile="D:\\Bingo_Index_News\\";
//		String indexFile="D:\\Bingo_Index_Image\\";
//		String indexFile="D:\\Bingo_Index_Music\\";
//		String indexFile="D:\\Bingo_Index_video\\";
		//������������
		Indexer.start(dataFile, indexFile);
	}

}
