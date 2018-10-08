import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Array;
import java.util.Arrays;
import java.util.Scanner;
import java.awt.event.MouseAdapter;
import javax.swing.*;

public class MajorReqs {

	public static GetReqs courses;
	public static boolean[] clicks = { true, false, true, false, false, true };
	public static CreateCourses majorCourses;
	public static String major; 
	public static String majorCode;
	
	public static void main(String[] args) throws IOException {
		Scanner console = new Scanner(System.in);
		
		System.out.println("What are you majoring in?");
		major = console.nextLine().toLowerCase();
		
		System.out.println("What is the major code?");
		majorCode = console.nextLine().toUpperCase();
		
		courses = new GetReqs(major);
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				gui();
			}
		});

	}

	public static void gui() {
		// setting the frame
		JFrame frame = new JFrame("Test");
		frame.setSize(1000, 1000);
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// code for the panel
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
		panel.setBackground(Color.WHITE);
		JLabel label = new JLabel("<html>Which of the classes have you taken for " + " your major?</html>");
		label.setFont(new Font("Garamond", 17, 17));
		label.setHorizontalAlignment(SwingConstants.CENTER);

		// making the buttons
		courses.getCourses();

		JButton[] buttons = new JButton[courses.reqs.length()];
		
		// making a boolean that will keep track of user input
		clicks = new boolean[courses.reqs.length()];
		for (int i = 0; i < clicks.length; i++) { clicks[i] = false;}

		for (int i = 0; i < courses.reqs.length(); i++) {
			buttons[i] = new JButton(courses.getAReq(i, courses.getCourses()));
			buttons[i].setForeground(Color.WHITE);
		}

		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1;
		c.weighty = 1;

		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(label, c);
		
		// coding for the button that will change color when your mouse is on top of a selection
		// and when you click on the actual button
		for (int k = 0; k < courses.reqs.length(); k++) {
			final int j = k;
			buttons[k].setBackground(new Color(115, 140, 201));
			c.gridy = k + 1;
			c.insets = new Insets(5, 150, 5, 150);
			panel.add(buttons[k], c);
			buttons[k].addMouseListener(new MouseAdapter() {
				
				// will be light blue when the mouse hovers over an option
				public void mouseEntered(MouseEvent evt) {
					JButton button = (JButton) evt.getSource();
					if (!clicks[j])
						button.setBackground(new Color(14, 77, 146));
				}
				
				// will be somewhat dark button as default
				public void mouseExited(MouseEvent evt) {
					JButton button = (JButton) evt.getSource();
					if (!clicks[j])
						button.setBackground(new Color(115, 140, 201));
				}
				
				// will turn dark blue when the mouse clicks on the button
				public void mouseClicked(MouseEvent evt) {
					JButton button = (JButton) evt.getSource();
					clicks[j] = !clicks[j];
					if (clicks[j]) {
						button.setBackground(new Color(30, 50, 97));
					}
					else {
						button.setBackground(new Color(115, 140, 201));
						mouseEntered(evt);
					}
				}
			});
		}
		
		// making the next button
		JButton next = new JButton("NEXT");
		next.setBackground(Color.WHITE);
		c.gridy = courses.reqs.length() + 1;
		panel.add(next, c);
		next.addMouseListener(new MouseAdapter() {

			private boolean clicked2 = false;
		
			// when mouse is on top of the next button, it will be gray
			public void mouseEntered(MouseEvent evt) {
				JButton button = (JButton) evt.getSource();
				if (!clicked2) {
					button.setBackground(Color.GRAY);
				}
			}
		
			// will be white
			public void mouseExited(MouseEvent evt) {
				JButton button = (JButton) evt.getSource();
				if (!clicked2) {
					button.setBackground(Color.WHITE);
				}
			}
		
			// will open up to a new window when the next button is clicked
			public void mouseClicked(MouseEvent evt) {
				JButton button = (JButton) evt.getSource();
				clicked2 = !clicked2;
				if (clicked2) {
					frame.dispose();
					try {
						guiShowReqs();
					} catch (IOException e) {
						e.printStackTrace();
						System.out.println("ERROR");
					}
				}
			}
		
		});

	frame.getContentPane().add(panel);

	// Display the window.
	frame.setVisible(true);

	}

	public static void guiShowReqs() throws IOException {
		// setting the frame
		JFrame frame = new JFrame("Test");		
		frame.setSize(1800, 1000);
		frame.getContentPane().setBackground(Color.WHITE);
		frame.getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints d = new GridBagConstraints();
		d.weightx = 1;
		d.weighty = 1;
		d.gridx = 0;
		d.fill = GridBagConstraints.BOTH;
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// creates header for new window listing requirements that need to be fulfilled
		JPanel header = new JPanel();
		header.setLayout(new GridBagLayout());
		GridBagConstraints x = new GridBagConstraints();
		x.weightx = 1;
		x.weighty = 0.0;
		x.gridx = 0; x.gridy = 0;
		x.fill = GridBagConstraints.BOTH;
		x.ipady = 40;
		JLabel headerLabel = new JLabel("<html>These are the courses you have left to take to fulfill all requirements. "
				+ "You can take the following classes this semester: <html>");
		headerLabel.setFont(new Font("Garamond", 20, 20));
		header.add(headerLabel, x);
		frame.add(header, x);
		
		// calling the CreateCourses class using ECON major code
		majorCourses = new CreateCourses(majorCode);
		majorCourses.apiCall();
		
		// making one huge JPanel to put on all the course reqs information
		JPanel panel = new JPanel();
		
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1;
		c.weighty = 1;
		
		panel.setBackground(Color.WHITE);
		
		// making some variables that will keep track of where the text should be 
		// in the y direction
		int count1 = 0;
		int count2 = 0;
		
		// making a variable that will keep count of the number of classes
		int classNumber = 0;
		
		// putting each requirement that needs to be fulfilled in this single huge panel
		for (int i = 0; i < clicks.length; i++) {
			
			// do we still need this line of code?
			d.gridy = i + 1;
			//
			
			System.out.println(i);
			if (!clicks[i]) {
				// setting the grid for the panel`
				c.fill = GridBagConstraints.BOTH;
				c.gridx = 1;
				
				// look and see what classes correspond to that requirement
				String courseRequirements = courses.getAReq(i, courses.getCourses());
				String[] oneCourse = courseRequirements.split("AND|OR");
				
//				System.out.println(oneCourse.length+"!");
				
				// making the JLabel 
				JLabel[] courseLabels = new JLabel[oneCourse.length]; 
				
				// a for loop that goes through each req and print out the info for all the classes for that req
				for (int j = 0; j < oneCourse.length; j++) {
//					c.gridy++;
					classNumber++;
					oneCourse[j] = oneCourse[j].trim();
					
					// calling the CreateCourses class
					if (!oneCourse[j].substring(0, 4).equals(majorCode)) {
						majorCourses = new CreateCourses(oneCourse[j].substring(0, 4));
						majorCourses.apiCall();
					}

					courseLabels[j] = new JLabel("<html>"+ majorCourses.courseNumber(majorCourses.findCourse(oneCourse[j])) + "<html>" + "\n" 
							+ "<html>" + " " + majorCourses.courseTitle(majorCourses.findCourse(oneCourse[j])) + "<html>" 
							+ "<html>" + " " + majorCourses.courseProf(majorCourses.findCourse(oneCourse[j])) + "<html>" + "<br>"
							+ "<html>" + majorCourses.courseDescription(majorCourses.findCourse(oneCourse[j])) + "<html>" 
							+ "<html>" + majorCourses.courseTime(majorCourses.findCourse(oneCourse[j])) + "<html>");
					
					courseLabels[j].setFont(new Font("Garamond", 17, 17));
					
					// offsetting the grid correctly for each class req information
					if (i % 2 == 0) {
						count1++;
						c.gridy = count1;
						c.gridx = 0;
						
						// changing the font color for every other one to be red
						if (count1 % 2 == 0) {
							courseLabels[j].setForeground(Color.RED);
						}
						else {
							courseLabels[j].setForeground(Color.BLACK);
						}
						
						panel.add(courseLabels[j], c);
					}
					else {
						count2++;
						c.gridy = count2;
						c.gridx = 1;
						
						// changing the font color for every other one to be red
						if (count2 % 2 == 0) {
							courseLabels[j].setForeground(Color.RED);
						}
						else {
							courseLabels[j].setForeground(Color.BLACK);
						}
						
						panel.add(courseLabels[j], c);
					}
					System.out.print(" " + j);
					
					if (!oneCourse[j].substring(0, 4).equals(majorCode)) {
						majorCourses = new CreateCourses(majorCode);
						majorCourses.apiCall();
					}
					
				}
			}
		}
		panel.setSize(1800, 1000);
		frame.getContentPane().add(panel, d);
		
		// Display the window.
		frame.setVisible(true);
	}
}