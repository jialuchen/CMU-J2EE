/**
 * Author :  Jialu Chen
 * Andrew ID: jialuc
 */

package formbeans;

import org.mybeans.form.FormBean;

import java.util.ArrayList;
import java.util.List;

public class RegisterForm extends FormBean {

    private String emailAddress;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;

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


    public List<String> getValidationErrors() {
        List<String> errors = new ArrayList<String>();


        if (emailAddress == null || emailAddress.length() == 0) {
            errors.add("Email address is required");
        } else if (!isValidEmailAddress(emailAddress)) {
            errors.add("Email address is invalid");
        }
        if (password == null || password.replaceAll(" ", "").length() == 0) {

            errors.add("Password is required");
        }

        if (!password.equals(confirmPassword)) {
            errors.add("Password does not match.");

        }

        if (firstName == null || firstName.length() == 0) {

            errors.add("First name is required");
        }

        if (lastName == null || lastName.length() == 0) {

            errors.add("Last name is required");
        }


        return errors;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}

