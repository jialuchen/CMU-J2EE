/**
 * Author :  Jialu Chen
 * Andrew ID: jialuc
 */

package controller;

import databeans.Favorite;
import databeans.User;
import formbeans.IdForm;
import model.Model;
import model.FavoriteDAO;
import model.UserDAO;
import org.genericdao.RollbackException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class RemoveAction extends Action {
    private FormBeanFactory<IdForm> formBeanFactory = FormBeanFactory.getInstance(IdForm.class);

    private FavoriteDAO favoriteDAO;
    private UserDAO userDAO;

    public RemoveAction(Model model) {
        favoriteDAO = model.getFavoriteDAO();
        userDAO = model.getUserDAO();
    }

    public String getName() {
        return "remove.do";
    }

    public String perform(HttpServletRequest request) {
        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors", errors);

        try {
            // Set up user list for nav bar
            request.setAttribute("userList", userDAO.getUsers());

            IdForm form = formBeanFactory.create(request);

            User user = (User) request.getSession().getAttribute("user");

            int id = form.getIdAsInt();
            favoriteDAO.delete(id, user.getUserId());

            // Be sure to get the favoriteList after the delete
            Favorite[] favoriteList = favoriteDAO.getFavoritesList(user.getUserId());
            request.setAttribute("favoriteList", favoriteList);

            return "manage.jsp";
        } catch (RollbackException e) {
            errors.add(e.getMessage());
            return "error.jsp";
        } catch (FormBeanException e) {
            errors.add(e.getMessage());
            return "error.jsp";
        }
    }
}
