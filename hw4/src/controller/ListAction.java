/**
 * Author :  Jialu Chen
 * Andrew ID: jialuc
 */

package controller;

import databeans.Favorite;
import databeans.User;
import formbeans.UserForm;
import model.Model;
import model.FavoriteDAO;
import model.UserDAO;
import org.genericdao.RollbackException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class ListAction extends Action {
    private FormBeanFactory<UserForm> formBeanFactory = FormBeanFactory.getInstance(UserForm.class);

    private FavoriteDAO favoriteDAO;
    private UserDAO userDAO;

    public ListAction(Model model) {
        favoriteDAO = model.getFavoriteDAO();
        userDAO = model.getUserDAO();
    }

    public String getName() {
        return "list.do";
    }

    public String perform(HttpServletRequest request) {
        // Set up the request attributes (the errors list and the form bean so
        // we can just return to the jsp with the form if the request isn't correct)
        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors", errors);

        try {

            request.setAttribute("userList", userDAO.getUsers());

            UserForm form = formBeanFactory.create(request);

            int userId = form.getUserId();


            User user = userDAO.read(userId);
            if (user == null) {
                errors.add("Invalid User: " + userId);
                return "error.jsp";
            }

            Favorite[] favoriteList = favoriteDAO.getFavoritesList(user.getUserId());
            request.setAttribute("favoriteList", favoriteList);
            return "list.jsp";
        } catch (RollbackException e) {
            errors.add(e.getMessage());
            return "error.jsp";
        } catch (FormBeanException e) {
            errors.add(e.getMessage());
            return "error.jsp";
        }
    }
}
