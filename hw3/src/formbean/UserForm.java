package formbean;

import java.util.ArrayList;
import java.util.List;
/**
 * Author :  Jialu Chen
 * Andrew ID: jialuc
 */
public class UserForm {
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
