<%@page import="model.bean.FileProcessing"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Conversion Progress</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/Resources/css/status.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</head>
<body>
    <nav class="navbar">
        <div class="nav-links">
            <a href="<%= request.getContextPath() %>/ConvertPage1.jsp">Convert</a>
            <a href="<%= request.getContextPath() %>/FileProcessingListServlet">Progress</a>
        </div>
		<form action="LogoutServlet" method="POST">
		    <button class="logout-btn" type="submit">Logout</button>
		</form>

    </nav>
    <%
    	List<FileProcessing> fileProcessingList = (List<FileProcessing>)request.getAttribute("fileProcessingList");
	%>
    <h1>File Processing Status</h1>
    <table>
        <thead>
            <tr>
                <th>File Name</th>
                <th>Status</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <%
                if (fileProcessingList.isEmpty()) {
            %>
                <tr>
                    <td colspan="3">No files found</td>
                </tr>
            <%
                } else {
                    for (FileProcessing fileProcessing : fileProcessingList) {
            %>
                <tr>
                    <td><%= fileProcessing.getName() %></td>
                    <td class="status-<%= fileProcessing.getStatus().toLowerCase() %>"><%= fileProcessing.getStatus() %></td>
                    <td>
                        <%
                            if ("DONE".equals(fileProcessing.getStatus())) {
                        %>
							<a href="<%= request.getContextPath() + "/FileDownLoadServlet?fileId=" + fileProcessing.getId() %>" download>
							    <button class="download-btn">Download</button>
							</a>
                        <%
                            } else {
                        %>
                            <button class="download-btn" disabled>Unavailable</button>
                        <%
                            }
                        %>
                    </td>
                </tr>
            <%
                    }
                }
            %>
        </tbody>
    </table>
</body>
</html>
