package edu.kh.jdbc.common;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class JDBCTemplateH {
	
	// 참조변수 선언
	private static Connection conn = null;
	
	// 1. Connection 연결용 메소드
	public static Connection/*반환형*/ getConnection() {
		
		try {
			
			// 기작성된 Connection 객체 있는지(null 값이 아니며 닫혀 있지 않을 시)  확인, 연결이 열려 있다면 호출부로 돌아감
			if( conn != null && !conn.isClosed() ) return conn;
			
			// XML 의 정보를 담을 Map 의 자식클래스 Properties 객체 생성
			Properties prop = new Properties(); 
			
			// driver.xml 파일을 inputStream 통해 읽어와 prop 이라는 Properties 객체에 저장(key-value 형태로 저장됨)
			prop.loadFromXML(new FileInputStream("driver.xml"));
			
			// 드라이버 메모리 탑재
			Class.forName(prop.getProperty("driver")); 
			
			// prop 에 저장된 값을 이용해 Connection 객체 생성
			conn = DriverManager.getConnection(prop.getProperty("url"),
											   prop.getProperty("dbName"),
											   prop.getProperty("dbPass"));
			
			// Connection 이용해 AutoCommit off
			conn.setAutoCommit(false);
			
		// 예외발생 처리	
		} catch (Exception e) { 
			
			System.out.println("커넥션 생성 중 예외 발생 : JDBCTemplate 내 getConnection()");
			e.printStackTrace(); 
			
		}
		
		// 연결된 Connection 객체를 메소드 호출부로 반환
		return conn; 
	}
		
	// 2. Commit 메소드
	public static void commit(Connection conn) {	
	
		try {
			
			// 열려있는지 확인
			if( conn != null && !conn.isClosed() ) conn.commit();
			
		} catch (Exception e) {
			System.out.println("Commit 중 예외 발생");
			e.printStackTrace();
		}
		
	}
	
	// 3. Rollback 메소드
	public static void rollback(Connection conn) {
		
		try {
			
			// 열려있는지 확인
			if( conn != null && !conn.isClosed() ) conn.rollback();
			
		} catch (Exception e) {
			System.out.println("Rollback 중 예외 발생");
			e.printStackTrace();
		}
		
	}
		
	// 4. 반환 메소드 - conn
	public static void close(Connection conn) {
		
		try {
			
			if( conn != null && !conn.isClosed() ) conn.close();
			
		} catch (Exception e) {
			System.out.println("Connection close 중 예외 발생");
			e.printStackTrace();
		}
		
	}
	
	// 5. 반환 메소드 - stmt
	public static void close(Statement stmt) {
		
		try {
			
			if( stmt != null && !stmt.isClosed() ) stmt.close();
			
		} catch (Exception e) {
			System.out.println("Statement close 중 예외 발생");
			e.printStackTrace();
		}
	}
	
	// 6. 반환 메소드 - rs
	public static void close(ResultSet rs) {
		
		try {
			
			if( rs != null && !rs.isClosed()) rs.close();
			
			
		} catch (Exception e) {
			System.out.println("ResultSet close 중 예외 발생");
			e.printStackTrace();
		}
	}


}
