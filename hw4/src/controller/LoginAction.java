/**
 * Author :  Jialu Chen
 * Andrew ID: jialuc
 */

package controller;


import databeans.User;
import formbeans.LoginForm;
import model.Model;
import model.UserDAO;
import org.genericdao.RollbackException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class LoginAction extends Action {
    private FormBeanFactory<LoginForm> formBeanFactory = FormBeanFactory.getInstance(LoginForm.class);

    private UserDAO userDAO;

    public LoginAction(Model model) {
        userDAO = model.getUserDAO();
    }

    public String getName() {
        return "login.do";
    }

    public String perform(HttpServletRequest request) {
        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors", errors);

        try {

            request.setAttribute("userList", userDAO.getUsers());

            LoginForm form = formBeanFactory.create(request);
            request.setAttribute("form", form);

            if (!form.isPresent()) {
                return "login.jsp";
            }

            errors.addAll(form.getValidationErrors());
            if (errors.size() != 0) {
                return "login.jsp";
            }

            User user = userDAO.read(form.getEmailAddress());

            if (user == null) {
                errors.add("User Name not found");
                return "login.jsp";
            }

            if (!user.checkPassword(form.getPassword())) {
                errors.add("Incorrect password");
                return "login.jsp";
            }

            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            return "manage.do";
        } catch (RollbackException e) {
            errors.add(e.getMessage());
            return "error.jsp";
        } catch (FormBeanException e) {
            errors.add(e.getMessage());
            return "error.jsp";
        }
    }
}
