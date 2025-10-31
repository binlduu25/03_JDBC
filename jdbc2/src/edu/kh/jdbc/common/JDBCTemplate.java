package edu.kh.jdbc.common;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

// 9. JDBCTemplate Class 작성
// JDBCTemplate 은 JDBC 관련 작업을 위한 코드를 미리 작성하여 제공하는 클래스

// 중복되는 코드들
 // - CONNECTION 생성
 // - AUTO COMMIT 설정
 // - COMMIT / ROLLBACK 등
 // - 사용 자원 반환 코드(CLOSE) 등

 // 따라서 위와 같이 반복되는 코드들을 TEMPLATE 클래스에 작성하여 재사용하기 위한 목적

/* *************** 중요한 점 *********************************************************
 해당 클래스를 어디서든 접근이 가능하도록 공유 가능한 클래스로 작성
  - static(공유) : 객체 생성 없이 필드, 메소드 접근 
  - ex) Math.random() 사용할 때 new 연산자를 사용해 따로 객체 생성할 필요 없는 것처럼
  
  > 모든 메소드를 public static 으로 선언 예정
 *************************************************************************************/


public class JDBCTemplate {
	
	// 필드
	private static Connection conn = null;
	
	// 메소드
	
	/* 9-1. getConnection 메소드 작성
	 * 
	 * 호출 시 Connection 객체를 생성하여 호출한 곳으로 반환하는 메소드
	 * 또한 Auto Commit off 기능 제공 
	 * @return conn
	 */
	public static Connection/*반환형*/ getConnection() {
		
		try {
			
			// 9-2. 이미 만들어진 Connection 을 계속해서 다시 만들어선 안 되기에, 아래 조건을 먼저 확인한다.
			
			// 이전에 Connection 객체가 만들어졌고, 아직 close 된 상태가 아니라면 기존의 Connection 반환
			if( conn != null && !conn.isClosed() ) return conn;
			
			// 9-3. Properties 객체 생성 (LoadXMLFile 클래스 참고)
			Properties prop = new Properties(); 
			
			// 9-4. properties 가 제공하는 메소드 이용해 driver.xmㅣ 파일 읽어오기
			 // driver.xml 을 inputStream 통해 읽어들여 prop 이라는 Properties 객체에 저장
			prop.loadFromXML(new FileInputStream("driver.xml"));
			
			// 9-5. prop 에 저장된 값을 이용해서 Connection 객체 생성
			Class.forName(prop.getProperty("driver"));
			
			conn = DriverManager.getConnection(prop.getProperty("url"),
											   prop.getProperty("userName"),
											   prop.getProperty("userPass"));
			
			// 9-6. 만들어진 Connection 에서 Auto Commit 끄기
			conn.setAutoCommit(false);
			
		} catch (Exception e) {
			
			System.out.println("커넥션 생성 중 예외 발생 : JDBCTemplate 내 getConnection()");
			e.printStackTrace();
			
		}
		
		return conn;
	}
	
	/* 9-7. commit 메소드 작성
	 * 
	 * 전달받은 Connection 에서 수행한 SQL 을 COMMIT 하는 메소드
	 * 
	 */
	public static void commit(Connection conn) {	
	
		try {
			
			if( conn != null && !conn.isClosed() ) conn.commit();
			
		} catch (Exception e) {
			System.out.println("커밋 중 예외 발생");
			e.printStackTrace();
		}
		
	}
	
	/** 9-8. rollback 메소드 작성
	 * 
	 *  전달 받은 커넥션에서 수행한 SQL 을 ROLLBACK 하는 메소드
	 * @param conn
	 */
	public static void rollback(Connection conn) {
		
		try {
			
			if( conn != null && !conn.isClosed() ) conn.rollback();
			
		} catch (Exception e) {
			System.out.println("롤백 중 예외 발생");
			e.printStackTrace();
		}
		
	}
	
	// 9-9. 자원 반환 메소드 작성
	 // Connection, Statement(PreparedStatement), ResultSet
	
	/** 9-9-1. 
	 * 전달받은 커넥션을 close(자원 반환) 하는 메소드
	 * 
	 */
	public static void close(Connection conn) {
		
		try {
			
			if( conn != null && !conn.isClosed() ) conn.close();
			
		} catch (Exception e) {
			System.out.println("커넥션 close 중 예외 발생");
			e.printStackTrace();
		}
		
	}
	
	/** 9-9-2. 
	 * 
	 * 전달받은 Statement, PreparedStatement 둘 다 close 하는 메소드
	 * PreparedStatement 는 Statement 의 자식 클래스이므로
	 * 매개변수 타입(아래 메소드명의 Statement)에 부모클래스로 작성을 하면
	 * 부모타입은 물론 자식타입 역시 들어올 수 있음
	 * (다형성 : UPCASTING)
	 * 
	 * @param stmt
	 */
	public static void close(Statement stmt) {
		
		try {
			
			if( stmt != null && !stmt.isClosed() ) stmt.close();
			
		} catch (Exception e) {
			System.out.println("Statement close() 중 오류 발생");
			e.printStackTrace();
		}
	}
	
	
	/** 매개변수로 전달받은 ReslutSet 을 close 하는 메소드
	 * @param rs
	 */
	public static void close(ResultSet rs) {
		
		try {
			
			if( rs != null && !rs.isClosed()) rs.close();
			
			
		} catch (Exception e) {
			System.out.println("ResultSet close 중 예외 발생");
			e.printStackTrace();
		}
	}
	

}
