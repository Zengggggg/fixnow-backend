<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="container">
    <h1>Welcome to the Home Page</h1>

    <c:choose>
        <c:when test="${not empty sessionScope.user}">
            <p>Hello, <strong>${sessionScope.user.username}</strong>!</p>
        </c:when>
        <c:otherwise>
            <p>You are not logged in.</p>
            <a href="/login">Login</a> | <a href="/register">Register</a>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
