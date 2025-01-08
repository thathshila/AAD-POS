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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;


@WebServlet(urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {

    private final CustomerBO customerBO = (CustomerBO) BoFactory.getBoFactory().getBoType(BoFactory.BoType.CUSTOMER);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Retrieve the DataSource object from the servlet context
        BasicDataSource dataSource = (BasicDataSource) getServletContext().getAttribute("dataSource");

        // Set the response type to JSON
        resp.setContentType("application/json");
        try {
            // Call the service method to get all customers
            List<CustomerDTO> customers = customerBO.getCustomers(dataSource);

            // Create a JSON array builder to build the response JSON
            JsonArrayBuilder customerArray = Json.createArrayBuilder();

            // Iterate over the customers and add each to the JSON array
            for (CustomerDTO customer : customers) {
                JsonObjectBuilder customerObject = Json.createObjectBuilder();
                customerObject.add("id", customer.getCustomerId());
                customerObject.add("name", customer.getCustomerName());
                customerObject.add("address", customer.getCustomerAddress());
                customerObject.add("email", customer.getCustomerEmail());
                customerObject.add("contact", customer.getCustomerPhone());
                customerArray.add(customerObject);
            }

            // Write the JSON response to the output stream
            resp.getWriter().write(customerArray.build().toString());
        } catch (SQLException e) {
            // Handle exceptions (e.g., database issues)
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
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Extract the customerId from the URL (assuming it's in the URL path, e.g., /customer/{customerId})
        String customerId = req.getPathInfo() != null ? req.getPathInfo().substring(1) : null;

        if (customerId == null || customerId.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
            resp.getWriter().write("Customer ID is required");
            return;
        }

        // Initialize the database connection
        BasicDataSource dataSource = (BasicDataSource) getServletContext().getAttribute("dataSource");

        try {
            // Call the delete method in the service layer
            boolean deleted = customerBO.deleteCustomer(customerId, dataSource);

            if (deleted) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT); // 204 No Content (successful deletion, no content in response)
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404 Not Found (customer not found)
                resp.getWriter().write("Customer not found");
            }
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            resp.getWriter().write("Error occurred while deleting customer: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Parse the customer data from the request body (expected in JSON format)
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = req.getReader().readLine()) != null) {
            stringBuilder.append(line);
        }

        // Convert the JSON string to a CustomerDTO object (using a library like Jackson or Gson)
        String jsonData = stringBuilder.toString();
        ObjectMapper objectMapper = new ObjectMapper();
        CustomerDTO customerDTO = objectMapper.readValue(jsonData, CustomerDTO.class);

        // Validate that the customerDTO is not null and contains necessary data
        if (customerDTO.getCustomerId() == null || customerDTO.getCustomerName() == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
            resp.getWriter().write("Missing customer data");
            return;
        }

        // Initialize the database connection (assuming you have a BasicDataSource setup)
        BasicDataSource dataSource = (BasicDataSource) getServletContext().getAttribute("dataSource");

        try {
            // Call the service method to update the customer
            boolean updated = customerBO.updateCustomer(customerDTO, dataSource);

            if (updated) {
                resp.setStatus(HttpServletResponse.SC_OK); // 200 OK
                resp.getWriter().write("Customer updated successfully");
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404 Not Found
                resp.getWriter().write("Customer not found");
            }
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            resp.getWriter().write("Error occurred while updating customer: " + e.getMessage());
        }
    }

}
