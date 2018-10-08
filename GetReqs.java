import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Array;

import org.json.*;

import java.util.Arrays;
import java.util.Scanner;

public class GetReqs {

	private String fileName;
	private String fileName1;
	private String str;
	private Scanner scan;
	private JSONObject major;
	public JSONArray reqs;
	public String[] reqClasses;
	public JSONArray req;

	public GetReqs(String major) throws FileNotFoundException {
		fileName1 = major;
		fileName = major + ".txt";
		scan = new Scanner(new File(fileName));
		str = "";
	}
	
	// it will get all the course requirements 
	public String[] getCourses() {

		while (scan.hasNextLine()) {
			str += scan.nextLine();
		}

		major = new JSONObject(str);
		reqs = major.getJSONArray(fileName1);

		JSONArray[] req = new JSONArray[reqs.length()];

		reqClasses = new String[reqs.length()];
		for (int i = 0; i < reqs.length(); i++) {
			req[i] = reqs.getJSONArray(i);
			reqClasses[i] = "";
			for (int j = 0; j < req[i].length(); j++) {
				reqClasses[i] += req[i].getString(j) + " ";
			}
			reqClasses[i].trim();
		}

		return reqClasses;
	}
	
	public String getAReq(int reqNumber, String[] reqClasses) {
		return reqClasses[reqNumber];
	}
	
	// testing if this class will work - it should output the math reqs for economics major
	public static void main (String [] args) throws FileNotFoundException {
		GetReqs want = new GetReqs("economics");
//		GetReqs want = new GetReqs();
		System.out.println((want.getAReq(0, want.getCourses())));
	}

}

