package controller;

import databeans.User;
import formbeans.ChangePwdForm;
import model.Model;
import model.UserDAO;
import org.genericdao.RollbackException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Author :  Jialu Chen
 * Andrew ID: jialuc
 */
public class ChangePwdAction extends Action {
    private FormBeanFactory<ChangePwdForm> formBeanFactory = FormBeanFactory.getInstance(ChangePwdForm.class);

    private UserDAO userDAO;

    public ChangePwdAction(Model model) {
        userDAO = model.getUserDAO();
    }

    public String getName() {
        return "change-pwd.do";
    }

    public String perform(HttpServletRequest request) {
        // Set up error list
        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors", errors);

        try {
            // Set up user list for nav 
            request.setAttribute("userList", userDAO.getUsers());

            // Load the form parameters into a form bean
            ChangePwdForm form = formBeanFactory.create(request);

            // If no params were passed, return with no errors so that the form will be
            // presented (we assume for the first time).
            if (!form.isPresent()) {
                return "change-pwd.jsp";
            }

            // Check for any validation errors
            errors.addAll(form.getValidationErrors());
            if (errors.size() != 0) {
                return "change-pwd.jsp";
            }

            User user = (User) request.getSession().getAttribute("user");

            if (user.checkPassword(form.getOldPassword())) {
                userDAO.setPassword(user.getUserId(), form.getNewPassword());
            } else {
                errors.add("Current Password does not match.");
                return "change-pwd.jsp";

            }
            // Change the password

            request.setAttribute("message", "Password changed for " + user.getEmailAddress());
            return "success.jsp";
        } catch (RollbackException e) {
            errors.add(e.toString());
            return "error.jsp";
        } catch (FormBeanException e) {
            errors.add(e.toString());
            return "error.jsp";
        }
    }
}
