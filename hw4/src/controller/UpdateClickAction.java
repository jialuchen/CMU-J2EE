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

public class UpdateClickAction extends Action {
    private FormBeanFactory<IdForm> formBeanFactory = FormBeanFactory.getInstance(IdForm.class);

    private FavoriteDAO favoriteDAO;
    private UserDAO userDAO;

    public UpdateClickAction(Model model) {
        favoriteDAO = model.getFavoriteDAO();
        userDAO = model.getUserDAO();
    }

    public String getName() {
        return "update_click.do";
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
            Favorite favorite = favoriteDAO.read(id);
            favoriteDAO.increment(favorite);


            Favorite[] favoriteList;
            if (user != null) {
                favoriteList = favoriteDAO.getFavoritesList(user.getUserId());
            } else {
                favoriteList = favoriteDAO.getFavoritesList();
            }

            request.setAttribute("favoriteList", favoriteList);

            return favorite.getUrl();
        } catch (RollbackException e) {
            errors.add(e.getMessage());
            return "error.jsp";
        } catch (FormBeanException e) {
            errors.add(e.getMessage());
            return "error.jsp";
        }
    }
}
