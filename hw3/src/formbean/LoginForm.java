package formbean;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
/**
 * Author : Jialu Chen
 * date: 11/27/2016
 * course: 08672
 */
public class LoginForm {
	private String emailAddress;
	private String password;
	private String button;

	
	public LoginForm(HttpServletRequest request) {
		emailAddress = request.getParameter("emailAddress");
		password = request.getParameter("password");
		button = request.getParameter("button");
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setEmailAddress(String s) {
		emailAddress = s.trim();
	}

	public void setPassword(String s) {
		password = s.trim();
	}
	
	public String getButton() {
		return button;
	}

	public boolean isPresent() {
		return button != null;
	}

	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();


		if (emailAddress == null || emailAddress.length() == 0) {
			errors.add("Email address is required");
		} else if (!isValidEmailAddress(emailAddress)) {
			errors.add("Email address is invalid");
		}

		if (password == null || password.length() == 0) {

			errors.add("Password is required");
		}
		if (button == null)
			errors.add("Button is required");


		if (errors.size() > 0) {
			return errors;
		}
		
		if (!button.equals("Login") && !button.equals("Register"))
			errors.add("Invalid button");

		return errors;
	}

	public boolean isValidEmailAddress(String email) {
		String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
		java.util.regex.Matcher m = p.matcher(email);
		return m.matches();
	}

}