package formbean; 
/**
 * Author : Jialu Chen
 * date: 11/27/2016
 * course: 08672
 */

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public class FavoriteForm{
    private String url;
    private String comment;
    
    public FavoriteForm(HttpServletRequest request) {
    	url = sanitize(request.getParameter("url"));
    	comment = sanitize(request.getParameter("comment"));
    }


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
            errors.add("Url may not contain angle brackets or quotes");
        }

        return errors;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
    private String sanitize(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;");
    }
}
