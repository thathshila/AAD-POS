package controller;

import bo.custom.CustomerBO;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.CustomerDTO;
import bo.BoFactory;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;

@WebServlet(urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {

    private final CustomerBO customerBO = (CustomerBO) BoFactory.getBoFactory().getBoType(BoFactory.BoType.CUSTOMER);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext servletContext = req.getServletContext();
        BasicDataSource basicDataSource = (BasicDataSource) servletContext.getAttribute("dataSource");

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try (var connection = basicDataSource.getConnection();
             var statement = connection.createStatement();
             var resultSet = statement.executeQuery("SELECT * FROM customer")) {

            // JSON array to hold the customer data
            var customersJson = new StringBuilder("[");
            boolean first = true;

            // Iterate through the result set and construct JSON
            while (resultSet.next()) {
                if (!first) {
                    customersJson.append(",");
                }
                first = false;

                customersJson.append("{")
                        .append("\"id\":").append("\"").append(resultSet.getString("id")).append("\",")
                        .append("\"name\":").append("\"").append(resultSet.getString("name")).append("\",")
                        .append("\"email\":").append("\"").append(resultSet.getString("email")).append("\",")
                        .append("\"contact\":").append("\"").append(resultSet.getString("contact")).append("\",")
                        .append("\"address\":").append("\"").append(resultSet.getString("address")).append("\"")
                        .append("}");
            }

            customersJson.append("]");

            // Send the JSON response
            resp.getWriter().write(customersJson.toString());

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to fetch customers.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Get DataSource from ServletContext
            BasicDataSource dataSource = (BasicDataSource) getServletContext().getAttribute("dataSource");

            // Parse JSON payload
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = req.getReader().readLine()) != null) {
                sb.append(line);
            }
            String json = sb.toString();

            // Convert JSON to DTO
            ObjectMapper mapper = new ObjectMapper();
            CustomerDTO customerDTO = mapper.readValue(json, CustomerDTO.class);

            // Call the Business Layer
            boolean isAdded = customerBO.saveCustomer(customerDTO, dataSource);

            // Send Response
            resp.setContentType("application/json");
            if (isAdded) {
                resp.getWriter().write("{\"status\":\"success\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"status\":\"error\"}");
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
            e.printStackTrace();
        }
    }
}
