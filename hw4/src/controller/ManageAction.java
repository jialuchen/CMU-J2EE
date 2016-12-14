/**
 * Author :  Jialu Chen
 * Andrew ID: jialuc
 */

package controller;

import databeans.Favorite;
import databeans.User;
import model.Model;
import model.FavoriteDAO;
import model.UserDAO;
import org.genericdao.RollbackException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class ManageAction extends Action {

    private FavoriteDAO favoriteDAO;
    private UserDAO userDAO;

    public ManageAction(Model model) {
        favoriteDAO = model.getFavoriteDAO();
        userDAO = model.getUserDAO();
    }

    public String getName() {
        return "manage.do";
    }

    public String perform(HttpServletRequest request) {
        // Set up the errors list
        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors", errors);

        try {
            // Set up user list for nav bar
            request.setAttribute("userList", userDAO.getUsers());

            User user = (User) request.getSession(false).getAttribute("user");
            Favorite[] favoriteList;
            if (user != null) {
                favoriteList = favoriteDAO.getFavoritesList(user.getUserId());
            } else {
                favoriteList = favoriteDAO.getFavoritesList();

            }
            request.setAttribute("favoriteList", favoriteList);

            return "manage.jsp";
        } catch (RollbackException e) {
            errors.add(e.getMessage());
            return "error.jsp";
        }
    }
}
