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

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonWriter;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/item")
public class ItemServlet extends HttpServlet {

    ItemBO itemBO = (ItemBO) BoFactory.getBoFactory().getBoType(BoFactory.BoType.ITEM);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        BasicDataSource dataSource = (BasicDataSource) getServletContext().getAttribute("dataSource");

        ItemBO itemBO = new ItemBOImpl(); // Assuming ItemBOImpl is the implementation of ItemBO

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

}
