<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<p>
<table>

    <c:forEach var="favorite" items="${favoriteList}">
        <tr>
            <c:choose>
                <c:when test="${ (empty user) }">
                </c:when>
                <c:otherwise>

                    <c:set var="loginUserId" value="${user.userId}"/>
                    <c:set var="currentUserId" value="${favorite.userId}"/>
                    <c:choose>
                        <c:when test="${loginUserId eq currentUserId}">
                            <td>
                                <form method="POST" action="remove.do">
                                    <input type="hidden" name="id" value="${favorite.id}"/>
                                    <input type="submit" value="X"/>
                                </form>
                            </td>
                        </c:when>
                    </c:choose>
                </c:otherwise>
            </c:choose>


            <td>
                    ${favorite.comment}
            </td>

            <td>
                <a href="update_click.do?id=${favorite.id}">${favorite.url}
                </a>
            </td>

            <td>
                    ${favorite.clickCount} Clicks
            </td>

        </tr>

    </c:forEach>


</table>

<c:choose>
    <c:when test="${ (empty favoriteList) }">
        No Favorite List from current user.
    </c:when>
    <c:otherwise>
    </c:otherwise>
</c:choose>

<p></p>