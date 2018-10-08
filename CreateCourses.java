import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class CreateCourses {
	
	private JSONArray everything;
	private Scanner scanCourses;
	private String url = "https://gw.its.yale.edu/soa-gateway/course/webservice/index?apiKey="
			+ "l7xx1b0222309c83441aaea3ce70b45b875d&subjectCode=";
	public String className;
	
	public CreateCourses(String major) { 
		url = url + major;
		className = "";
	}
	
	public void apiCall() throws IOException {
		URL urlCourses = new URL(url);
		scanCourses = new Scanner(urlCourses.openStream());
		String stringEverything = scanCourses.nextLine();
		everything = new JSONArray(stringEverything);
	}
	
	public JSONObject findCourse(String course) {
		JSONObject specificCourse = new JSONObject();
		for (int i = 0; i < everything.length(); i ++) {
			specificCourse = everything.getJSONObject(i);
			if (specificCourse.getString("subjectNumber").equals(course)) {
				break;
			}
			else {
				className = course;
				specificCourse = new JSONObject();
			}
		}
		return specificCourse;
//		System.out.println(specificCourse.get("subjectNumber"));
//		System.out.println(course);

	}
	
	// check to see if the course exists this semester
	public boolean isAvailable(JSONObject course){
		return course.length() > 0;
	}
	
	// use API to get course number
	public String courseNumber(JSONObject course) {
		if (isAvailable(course)) {
			return course.getString("subjectNumber");
		}
		else {
			return "Since " + className + " is not offered this semester, "
					+ "we cannot provide any information.";
		}
	}
	
	// use API to get course description 
	public String courseDescription(JSONObject course) {
		if (isAvailable(course)) {
			return "DESCRIPTION: " + course.getString("description");
		}
		else {
			return "";
		}
	}
	
	// use API to get course time 
	public String courseTime(JSONObject course) {
		if (isAvailable(course)) {
			JSONArray meeting = course.getJSONArray("meetingPattern");
			String timeMeeting = meeting.toString();
			timeMeeting = timeMeeting.replaceAll("[\\[\\]\"|]","");
			return "MEETING TIME: " + timeMeeting;
		}
		else {
			return "";
		}
	}
	
	// use API to get professor of the course 
	public String courseProf(JSONObject course) {
		if (isAvailable(course)) {
			JSONArray professor = course.getJSONArray("instructorList");
			String prof = professor.toString();
			prof = prof.replaceAll("[\\[\\]\"|]","");
			return "PROFESSOR: " + prof;
		}
		else {
			return "";
		}
	}
	
	// use API to get courseTitle
	public String courseTitle(JSONObject course) {
		if (isAvailable(course)) {
			return "TITLE: " + course.getString("courseTitle");
		}
		else {
			return "";
		}
	}
	
	// test to make sure this class works
	public static void main (String [] args) throws IOException {
		CreateCourses test = new CreateCourses("ECON");
		test.apiCall();
		System.out.println(test.courseTime(test.findCourse("ECON115")));
		System.out.println(test.courseProf(test.findCourse("ECON115")));
	}
}