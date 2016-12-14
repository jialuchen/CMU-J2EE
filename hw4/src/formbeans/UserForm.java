/**
 * Author :  Jialu Chen
 * Andrew ID: jialuc
 */

package formbeans;

import org.mybeans.form.FormBean;

import java.util.ArrayList;
import java.util.List;

public class UserForm extends FormBean {
    private String userId = "";

    public int getUserId() {
        return Integer.valueOf(userId);
    }

    public void setUserId(String s) {
        userId = s;
    }

    public List<String> getValidationErrors() {
        List<String> errors = new ArrayList<>();

        try {
            Integer.parseInt(userId);
        } catch (NumberFormatException e) {
            errors.add("User Name is required");
        }


        return errors;
    }

}
