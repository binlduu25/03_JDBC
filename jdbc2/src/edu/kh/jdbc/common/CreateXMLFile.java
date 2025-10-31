package edu.kh.jdbc.common;

import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Scanner;

public class CreateXMLFile { 
	
	// 6. XML 파일 생성용 클래스 작성
	
	public static void main(String[] args) {
		
		// XML(eXtensible Markup Language) : 확장 가능한 마크업 언어
		// - 단순화된 데이터 기술 형식
		// git 에 올리면 안 되는 데이터를 저장하기 위함
		
		// 즉, DB 에 연결하기 위한 URL, ID, PW 등이 github 에 올라가는 것을 방지
		// XML 에 저장되는 데이터 형식은 KEY : VALUE 이다. (컬렉션 파트 MAP 참고)
		 // -> KEY 와 VALUE 모두 String 형식
		
		FileOutputStream fos = null;
		Scanner sc = null;
		
		// 6-1. XML 파일을 읽고 쓰기 위한 IO 관련된 클래스가 필요하다.
		
		try {
			
			// 6-2. 파일 이름 입력 받기
			sc = new Scanner(System.in);
			System.out.print("생성할 파일 입력 : ");
			String fileName = sc.next();
			
			// 6-3. Properties 객체 생성
			
			// 컬렉션 부분 map 참고
			// Properties : - 키와 값을 String 타입으로 제한한 Map 의 후손 클래스(컬렉션)
			// 				- 주로  *.properties 파일을 읽어들일 때 사용 
			//				- 다만 지금은 properties 대신 xml 파일로 만든다.
			
			// properties 객체를 이용하는 이유 : 
			 // key value 가 String 타입으로 되어 있는 파일을 읽어들일 때 용이하다.(해당 기능에 특화된 메소드 제공)
			
			Properties prop = new Properties();
			
			// 6-4. 파일 생성
			// 6-4-1. FileOutputStream
			fos = new FileOutputStream(fileName + ".xml"); // <- 경로를 지정해주지 않았기 때문에 현재 프로젝트 경로 내 생성
			
			// 6-4-2. Properties 객체 이용하여 XML 파일 생성
			 // properties.storeToXML()
			 // : 객체의 데이터를 XML 형식으로 파일을 저장하는 메소드
			 // KEY = VALUE 형태로 저장된 설정값을 XML 파일 형태로 내보냄
			 // 매개인자 : (저장할 XML 파일의 출력스트림, XML 파일 상단에 주석으로 기록될 설명)
			prop.storeToXML(fos, fileName + ".xml 파일!"); 
			
			// 6-4-3. 파일 생성 완료 메시지 출력
			System.out.println(fileName + ".xml 파일 생성 완료");
			
			// 6-5. 메소드 실행 후 파일 이름 driver 로 입력하여 driver.xml 파일 생성
			 // jdbc2 프로젝트 하위 파일로 생성됨
			 // 이하 주석은 driver.xml 파일에서 계속됨
			
		} catch (Exception e) {
			System.out.println("XML 파일 생성 중 예외 발생");
			e.printStackTrace();
		} finally {
			
			try {
				
				if(fos != null) fos.close();
				if(sc != null) sc.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		
		
	}

}
