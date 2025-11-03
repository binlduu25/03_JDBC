package edu.kh.jdbc.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Student {

	private int stdNo;
	private String stdName;
	private int stdAge;
	private String stdMajor;
	private int stdScore;
	private String stdGender;
	private String stdClub;
	private String stdEntDate;
	
	
	@Override
	public String toString() {
		return stdNo + " / " + stdName + " / " + stdAge + " / " + 
			   stdMajor + " / " + stdScore + " / " + stdGender + " / " + 
			   stdClub + " / " + stdEntDate;
	}
	
	
	
}
