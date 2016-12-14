<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:forEach var="error" items="${errors}">
	<h3 style="color:red"> ${error} </h3>
</c:forEach>

<c:choose>
	<c:when test="${ (empty user) }">
		Click <a href="login.do">here</a> to login.
	</c:when>
	<c:otherwise>
		Click <a href="manage.do">here</a> to return to Favorite List.
	</c:otherwise>
</c:choose>