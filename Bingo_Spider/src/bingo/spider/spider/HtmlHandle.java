package bingo.spider.spider;

import javax.swing.text.html.HTMLEditorKit;

public class HtmlHandle extends HTMLEditorKit {

	/**
	 * Ĭ�����л�ID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * ʹ��Swing HTML������
	 * HTMLEditorKit��getParser()������Protected,
	 * ֻ��ͨ����д���෽����ʹ��
	 */
	@Override
	public Parser getParser(){
		return super.getParser();
	}

}
