<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    // Redirect directly to admin dashboard
    response.sendRedirect(request.getContextPath() + "/admin/dashboard");
%>