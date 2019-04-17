package InternshipCoera;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import org.javatuples.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Main {

	public static void writeToJSON(String msg, int noOfSuspensions, ArrayList<Pair<Integer, Integer>> details) {

		JSONObject jo = new JSONObject();
		Map m = new LinkedHashMap(2);
		jo.put("output", m);
		m.put("error message", msg);

		JSONArray ja = new JSONArray();

		for (int i = 0; i < noOfSuspensions; i++) {
			m = new LinkedHashMap(2);
			m.put("year", details.get(i).getValue0());
			m.put("holidayDays", details.get(i).getValue1());
			ja.add(m);
		}

		jo.put("holidayRightsPerYearList", ja);

		// writing JSON to file : output.json
		PrintWriter pw;
		try {
			pw = new PrintWriter("output.json");
			pw.append(jo.toJSONString());

			pw.flush();
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void validateData(String checkDate) {
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = sdf.parse(checkDate);
			if (!checkDate.equals(sdf.format(date))) {
		        date = null;
		    }
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    if (date == null) {
	    	// Invalid date format
	    	JOptionPane.showMessageDialog(null, "Input not valid!");
	    }
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		JSONParser parser = new JSONParser();

		try {

			Object obj = parser.parse(new FileReader("input.json"));
			
			JSONObject jsonObject = (JSONObject) obj;
			System.out.println(jsonObject);
			// System.out.println(jsonObject.size());
			
			// if we would have many employee data items in the hashmap
			for (int k = 0; k < jsonObject.size(); k++) {

				JSONObject employeeData = (JSONObject) jsonObject.get("employeeData");
				String employmentStartDate = (String) employeeData.get("employmentStartDate");
				validateData(employmentStartDate);
				
				String employmentEndDate = (String) employeeData.get("employmentEndDate");
				validateData(employmentEndDate);
				
				JSONArray suspensionPeriodList = (JSONArray) employeeData.get("suspensionPeriodList");

				// iterating through the suspensionPeriodList
				Iterator itr1 = suspensionPeriodList.iterator();
				Iterator itr2;

				// put in a list the starting end ending dates for each suspension
				ArrayList<String> list = new ArrayList<String>();

				while (itr1.hasNext()) {
					itr2 = ((Map) itr1.next()).entrySet().iterator();
					while (itr2.hasNext()) {
						Map.Entry pair = (Entry) itr2.next();
						validateData((String) pair.getValue());
						
						list.add((String) pair.getValue());

					}
				}

				// each employee has an employment start date, end date and a list of suspension
				// periods
				// initially the holiday rights are 20
				Employee e = new Employee(employmentStartDate, employmentEndDate, 20);
				int startDateYear = e.getEmploymentStartDate().getYear();
				int endDateYear = e.getEmploymentEndDate().getYear();

				int holidayDaysLostBecauseSuspension;
				int holidayDays;

				// pair the year and the holiday rights for that year in tuples inside an list
				ArrayList<Pair<Integer, Integer>> details = new ArrayList<Pair<Integer, Integer>>();

				// go from the employement start year and employment end year
				// and increment with each year that passes the holidayRights
				// until they reach maximum 24 days
				for (int i = startDateYear; i <= endDateYear; i++) {

					for (int j = 0; j < list.size(); j = j + 2) {

						// get how many days are between the two dates
						int noOfDaysBetween = (int) ChronoUnit.DAYS.between(LocalDate.parse(list.get(j + 1)),
								LocalDate.parse(list.get(j)));

						// get the final no of days, by removing the non-working days, saturday/sunday
						// in the case, 8 days per month
						noOfDaysBetween = noOfDaysBetween - (int) ChronoUnit.MONTHS
								.between(LocalDate.parse(list.get(j + 1)), LocalDate.parse(list.get(j))) * 8;
						System.out.println("Day diff: " + noOfDaysBetween);

						// because there are 261 working days from 365 days in a year
						holidayDaysLostBecauseSuspension = (int) Math
								.ceil(noOfDaysBetween * e.getHolidayRights() / 261);
						System.out.println("Lost: " + holidayDaysLostBecauseSuspension);
						holidayDays = e.getHolidayRights() - holidayDaysLostBecauseSuspension;
						System.out.println("Final holiday days: " + holidayDays);

						details.add(new Pair(LocalDate.parse(list.get(j)).getYear(), holidayDays));

						writeToJSON("none", list.size() / 2, details);

					}

					e.increaseHolidayRightsWithYear();
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();

		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}

	}
}
