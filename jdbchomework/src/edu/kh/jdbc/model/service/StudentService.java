package edu.kh.jdbc.model.service;

import java.sql.Connection;
import java.util.List;

import edu.kh.jdbc.common.JDBCTemplateH;
import edu.kh.jdbc.model.dao.StudentDAO;
import edu.kh.jdbc.model.dto.Student;

public class StudentService {
	
	StudentDAO dao = new StudentDAO();
		
	public List<String> condition(String inputName) throws Exception{
			
		Connection conn = JDBCTemplateH.getConnection();
		
		String gender = dao.findGender(conn, inputName);
		
		int randMajor = (int)(Math.random() * 4) + 1;
		String major = null;
		
		switch(randMajor) {
		case 1: major = "경영학"; break;
		case 2: major = "심리학"; break;
		case 3: major = "물리학"; break;
		case 4: major = "국문학"; break;
		}
		
		int randClub = (int)(Math.random() * 4) + 1;
		String club = null;
		
		switch(randClub) {
		case 1: club = "농구부"; break;
		case 2: club = "사진부"; break;
		case 3: club = "밴드부"; break;
		case 4: club = "제빵부"; break;
		}
		
		int age = (int)(Math.random() * 2) + 1;
		int score = (int)(Math.random() * 2) + 1;
		
		List<String> matchedStudentList = dao.selectStdMatched(conn, gender, age, score, club, major, inputName);
		
		return matchedStudentList;
			
	}

	public int insertStd(String inputName) throws Exception{
		
		Connection conn = JDBCTemplateH.getConnection();
		
		int age = (int)(Math.random() * 10) + 20;
		
		int score = (int)(Math.random() * 101);		
		
		int randMajor = (int)(Math.random() * 4) + 1;
		String major = null;
		switch(randMajor) {
		case 1: major = "경영학"; break;
		case 2: major = "심리학"; break;
		case 3: major = "물리학"; break;
		case 4: major = "국문학"; break;
		}
		
		int randClub = (int)(Math.random() * 4) + 1;
		String club = null;
		switch(randClub) {
		case 1: club = "농구부"; break;
		case 2: club = "사진부"; break;
		case 3: club = "밴드부"; break;
		case 4: club = "제빵부"; break;
		}
		
		int randGender = (int)(Math.random() * 2) + 1;
		String gender = null;
		switch(randGender) {
		case 1: gender = "남"; break;
		case 2: gender = "여"; break;
		}
		
		int result = dao.insertStd(conn, inputName, age, major, score, gender, club);
		
		if(result > 0) {
			JDBCTemplateH.commit(conn);
		}else {
			JDBCTemplateH.rollback(conn);
		}
		
		JDBCTemplateH.close(conn);
		
		return result;
	}

	public List<Student> selectStd() throws Exception{
		
		Connection conn = JDBCTemplateH.getConnection();
		
		List<Student> selectStd = dao.selectStd(conn);
		
		JDBCTemplateH.close(conn);
		
		return selectStd;
	}

	
	
	
	
	
}
	

