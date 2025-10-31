package jdbc1.edu.kh.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class JDBCExample2 {
	
	public static void main(String[] args) {
		
		// 입력 받은 급여보다 초과해서 받는 사원의 사번, 이름, 급여 조회
		
		// 1. JDBC 객체 참조용 변수 선언
		
			Connection conn = null; // DB 연결 정보 저장 객체
			Statement stmt = null; // SQL 수행, 결과 반환용 객체
			ResultSet rs = null; // SELECT 수행 결과 저장용 객체
			
			Scanner sc = null; // 키보드 입력용 객체
			
		try {
		
		// 2. DriverManager 객체 이용해서 Connection 객체 생성
			
			// 2-1. Oracle JDBC Driver 객체 메모리 로드
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2-2. DB 연결 정보 작성
			String type = "jdbc:oracle:thin:@"; 
			String host = "localhost"; 
			String port = ":1521"; 
			String dbName = ":XE"; 
			String userName ="c##sjh"; 
			String password = "sjh1234"; 
			
			// 2-3. DB 연결 정보와 DriverManager 이용해서 Connection 객체 생성
			conn = DriverManager.getConnection( type + host + port + dbName, userName, password );
			
			// 2-4. Connection 객체 잘 생성되었는지 확인
			System.out.println(conn);
			
		// 3. SQL 작성
			
			// 입력 받을 스캐너 객체 생성 필요
			sc = new Scanner(System.in);
			
			System.out.print("급여 입력 : ");
			int input = sc.nextInt();
			
			// EMPLOYEE 테이블에서 사번, 이름, 부서코드, 직급코드, 급여, 입사일 조회
			String sql = "SELECT EMP_ID, EMP_NAME, DEPT_CODE, JOB_CODE, SALARY, HIRE_DATE FROM EMPLOYEE"
						+" WHERE SALARY > " + input;
				
		// 4. Statement 객체 생성					
			stmt = conn.createStatement();
					
		// 5. Statement 객체 이용해 SQL 수행 후 결과를 반환 받기
			rs = stmt.executeQuery(sql);
		
		// 6. 조회 결과가 담긴 rs 를 커서(cursor)를 이용하여 '1행 씩' 접근하여 각 행에 작성된 컬럼 얻어오기
			
			while(rs.next()) {
				
				// while 문 안에서 각 컬럼값을 뽑아오는 역할을 수행해야 한다.
				 // ResultSet.get자료형(컬럼명 | 순서)
				
				String empId = rs.getString("EMP_ID");
				String empName = rs.getString("EMP_NAME");
				String deptCode = rs.getString("DEPT_CODE");
				String jobCode = rs.getString("JOB_CODE");
				int salary = rs.getInt("SALARY");
				Date hireDate = rs.getDate("HIRE_DATE"); // DATE 타입 IMPORT 필요 
				
				System.out.printf("사번 : %s / 이름 : %s / 부서코드 : %s / 직급코드 : %s / 급여 : %d / 입사일 : %s \n"
								  ,empId, empName, deptCode, jobCode, salary, hireDate.toString() );
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		
		} finally {	
			
			try {
				
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
				if (sc != null) sc.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
			
		
	}

}
