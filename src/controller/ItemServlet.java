package controller;

import bo.BoFactory;
import bo.custom.ItemBO;
import bo.custom.impl.ItemBOImpl;
import dto.ItemDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.json.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/item")
public class ItemServlet extends HttpServlet {

    ItemBO itemBO = (ItemBO) BoFactory.getBoFactory().getBoType(BoFactory.BoType.ITEM);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        BasicDataSource dataSource = (BasicDataSource) getServletContext().getAttribute("dataSource");

        // Fetch all items as DTOs
        List<ItemDTO> itemDTOs = itemBO.getAllItems(dataSource);

        // Convert the list to JSON using Json.createArrayBuilder
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

        for (ItemDTO itemDTO : itemDTOs) {
            jsonArrayBuilder.add(Json.createObjectBuilder()
                    .add("item_code", itemDTO.getItemId())
                    .add("item_name", itemDTO.getItemName())
                    .add("quantity", itemDTO.getItemQuantity())
                    .add("unit_price", itemDTO.getItemPrice()));
        }

        try (JsonWriter jsonWriter = Json.createWriter(resp.getWriter())) {
            jsonWriter.writeArray(jsonArrayBuilder.build());
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        try {
            // Parse JSON payload
            BufferedReader reader = req.getReader();
            JsonObject jsonObject = Json.createReader(reader).readObject();

            String itemId = jsonObject.getString("item_code");
            String itemName = jsonObject.getString("item_name");
            String itemQuantity = jsonObject.getString("quantity");
            String itemPrice = jsonObject.getString("unit_price");

            if (itemId.isEmpty() || itemName.isEmpty() || itemQuantity.isEmpty() || itemPrice.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(
                        Json.createObjectBuilder()
                                .add("status", "error")
                                .add("message", "Invalid input. All fields are required.")
                                .build()
                                .toString()
                );
                return;
            }

            // Create ItemDTO and save item
            ItemDTO itemDTO = new ItemDTO(itemId, itemName, itemQuantity, itemPrice);
            boolean isSaved = itemBO.saveItem((BasicDataSource) getServletContext().getAttribute("dataSource"), itemDTO);

            if (isSaved) {
                resp.getWriter().write(
                        Json.createObjectBuilder()
                                .add("status", "success")
                                .add("message", "Item saved successfully!")
                                .build()
                                .toString()
                );
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write(
                        Json.createObjectBuilder()
                                .add("status", "error")
                                .add("message", "Failed to save item.")
                                .build()
                                .toString()
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(
                    Json.createObjectBuilder()
                            .add("status", "error")
                            .add("message", "Server error: " + e.getMessage())
                            .build()
                            .toString()
            );
        }
    }

}
