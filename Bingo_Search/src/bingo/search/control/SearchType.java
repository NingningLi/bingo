package bingo.search.control;

/**
 * ���ݲ�ͬ���������ͣ�ѡ��ͬ������Ŀ¼
 * @author BingYue
 *
 */
public class SearchType {
	
	public static String getSearchType(String searchType) {
		String searchPath = null;
		if(searchType.equals("image"))
			searchPath = "D:\\Bingo_Index_Image";
		else if (searchType.equals("news"))
			searchPath = "D:\\Bingo_Index_News";
		else if (searchType.equals("music"))
			searchPath = "D:\\Bingo_Index_Music";
		else if (searchType.equals("video"))
			searchPath = "D:\\Bingo_Index_Video";
		
		return searchPath;
	}


}
