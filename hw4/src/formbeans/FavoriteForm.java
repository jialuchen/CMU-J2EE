/**
 * Author :  Jialu Chen
 * Andrew ID: jialuc
 */

package formbeans; 
import org.mybeans.form.FormBean;

import java.util.ArrayList;
import java.util.List;

public class FavoriteForm extends FormBean {
    private String url;
    private String comment;


    public String getUrl() {
        return url;
    }


    public void setUrl(String s) {
        url = s.trim();
    }

    public List<String> getValidationErrors() {
        List<String> errors = new ArrayList<String>();


        if (url == null || url.length() == 0) {
            errors.add("The url could not be empty");
        }else if (url.matches(".*[<>\"].*")) {
            errors.add("Item may not contain angle brackets or quotes");
        }

        return errors;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
