package bingo.spider.spider;

import java.net.URL;
/**
 * ����ӿڣ���Main����ʵ��
 * @author BingYue
 *
 */
public interface ISpiderReportable {

	/**
	 * ����λһ��urlʱ�����ã��������true
	 * �����ִ����ȥ��������������
	 */
	public boolean spiderFoundURL(URL base, URL url);
	/**
	 * ����ʧЧ������
	 * �������δ��Ӧ
	 * @param url
	 */
	public void spiderURLError(URL url);
	
	/**
	 * �����ʼ���ַ
	 * @param email
	 */
	public void spiderFoundEMail(String email);
}
