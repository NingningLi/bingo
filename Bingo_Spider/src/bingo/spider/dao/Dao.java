package bingo.spider.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * ���ݿ�洢DAO
 * @author BingYue
 *
 */
public class Dao {
	
	//��̬ȫ�ֱ���
	public static String DRIVERNAME="com.mysql.jdbc.Driver";
	public static String URL="jdbc:mysql://localhost/";
	public static String USER="root";
	public static String PASSWORD="mysql";
	/*
	 * ��̬����飬ʵ����ʱ����JDBC����
	 */
	static{
		try {
			Class.forName(DRIVERNAME);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * ������ݿ�����
	 * @return
	 */
	public Connection getConnection(){
		Connection conn=null;
		 try {
			conn=DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;	
	}
	
	/**
	 * Ԥ��ѯ���
	 * @param conn
	 * @return
	 */
	public PreparedStatement getPreStatement(Connection conn){
		PreparedStatement preStat=null;
		StringBuffer sqlbuffer=new StringBuffer();
		sqlbuffer.append("SELECT link FROM test.LINK_INFO");
		try {
			preStat=conn.prepareStatement(sqlbuffer.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return preStat;
	}
	
	/**
	 * ��ò�ѯ�����
	 * @param preStat
	 * @return
	 */
	public ResultSet getResuleSet(PreparedStatement preStat){
		ResultSet rs = null;
		try {
			rs=preStat.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	/**
	 * ��ȡ���ݿ�link_info���д����url
	 * @param stat
	 * @return
	 */
	public ResultSet getResuleSet(Statement stat){
		ResultSet rs = null;
		String sql = "SELECT link FROM test.LINK_INFO";
		try {
			rs = stat.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * ��ѯ����
	 * @param conn
	 * @return
	 */
	public Statement getStatement(Connection conn) {
		Statement stat = null;
		try {
			stat = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stat;
	}
	
	public void saveData(Statement stat, String str){
		try {
			stat.executeUpdate(str);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �ر����ݿ�����
	 * @param conn
	 * @param preStat
	 * @param stat
	 * @param rs
	 */
	public void closeAll(Connection conn,PreparedStatement preStat,Statement stat,ResultSet rs){
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (stat != null) {
			try {
				stat.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
