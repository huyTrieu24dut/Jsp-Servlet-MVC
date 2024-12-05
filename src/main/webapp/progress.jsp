<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // Database connection variables
    String jdbcURL = "jdbc:mysql://localhost:3306/your_database";
    String dbUser = "root";
    String dbPassword = "password";

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    // Retrieve the user ID from session (example: replace with your actual session handling logic)
    int userId = (int) session.getAttribute("user_id");

    // Initialize a list to store file records
    List<Map<String, String>> fileRecords = new ArrayList<>();

    try {
        // Connect to the database
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);

        // Query the file_processing table for the user's files
        String sql = "SELECT id, file_path, output_path, status FROM file_processing WHERE user_id = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setInt(1, userId);
        rs = stmt.executeQuery();

        // Iterate over the result set
        while (rs.next()) {
            Map<String, String> fileRecord = new HashMap<>();
            fileRecord.put("id", rs.getString("id"));
            fileRecord.put("fileName", rs.getString("file_path").substring(rs.getString("file_path").lastIndexOf("/") + 1)); // Extract file name
            fileRecord.put("status", rs.getString("status"));
            fileRecord.put("outputPath", rs.getString("output_path"));
            fileRecords.add(fileRecord);
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        // Close resources
        if (rs != null) rs.close();
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
    }
%>
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
            <a href="./ConvertPage1.jsp">Convert</a>
            <a href="./progress.jsp">Progress</a>
        </div>
        <button class="logout-btn" onclick="logout()">Logout</button>
    </nav>
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
                if (fileRecords.isEmpty()) {
            %>
                <tr>
                    <td colspan="3">No files found</td>
                </tr>
            <%
                } else {
                    for (Map<String, String> record : fileRecords) {
            %>
                <tr>
                    <td><%= record.get("fileName") %></td>
                    <td class="status-<%= record.get("status").toLowerCase() %>"><%= record.get("status") %></td>
                    <td>
                        <%
                            if ("DONE".equals(record.get("status"))) {
                        %>
                            <a href="<%= record.get("outputPath") %>" download>
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
