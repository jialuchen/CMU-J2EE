/**
 * Author :  Jialu Chen
 * Andrew ID: jialuc
 */

package controller;

import model.Model;
import model.UserDAO;
import org.genericdao.RollbackException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LogoutAction extends Action {


    private UserDAO userDAO;


    public LogoutAction(Model model) {
        userDAO = model.getUserDAO();
    }

    public String getName() {
        return "logout.do";
    }

    public String perform(HttpServletRequest request) {

        try {
            request.setAttribute("userList", userDAO.getUsers());
        } catch (RollbackException e) {
            e.printStackTrace();
        }

        HttpSession session = request.getSession(false);
        session.setAttribute("user", null);

        request.setAttribute("message", "You are now logged out");
        return "success.jsp";
    }
}
