
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CalculatorServlet
 */
@WebServlet("/Calculator")
public class Calculator extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String OPERATOR_PLUS = "+";
	private static final String OPERATOR_MINUS = "-";
	private static final String OPERATOR_MULTIPLY = "*";
	private static final String OPERATOR_DIVIDE = "/";
	private static final String OPERATOR_EQUAL = "=";
	private String parameterX;
	private String parameterY;
	private String action;

	// init servlet
	public void init() throws ServletException {
		System.out.println("CalculatorServlet has been initialized and it's only initialized once.");
	}

	//override doGet()
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		parameterX = request.getParameter("parameterX");
		parameterY = request.getParameter("parameterY");
		action = request.getParameter("action");
		String resultValue = null;
		//error list
		List<String> errors = new ArrayList<>();

		try {
			//rule out the initial loading and other blank actions
			if (action != null) {
				//get all the possible errors
				errors.addAll(getValidationErrors());
				request.setAttribute("errors", errors);

				if (errors.size() == 0) {
					resultValue = formatAnswer();
				}
			}
			//print form
			printForm(response, errors, resultValue, parameterX, parameterY);
		} catch (Exception e) {
			//handle other errors
			errors.add(e.getMessage());
			printForm(response, errors, resultValue, parameterX, parameterY);
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	//describe all the possible errors
	private List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();

		if (parameterX == null || parameterX.length() == 0) {
			errors.add("The input of X is empty");
		} else if (!isNumber(parameterX)) {
			errors.add("The input of X is invalid");
		}

		if (parameterY == null || parameterY.length() == 0) {
			errors.add("The input of Y is empty");
		} else if (!isNumber(parameterY)) {
			errors.add("The input of Y is invalid");
		}

		if (!OPERATOR_PLUS.equals(action) && !OPERATOR_MINUS.equals(action) && !OPERATOR_MULTIPLY.equals(action)
				&& !OPERATOR_DIVIDE.equals(action)) {
			errors.add("The action is invalid");
		}

		if (OPERATOR_DIVIDE.equals(action) && isZero(parameterY)) {
			errors.add("Zero could not be divided.");
		}

		return errors;
	}
	//determine if one parameter is zero
	private static boolean isZero(String parameter) {
		try {
			int result = Integer.parseInt(parameter);
			return result == 0;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	//determine if the input is valid number
	private static boolean isNumber(String parameter) {
		if (parameter.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$")) {
			return true;
		}
		return false;
	}
	//format result
	public String formatAnswer() {
		return format(Double.parseDouble(getParameterX())) + " " + getAction() + " "
				+ format(Double.parseDouble(getParameterY())) + " " + OPERATOR_EQUAL + " " + calculateResult();
	}
	//calculate result
	private String calculateResult() {
		double result = 0L;
		switch (action) {
		case OPERATOR_DIVIDE:
			result = Double.parseDouble(parameterX) / Double.parseDouble(parameterY);
			break;
		case OPERATOR_PLUS:
			result = Double.parseDouble(parameterX) + Double.parseDouble(parameterY);
			break;
		case OPERATOR_MINUS:
			result = Double.parseDouble(parameterX) - Double.parseDouble(parameterY);
			break;
		case OPERATOR_MULTIPLY:
			result = Double.parseDouble(parameterX) * Double.parseDouble(parameterY);
			break;
		}

		return format(result);
	}
	//print form
	private void printForm(HttpServletResponse response, List<String> errors, String resultValue, String parameterX,
			String parameterY) throws IOException {
		PrintWriter out = response.getWriter();
		out.print("<!DOCTYPE html>" + "<html>" + "<head>" + "<meta charset=\"utf-8\"/>"
				+ "<title>Simple Calculator</title>" + "</head>" + "<body>" + "<h2>Simple Calculator</h2>");
		if (errors != null) {
			for (String error : errors) {
				out.print("<h3 style=\"color:red\">");
				out.print(error);
				out.print("</h3>");
			}
		}
		out.print("<h3>");
		resultValue = resultValue == null ? "" : resultValue;
		out.print(resultValue);
		out.print("</h3>");
		out.print("<form action=\"Calculator\" method=\"POST\">" + "<table>" + "<tr>"
				+ "<td><label for=\"label_x\">X:</label>"
				+ "<input id=\"label_x\" name=\"parameterX\" type=\"text\" size=\"40\"" + "value="
				+ (parameterX == null ? "" : parameterX) + ">" + "</td>" + "</tr>" + "<tr>" + "<td>"
				+ "<label for=\"label_y\">Y:</label>"
				+ "<input id=\"label_y\" name=\"parameterY\" type=\"text\" size=\"40\"" + "value="
				+ (parameterY == null ? "" : parameterY) + ">" + "</td>" + "</tr>" + "<tr>"
				+ "<td><input class=\"operator\" type=\"submit\" name=\"action\" value=\"+\"/>"
				+ "<input class=\"operator\" type=\"submit\" name=\"action\" value=\"-\"/>"
				+ "<input class=\"operator\" type=\"submit\" name=\"action\" value=\"*\"/>"
				+ "<input class=\"operator\" type=\"submit\" name=\"action\" value=\"/\"/>" + "</td>" + "</tr>"
				+ "</table>" + "</form>" + "</body>" + "</html>");
	}
	//get required format
	private String format(double tempAnswer) {
		NumberFormat formatter = new DecimalFormat("#,##0.00");
		return formatter.format(tempAnswer);
	}

	public String getParameterX() {
		return parameterX;
	}

	public String getParameterY() {
		return parameterY;
	}

	public void setParameterX(String s) {
		parameterX = s.trim();
	}

	public void setParameterY(String s) {
		parameterY = s.trim();
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}
