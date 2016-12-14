/**
 * Author :  Jialu Chen
 * Andrew ID: jialuc
 */

package formbeans;

import org.mybeans.form.FormBean;

import java.util.ArrayList;
import java.util.List;

public class ChangePwdForm extends FormBean {
	private String confirmPassword;
	private String newPassword;
	private String oldPassword;

	public String getConfirmPassword() { return confirmPassword; }
	public String getNewPassword()     { return newPassword;     }
	
	public void setConfirmPassword(String s) { confirmPassword = s.trim(); }
	public void setNewPassword(String s)     { newPassword     = s.trim(); }

	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();

		if (oldPassword == null || oldPassword.length() == 0) {
			errors.add("Current Password is required");
		}

		if (newPassword == null || newPassword.length() == 0) {
			errors.add("New Password is required");
		}
		
		if (confirmPassword == null || confirmPassword.length() == 0) {
			errors.add("Confirm Pwd is required");
		}
		
		if (errors.size() > 0) {
			return errors;
		}
		
		if (!newPassword.equals(confirmPassword)) {
			errors.add("Passwords do not match");
		}

		return errors;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
}
