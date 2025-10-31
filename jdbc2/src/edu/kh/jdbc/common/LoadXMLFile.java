package edu.kh.jdbc.common;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class LoadXMLFile {
	
	// 8. 작성된 xml 파일 load 하는 연습용 클래스 (1회용)
	 // 해당 클래스는 git 에 올라가도 됨

	public static void main(String[] args) {
		
		// 8-1. FileInputStream , Properties 활용

		// 8-2. XML 파일 읽기 위한 fis 객체 및 db 연결 위한 Connection 생성
		FileInputStream fis = null;
		Connection conn = null;
		
		try {
			
			// 8-3. properties 객체 생성
			Properties prop = new Properties();
			
			// 8-4. driver.xml 파일 읽기 위한 InputStream 객체 생성
			fis = new FileInputStream("driver.xml"); // 당 프로젝트 폴더 내 생성되어 있는 driver.xml 파일 읽어오기			
			
			// 8-5. 연결된 driver.xml 파일에 있는 내용을 모두 읽어와 Properties 객체에 K:V 형식으로 저장
			 // Properties.getProperty() 메소드 사용
			 // 속성값 얻어오기 위해서는 fis 를 이용하여 xml 파일을 로드부터 해야 한다.
			
			// 8-6. xml 파일에 있는 내용을 읽어와 Properties 객체 안에 저장함
			prop.loadFromXML(fis); 
			
			// 8-7. Properties.getProperty() : key 가 일치하는 속성값(value)를 얻어오는 메소드
			String driver = prop.getProperty("driver"); // driver 라는 키와 매칭되는 value 값을 driver 변수에 저장
			String url = prop.getProperty("url");
			String userName = prop.getProperty("userName");
			String userPass = prop.getProperty("userPass");
			
			// 8-8. 드라이버 메모리 탑재
			Class.forName(driver);
			
			// 8-9. DB 연결
			conn = DriverManager.getConnection(url, userName, userPass);
			
			// 8-10. 연결 확인
			System.out.println(conn);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			
			try {
				
				if(conn != null) conn.close();
				if(fis != null) fis.close();
				
			} catch (Exception e) {
				e.printStackTrace();
				}
		}
		
		
		
		
	}
	
}
