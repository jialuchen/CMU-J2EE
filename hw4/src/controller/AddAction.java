/**
 * Author :  Jialu Chen
 * Andrew ID: jialuc
 */

package controller;

import databeans.Favorite;
import databeans.User;
import formbeans.FavoriteForm;
import model.Model;
import model.FavoriteDAO;
import model.UserDAO;
import org.genericdao.RollbackException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddAction extends Action {
    private FormBeanFactory<FavoriteForm> formBeanFactory = FormBeanFactory
            .getInstance(FavoriteForm.class);

    private FavoriteDAO favoriteDAO;
    private UserDAO userDAO;

    public AddAction(Model model) {
        favoriteDAO = model.getFavoriteDAO();
        userDAO = model.getUserDAO();
    }

    public String getName() {
        return "add.do";
    }

    public String perform(HttpServletRequest request) {
        // Set up the errors list
        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors", errors);

        try {
            // Set up user list for nav bar
            request.setAttribute("userList", userDAO.getUsers());

            User user = (User) request.getSession(false).getAttribute("user");
            Favorite[] favoriteList = favoriteDAO.getFavoritesList(user.getUserId());
            request.setAttribute("favoriteList", favoriteList);

            FavoriteForm form = formBeanFactory.create(request);
            errors.addAll(form.getValidationErrors());
            if (errors.size() > 0)
                return "error.jsp";

            Favorite bean = new Favorite();
            bean.setUrl(form.getUrl());
            bean.setComment(form.getComment());
            bean.setUserId(user.getUserId());
            bean.setClickCount(0);
            favoriteDAO.create(bean);
            // Update favoriteList (there's now one more on the list)
            Favorite[] newFavoriteList = favoriteDAO.getFavoritesList(user.getUserId());
            request.setAttribute("favoriteList", newFavoriteList);
            return "manage.jsp";
        } catch (RollbackException e) {
            errors.add(e.getMessage());
            return "manage.jsp";
        } catch (FormBeanException e) {
            errors.add(e.getMessage());
            return "manage.jsp";
        }
    }

    private String fixBadChars(String s) {
        if (s == null || s.length() == 0)
            return s;

        Pattern p = Pattern.compile("[<>\"&]");
        Matcher m = p.matcher(s);
        StringBuffer b = null;
        while (m.find()) {
            if (b == null)
                b = new StringBuffer();
            switch (s.charAt(m.start())) {
            case '<':
                m.appendReplacement(b, "&lt;");
                break;
            case '>':
                m.appendReplacement(b, "&gt;");
                break;
            case '&':
                m.appendReplacement(b, "&amp;");
                break;
            case '"':
                m.appendReplacement(b, "&quot;");
                break;
            default:
                m.appendReplacement(b, "&#" + ((int) s.charAt(m.start())) + ';');
            }
        }

        if (b == null)
            return s;
        m.appendTail(b);
        return b.toString();
    }
}
