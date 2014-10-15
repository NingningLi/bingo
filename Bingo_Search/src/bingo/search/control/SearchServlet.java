package bingo.search.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jeasy.analysis.MMAnalyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;

/**
 * �ṩ���������Servlet
 * @author BingYue
 *
 */
public class SearchServlet extends HttpServlet {

	/**
	 * Ĭ�ϵ����л�ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * ��ʼ������
	 */
	public void init() throws ServletException{
		
	}
	
	/**
	 * Post����
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		        
		        //���¶����������Ӧ�ı���,�����Ľ�����ȷ�Ľ���
				request.setCharacterEncoding("gbk");
				response.setContentType("text/html;charset=gbk");
				
				//����Ĭ�ϵ���Ϣ������
		        String searchPath = "D:\\Bingo_Index";
		        
		        //��ȡ�û���ȡ���������Ĺؼ���
				String keyWords = (String) request.getParameter("keyWords");
				//ServletContextʵ��
				ServletContext sc = getServletContext();
				/**
				 * �ؼ���Ϊ�գ�ת��errorҳ��
				 * ����Ҳ�������javascript valadition��֤
				 */
				if(keyWords==null){
					RequestDispatcher rd = sc.getRequestDispatcher("/error.jsp");
					rd.forward(request, response);
					return;
				}
				
				//���û��������������ж�
				//��Բ�ͬ�Ĳ�ѯ���ͣ�ȥ��ͬ��Ŀ¼�¼���
				String searchType = request.getParameter("searchType");
				if(!searchType.equals("webPage") && (searchType != null)) {
					searchPath = SearchType.getSearchType(searchType);
				}
				
				//��ҳ����
				String startLocationTemp = request.getParameter("startLocation");
				if(startLocationTemp != null) {
			    //parseInt ��������
				int startLocation = Integer.parseInt(startLocationTemp);
				System.out.println(startLocation);
				}
		        
				//�ִ���
				Analyzer analyzer = null;
				//ҳ�����
				Highlighter highlighter = null;
				/**
				 * Document��Lucene����Ҫ����
				 */
				List<Document> list = new ArrayList<Document>();
				Integer totalCount = null;

				try {
					IndexSearcher indexSearch = new IndexSearcher(searchPath);
					analyzer = new MMAnalyzer();
					String[] field = { "title", "content", "link"};
					//
					Map<String, Float> boosts = new HashMap<String, Float>();
					boosts.put("title", 3f);
					QueryParser queryParser = new MultiFieldQueryParser(field,
							analyzer, boosts);
					Filter filter = null;
					try {
						/**
						 * �����Ǿ���Ĳ�ѯʵ��
						 * queryParser��Lucene��Ҫ�Ĳ�ѯ��
						 */
						Query query = queryParser.parse(keyWords);
						TopDocs topDocs = indexSearch.search(query, filter, 1000,
								new Sort(new SortField("size")));
						totalCount = new Integer(topDocs.totalHits);
						SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter(
								"<font color='red'>", "</font>");
						//��������
						highlighter = new Highlighter(simpleHTMLFormatter,
								new QueryScorer(query));
						
						highlighter.setTextFragmenter(new SimpleFragmenter(70));
						for (int i = 0; i < topDocs.totalHits; i++) {
							ScoreDoc scoreDoc = topDocs.scoreDocs[i];
							int docSn = scoreDoc.doc; // �ĵ��ڲ����
							Document doc = indexSearch.doc(docSn); // ���ݱ��ȡ����Ӧ���ĵ�
							list.add(doc);
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} catch (CorruptIndexException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				RequestDispatcher rd = sc.getRequestDispatcher("/result.jsp");
				request.setAttribute("keyWords", keyWords);
				request.setAttribute("totalCount", totalCount);
				request.setAttribute("docList", list);
				request.setAttribute("analyzer", analyzer);
				request.setAttribute("highlighter", highlighter);
				/**
				 * RequestDispatcher�ṩ����������forward��include
				 */
				rd.forward(request, response);
	}
	/**
	 * Get����
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				this.doPost(request, response);
			}
	
	/**
	 * ���ٷ���
	 */
	public void destroy() {
		super.destroy();
	}


}
