package favoritelist;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.RollbackException;

import dao.FavoriteDAO;
import dao.UserDAO;
import databean.Favorite;
import databean.User;
import formbean.IdForm;
import formbean.FavoriteForm;
import formbean.LoginForm;
import formbean.RegisterForm;

public class FavoriteList extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private FavoriteDAO favoriteDAO;
	private UserDAO userDAO;

	public void init() throws ServletException {
		String jdbcDriverName = getInitParameter("jdbcDriver");
		String jdbcURL = getInitParameter("jdbcURL");

		try {
			ConnectionPool cp = new ConnectionPool(jdbcDriverName, jdbcURL);
/*	
			cp.setDebugOutput(System.out);  // Print out the generated SQL
*/
			userDAO = new UserDAO(cp);
			favoriteDAO = new FavoriteDAO(cp);
		} catch (DAOException e) {
			throw new ServletException(e);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session.getAttribute("user") == null) {
			outputLoginPage(response, null, null);
		} else {
			outputFavoriteList(response);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session.getAttribute("user") == null) {
			login(request, response);
		} else {
			manageList(request, response);
		}
	}

	private void login(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<String> errors = new ArrayList<String>();

		LoginForm form = new LoginForm(request);

		errors.addAll(form.getValidationErrors());
		if (errors.size() != 0) {
			outputLoginPage(response, form, errors);
			return;
		}

		try {
			User user = new User();

			if (form.getButton().equals("Register")) {
				register(request, response);
			} else {
				user = userDAO.read(form.getEmailAddress());
				if (user == null) {
					errors.add("No such user");
					outputLoginPage(response, form, errors);
					return;
				}

				if (!user.checkPassword(form.getPassword())) {
					errors.add("Incorrect password");
					outputLoginPage(response, form, errors);
					return;
				}
			}

			HttpSession session = request.getSession();
			session.setAttribute("user", user);
			outputFavoriteList(response);
		} catch (RollbackException e) {
			errors.add(e.getMessage());
			outputLoginPage(response, form, errors);
		}
	}
	
	private void register(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<String> rstErrors = new ArrayList<String>();
		RegisterForm rstForm = new RegisterForm(request);
		
		rstErrors.addAll(rstForm.getValidationErrors());
		if (rstErrors.size() != 0) {
			outputRegisterPage(response, rstForm, rstErrors);
			return;
		}
		
		try {
			User user;

				user = userDAO.read(rstForm.getEmailAddress());
				if (user != null) {
					rstErrors.add("This email address has already been registered!");
					outputRegisterPage(response, rstForm, rstErrors);
					return;
				}

			HttpSession session = request.getSession();
			session.setAttribute("user", user);
			outputFavoriteList(response);
			userDAO.create(user);
		} catch (RollbackException e) {
			rstErrors.add(e.getMessage());
			outputRegisterPage(response, rstForm, rstErrors);
		}
	}

	private void manageList(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Look at the action parameter to see what we're doing to the list
		String action = request.getParameter("action");

		if (action == null) {
			outputFavoriteList(response, "No action specified.");
			return;
		}

		if (action.equals("Add Favorite")) {
			processAdd(request, response);
			return;
		}

		if (action.equals("Log Out")) {
			processLogOut(request, response);
			return;
		}

		//outputFavoriteList(response, "Invalid action: " + action);
	}

	private void processAdd(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<String> errors = new ArrayList<String>();

		FavoriteForm form = new FavoriteForm(request);

		errors.addAll(form.getValidationErrors());
		if (errors.size() > 0) {
			outputFavoriteList(response, errors);
			return;
		}

		try {
			Favorite bean = new Favorite();
			bean.setUrl(form.getUrl());
			bean.setComment(form.getUrl());
			bean.setClickCount(0);
			User u = (User) request.getSession().getAttribute("user");
			bean.setUserId(u.getUserId());
			favoriteDAO.create(bean);
			
			outputFavoriteList(response, "Item Added");
		} catch (RollbackException e) {
			errors.add(e.getMessage());
			outputFavoriteList(response, errors);
		}
	}
	
	private void processClick(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		To do;
		
	}

	// Methods that generate & output HTML
	
	private final String servletName = this.getClass().getSimpleName();

	private void generateHead(PrintWriter out) {
		out.println("  <head>");
		out.println("    <meta charset=\"utf-8\"/>");
		out.println("    <title>" + servletName + "</title>");
		out.println("  </head>");
	}

	private void outputLoginPage(HttpServletResponse response, LoginForm form, List<String> errors) throws IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		out.println("<!DOCTYPE html>");
		out.println("<html>");

		generateHead(out);

		out.println("<body>");
		out.println("<h2>" + servletName + " Login</h2>");

		if (errors != null && errors.size() > 0) {
			for (String error : errors) {
				out.println("<p style=\"font-size: large; color: red\">");
				out.println(error);
				out.println("</p>");
			}
		}

		// Generate an HTML <form> to get data from the user
		out.println("<form method=\"POST\">");
		out.println("    <table>");
		out.println("        <tr>");
		out.println(
				"            <td style=\"font-size: x-large\">User Name:</td>");
		out.println("            <td>");
		out.println("                <input type=\"text\" name=\"emailAddress\"");
		if (form != null && form.getEmailAddress() != null) {
			out.println(
					"                    value=\"" + form.getEmailAddress() + "\"");
		}
		out.println("                autofocus />");
		out.println("            </td>");
		out.println("        </tr>");
		out.println("        <tr>");
		out.println(
				"            <td style=\"font-size: x-large\">Password:</td>");
		out.println(
				"            <td><input type=\"password\" name=\"password\" /></td>");
		out.println("        </tr>");
		out.println("        <tr>");
		out.println(
				"            <td colspan=\"2\" style=\"text-align: center;\">");
		out.println(
				"                <input type=\"submit\" name=\"button\" value=\"Login\" />");
		out.println(
				"                <input type=\"submit\" name=\"button\" value=\"Register\" />");
		out.println("            </td>");
		out.println("        </tr>");
		out.println("    </table>");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");
	}
	
	private void outputRegisterPage(HttpServletResponse response, RegisterForm form, List<String> errors) throws IOException {
				
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		out.println("<!DOCTYPE html>");
		out.println("<html>");

		generateHead(out);

		out.println("<body>");
		out.println("<h2>" + servletName + " Register</h2>");

		if (errors != null && errors.size() > 0) {
			for (String error : errors) {
				out.println("<p style=\"font-size: large; color: red\">");
				out.println(error);
				out.println("</p>");
			}
		}

		// Generate an HTML <form> to get data from the user
		out.println("<form method=\"POST\">");
		out.println("    <table>");
		out.println("        <tr>");
		out.println(
				"            <td style=\"font-size: x-large\">User Name:</td>");
		out.println("            <td>");
		out.println("                <input type=\"text\" name=\"emailAddress\"");
		if (form != null && form.getEmailAddress() != null) {
			out.println(
					"                    value=\"" + form.getEmailAddress() + "\"");
		}
		out.println("                autofocus />");
		out.println("            </td>");
		out.println("        </tr>");
		out.println("        <tr>");
		out.println(
				"            <td style=\"font-size: x-large\">First Name:</td>");
		out.println(
				"            <td><input name=\"firstName\" /></td>");
		out.println("        </tr>");
		out.println("        <tr>");
		out.println(
				"            <td style=\"font-size: x-large\">Last Name:</td>");
		out.println(
				"            <td><input name=\"lastName\" /></td>");
		out.println("        </tr>");
		out.println("        <tr>");
		out.println(
				"            <td style=\"font-size: x-large\">Password:</td>");
		out.println(
				"            <td><input type=\"password\" name=\"password\" /></td>");
		out.println("        </tr>");
		out.println("        <tr>");
		out.println(
				"            <td colspan=\"2\" style=\"text-align: center;\">");
		out.println(
				"                <input type=\"submit\" name=\"button\" value=\"Register\" />");
		out.println("            </td>");
		out.println("        </tr>");
		out.println("    </table>");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");
	}

	private void outputFavoriteList(HttpServletResponse response)
			throws IOException {
		// Just call the version that takes a List passing an empty List
		List<String> list = new ArrayList<String>();
		outputFavoriteList(response, list);
	}

	private void outputFavoriteList(HttpServletResponse response, String message)
			throws IOException {
		// Put the message into a List and call the version that takes a List
		List<String> list = new ArrayList<String>();
		list.add(message);
		outputFavoriteList(response, list);
	}

	private void outputFavoriteList(HttpServletResponse response,
			List<String> messages) throws IOException {
		// Get the list of items to display at the end
		Favorite[] beans;
		try {
			beans = favoriteDAO.getFavoritesList();
		} catch (RollbackException e) {
			// If there's an access error, add the message to our list of
			// messages
			messages.add(e.getMessage());
			beans = new Favorite[0];
		}

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		out.println("<!DOCTYPE html>");
		out.println("<html>");

		generateHead(out);

		out.println("<body>");
		out.println("<h2>Favorites for ");
		if (beans.length > 0) {
			int usrID = beans[0].getUserId();
			User usr = userDAO.read(usrID);
			out.println(usr.getFirstName() + " ");
			out.println(usr.getLastName());
		}
				
		out.println("</h2>");

		// Generate an HTML <form> to get data from the user
		out.println("<form method=\"POST\">");
		out.println("    <table>");
		out.println("        <tr><td colspan=\"3\"><hr/></td></tr>");
		out.println("        <tr>");
		out.println(
				"            <td style=\"font-size: large\">URL:</td>");
		out.println(
				"            <td colspan=\"2\"><input type=\"text\" size=\"40\" name=\"url\" autofocus/></td>");
		out.println("        </tr>");
		out.println("        <tr>");
		out.println("        <tr>");
		out.println(
				"            <td style=\"font-size: large\">Comment:</td>");
		out.println(
				"            <td colspan=\"2\"><input type=\"text\" size=\"40\" name=\"comment\" /></td>");
		out.println("        </tr>");
		out.println("        <tr>");
		out.println("            <td></td>");
		out.println(
				"            <td><input type=\"submit\" name=\"action\" value=\"Add Favorite\"/></td>");
		out.println(
				"            <td><input type=\"submit\" name=\"action\" value=\"Log out\"/></td>");
		out.println("        </tr>");
		out.println("        <tr><td colspan=\"3\"><hr/></td></tr>");
		out.println("    </table>");
		out.println("</form>");

		for (String message : messages) {
			out.println("<p style=\"font-size: large; color: red\">");
			out.println(message);
			out.println("</p>");
		}

		out.println("<table>");
		for (int i = 0; i < beans.length; i++) {
			out.println("    <tr>");
			out.println("        <td>");
			out.println(
					"                <input type=\"hidden\" name=\"id\" value=\""
							+ beans[i].getId() + "\" />");
			out.println("        </td>");
			out.println("        <td><span style=\"font-size: x-large\">"
					+ beans[i].getUrl() + "</td><br>");
			out.println("        <td><span style=\"font-size: x-large\">"
					+ beans[i].getComment() + "</td><br>");
			out.println("        <td><span style=\"font-size: x-large\">"
					+ beans[i].getClickCount() + "</td><br>");
			out.println("    </tr>");
		}
		out.println("</table>");

		out.println("</body>");
		out.println("</html>");
	}
}

