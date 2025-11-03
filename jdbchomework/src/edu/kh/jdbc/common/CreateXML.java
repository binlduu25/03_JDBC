package edu.kh.jdbc.common;

import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Scanner;

public class CreateXML {
	
	public static void main(String[] args) {
		
		// 참조변수 선언
		FileOutputStream fos = null; // xml 파일 출력 위한 스트림
		Scanner sc = null; // 파일명 입력 위한 스캐너
		
		try { // try-catch(io 필수)
			
			// 파일 이름 입력받기
			sc = new Scanner(System.in);
			System.out.print("생성할 파일 입력(driver) : ");
			String input = sc.next();
			
			// 정보 저장할 Properties 객체 생성
			Properties prop = new Properties();
			
			// 출력용 fos 객체 생성, 경로미지정 -> 프로젝트 경로 내 생성
			fos = new FileOutputStream(input + ".xml"); 
			
			// properties 이용하여 output 스트림을 통한 후 xml 파일 생성
			prop.storeToXML(fos, "homework"); 
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally { // 자원 반환
			
			try { 
				
				if(fos != null) fos.close();
				if(sc != null) sc.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	
		
	}

}
