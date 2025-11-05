package edu.kh.jdbc.view;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import edu.kh.jdbc.model.dto.Student;
import edu.kh.jdbc.model.service.StudentService;

public class StudentView {

	StudentService serv = new StudentService();
	Scanner sc = new Scanner(System.in);
	
	public void mainMenu() {
		
		int input = 0;
		
		do {
			
			try {
			
				System.out.println("*************************************");
				System.out.println("****** 학생 정보 조회 프로그램 ******");
				System.out.println("*************************************");
				System.out.println("1. 새로운 학생 등록");
				System.out.println("2. 모든 학생 정보 조회");
				System.out.println("3. 학생 정보 수정");
				System.out.println("4. 학생 정보 삭제");
				System.out.println("5. 전공별 학생 조회");
				System.out.println("6. 짝꿍 찾기");
				System.out.println("0. 프로그램 종료");
				System.out.print("메뉴 입력 : ");
				input = sc.nextInt();
				sc.nextLine();
		
				switch(input) {
				case 1 : insertStd(); break;
				case 2 : selectStd(); break;
				/*case 3 : updatetStd(); break;
				case 4 : deleteStd(); break;
				case 5 : selectStdMajor(); break;*/
				case 6 : matching(); break;
				case 0 : System.out.println("프로그램을 종료합니다."); break;
				}
		
			} catch (InputMismatchException e) {
				System.out.println("잘못된 입력입니다.");
				e.printStackTrace();
				input = -1;
				sc.nextLine();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} while(input != 0);
			
	}
	
	public void insertStd() throws Exception{
		
		while(true) {
			
		System.out.println("\n새로운 학생을 등록하세요.(이름만 입력)");
		String inputName = sc.next();
		
		int result = serv.insertStd(inputName);
		
		if(result > 0) {
			System.out.println(inputName + "의 정보가 추가되었습니니다.");
		} else {
			System.out.println("학생 정보 추가 실패");
		}
		
		System.out.println("\n더 추가하시겠습니까?(y/n)");
		String yn = sc.next().toUpperCase();
			
		if(yn.equals("N")) {System.out.println("메인 메뉴로 돌아갑니다.\n"); break;} 
		else {
			continue;
		}
		}
	}
	
	public void selectStd() throws Exception{
		
		System.out.println("\n전체 학생 정보를 조회합니다.");
		List<Student> selectStd = serv.selectStd();
	
		System.out.println("\n학번 / 이름 / 나이 / 전공 / 점수 / 성별 / 동아리 / 입학일");
		for(Student result : selectStd) {
			System.out.println(result);
		}
		
		
	}
	
	public void matching() throws Exception{ // 이름 목록 내에 있는지 조회하는 기능 추가 필요
		
		System.out.println("\n학생의 이름을 입력하여 마음에 둔 사람을 찾아보세요.");
		String inputName = sc.next();
		
		List<String> matchName = serv.condition(inputName);
		
		if(matchName.isEmpty()) {
			System.out.println(inputName + "에게 어울리는 짝꿍은 없습니다.\n");
			return;
		}
		System.out.println(inputName + "와/과 어울리는 짝꿍은 다음과 같습니다.\n");
		for(int i = 0; i < matchName.size(); i++) {
			System.out.println(matchName.get(i));		
		}
			
	}
	
}
