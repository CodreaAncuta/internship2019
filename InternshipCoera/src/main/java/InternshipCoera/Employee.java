package InternshipCoera;

import java.time.LocalDate;

public class Employee {

	private LocalDate employmentStartDate;
	private LocalDate employmentEndDate;
	private Integer holidayRights;
	
	public Employee(String startDate, String endDate, Integer holidayRights) {
		this.employmentStartDate = LocalDate.parse(startDate);
		this.employmentEndDate = LocalDate.parse(endDate);
		this.holidayRights = holidayRights;
	}
	
	public void increaseHolidayRightsWithYear() {
		
		if(holidayRights <= 24) {
				this.holidayRights++;
		}
		else System.out.println("The holiday rights already reached the maximum of 24 days!");
	}

	public LocalDate getEmploymentStartDate() {
		return employmentStartDate;
	}

	public void setEmploymentStartDate(LocalDate employmentStartDate) {
		this.employmentStartDate = employmentStartDate;
	}

	public LocalDate getEmploymentEndDate() {
		return employmentEndDate;
	}

	public void setEmploymentEndDate(LocalDate employmentEndDate) {
		this.employmentEndDate = employmentEndDate;
	}

	public Integer getHolidayRights() {
		return holidayRights;
	}

	public void setHolidayRights(Integer holidayRights) {
		this.holidayRights = holidayRights;
	}
	
	
}
