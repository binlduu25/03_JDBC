package jdbc1.edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class JDBCExample4 {

	public static void main(String[] args) {

		// 부서명 입력 받아 해당 부서에 근무하는 사원의 사번, 이름, 부서명, 직급명 조회
		// 직급코드 오름차순

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		Scanner sc = null;

		try {
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			String url = "jdbc:oracle:thin:@localhost:1521:XE"; // DB 접속 정보 작성 
			String userName ="c##sjh"; 
			String password = "sjh1234"; 
			
			conn = DriverManager.getConnection(url, userName, password);
			
			System.out.println(conn);
			
			sc = new Scanner(System.in);
			System.out.print("부서명 : ");
			String input = sc.nextLine();
			
			 String sql = """
						 SELECT EMP_ID, EMP_NAME, DEPT_TITLE, JOB_NAME
						 FROM EMPLOYEE
						 JOIN JOB USING (JOB_CODE)
						 LEFT OUTER JOIN DEPARTMENT ON (DEPT_CODE = DEPT_ID)
						 WHERE DEPT_TITLE = 
						 """ +"'"+ input +"'"+ 
						 " ORDER BY JOB_NAME ASC";
			
			// String sql = """
			//		SELECT EMP_ID, EMP_NAME, DEPT_TITLE, JOB_NAME
			//		FROM EMPLOYEE
			//		JOIN JOB ON(EMPLOYEE.JOB_CODE = JOB.JOB_CODE)
			//		LEFT JOIN DEPARTMENT ON(DEPT_CODE = DEPT_ID)
			//		WHERE DEPT_TITLE = '""" + input + "' ORDER BY EMPLOYEE.JOB_CODE";
			
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			// 일치하는 부서 없을 시 문구 출력해야 하기에
			
			/*
			// 방법 1) flag 이용
			
			boolean flag = true;
		
			while(rs.next()) {
				
				flag = false; // while 문에 진입했다는 것은 rs.next() 의 값이 있다는 얘기이므로 flag 를 false 로 전환
				
				String empId = rs.getString("EMP_ID");
				String empName = rs.getString("EMP_NAME");
				String deptT = rs.getString("DEPT_TITLE");
				String jobName = rs.getString("JOB_NAME");
				
				System.out.printf("사번 : %s / 이름 : %s / 부서명 : %s / 직급명 : %s \n", empId, empName, deptT, jobName);
			
			}
			
			if (flag) System.out.println("일치하는 부서가 없습니다!");
			*/
			
			// 방법 2) return 사용법
			
			if(!rs.next()) { 
			// if 문이 false일 시에도 (= 조회되는 행이 하나도 없을 시에도)  
			// if 문을 타서 코드를 수행해야 하기에 조건절에 !rs.next() 로 false 값 설정
				System.out.println("일치하는 부서가 없습니다!");
				return; // 메소드 종료. 여기서는 main 메소드				
			}
			
			// 중요! 위 if 문을 통해 이미 rs.next() 메소드 특성 상 1행이 이미 지나친 상태
			 // (1번째 행에서 2번째 행으로 커서가 넘어간 상태)
			// 즉, 이제부터 rs.next()를 통해 전체 행을 훑어야 하지만 아래부터 수행될 코드는 rs.next() 가 2번째 행부터 훑게 됨
			// 따라서 일반 while 문을 사용하게 되면 while 조건절에서 next() 를 바로 만나게 되어
			// 해당 행에 데이터가 있음에도 2번째 행부터 출력하게 되기 때문에 수정 필요
			 // - > 곧바로 while 문을 쓰는 것이 아니라 do while 문을 통해 1번째 행이 한번 수행되어야 한다!
			
			do{
				
				String empId = rs.getString("EMP_ID");
				String empName = rs.getString("EMP_NAME");
				String deptT = rs.getString("DEPT_TITLE");
				String jobName = rs.getString("JOB_NAME");
				
				System.out.printf("사번 : %s / 이름 : %s / 부서명 : %s / 직급명 : %s \n", empId, empName, deptT, jobName);
				
				// 위 do 를 통해 첫번째 행의 데이터를 담은 코드가 한번 수행되고
				// 아래 while 을 통해 while 의 조건절 rs.next() 가 false 가 될 때까지(더 이상 다음 행이 없을 때까지) 코드를 반복수행하게 됨
			
			} while(rs.next());
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {

				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();
				if (sc != null)
					sc.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

}
