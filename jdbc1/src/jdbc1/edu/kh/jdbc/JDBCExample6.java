package jdbc1.edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class JDBCExample6 {
	
	public static void main(String[] args) {
		
		// 아이디, 비밀번호, 이름을 입력받아
		// 아이디, 비밀번호가 일치하는 사용자의
		// 이름을 수정.(UPDATE)
		// 성공 시 "수정 성공!" 출력 / 실패 시 "아이디 또는 비밀번호 불일치" 출력
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		Scanner sc = null; 
		
		try {
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			String url = "jdbc:oracle:thin:@localhost:1521:XE";
			String userName = "c##sjh";
			String userPass = "sjh1234";
			
			conn = DriverManager.getConnection(url, userName, userPass);
			
			System.out.println(conn);
			
			conn.setAutoCommit(false); // 이 지점에서 db auto commit off 
			
			sc = new Scanner(System.in);
			
			System.out.print("아이디 입력 : ");
			String id = sc.next();
			
			System.out.print("비밀번호 입력 : ");
			String pw = sc.next();
			
			System.out.print("수정할 이름 : ");
			String name = sc.next();
			
			String sql = """
						 UPDATE TB_USER
						 SET USER_NAME = ?
						 WHERE USER_ID = ?
						 AND USER_PW = ?	
						 """;
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, name);
			pstmt.setString(2, id);
			pstmt.setString(3, pw);
			
			int result = pstmt.executeUpdate();
			
			if(result > 0) {
				
				conn.commit();
				System.out.println("변경 완료");
				
			} else {
				
				conn.rollback();
				System.out.println("변경 실패");
			}
			
		} catch (Exception e) {

			e.printStackTrace();
		
		} finally {
			
			try {
				
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
				if(sc != null) sc.close(); 
				
			} catch (Exception e) {
		
				e.printStackTrace();
			}
			
		}
		
		
		
	}

}
