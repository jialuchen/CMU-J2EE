<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="template-top.jsp"/>

<jsp:include page="error-list.jsp"/>


<c:choose>
    <c:when test="${ (empty user) }">
    </c:when>
    <c:otherwise>
        <p style="font-size:medium">
            Add a new url:
        </p>

        <p>

        <form action="add.do" method="POST">


            <table>
                <tr>
                    <td style="font-size: x-large">URL:</td>
                    <td>
                        <input id="url" type="text" size="40" name="url"/>
                    </td>
                </tr>

                <tr>
                    <td style="font-size: x-large">Comment:</td>
                    <td>
                        <input id="comment" type="text" size="40" name="comment"/>
                    </td>
                </tr>
                <tr>
                    <td style="font-size: x-large" colspan="2" style="text-align: center;">
                        <input type="submit" name="action" value="Add URL"/>
                    </td>
                </tr>
            </table>

        </form>
        </p>

    </c:otherwise>
</c:choose>

<hr/>


<jsp:include page="website_list.jsp"/>
<jsp:include page="template-bottom.jsp"/>
