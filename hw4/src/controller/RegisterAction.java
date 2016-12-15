/**
 * Author :  Jialu Chen
 * Andrew ID: jialuc
 */
package controller;

import databeans.User;
import formbeans.RegisterForm;
import model.Model;
import model.UserDAO;
import org.genericdao.RollbackException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class RegisterAction extends Action {
    private FormBeanFactory<RegisterForm> formBeanFactory = FormBeanFactory.getInstance(RegisterForm.class);

    private UserDAO userDAO;

    public RegisterAction(Model model) {
        userDAO = model.getUserDAO();
    }

    public String getName() {
        return "register.do";
    }

    public String perform(HttpServletRequest request) {
        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors", errors);

        try {

            request.setAttribute("userList", userDAO.getUsers());

            RegisterForm form = formBeanFactory.create(request);
            request.setAttribute("form", form);

            if (!form.isPresent()) {
                return "register.jsp";
            }
            
            if (userDAO.read(form.getEmailAddress()) != null) {
            	errors.add("The email has already existed!");
            }

            errors.addAll(form.getValidationErrors());
            
            if (errors.size() != 0) {
                return "register.jsp";
            }

            User user = new User();

            user.setEmailAddress(form.getEmailAddress());
            user.setFirstName(form.getFirstName());
            user.setLastName(form.getLastName());
            user.recordPassword(form.getPassword());
            userDAO.create(user);

            HttpSession session = request.getSession(false);
            session.setAttribute("user", user);

            return "manage.do";
        } catch (RollbackException e) {
            errors.add(e.getMessage());
            return "register.jsp";
        } catch (FormBeanException e) {
            errors.add(e.getMessage());
            return "register.jsp";
        }
    }
}
