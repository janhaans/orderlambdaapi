package org.example;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CreateOrderLambda {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DynamoDB dynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient());
    private final Table table = dynamoDB.getTable(System.getenv("ORDERS_TABLE"));

    public APIGatewayProxyResponseEvent createOrder(APIGatewayProxyRequestEvent request) throws JsonProcessingException {

        Order order = objectMapper.readValue(request.getBody(), Order.class);

        Item item = new Item()
                .withPrimaryKey("id", order.getId())
                .withString("itemName", order.getItemName())
                .withInt("quantity", order.getQuantity());
        table.putItem(item);

        return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody("Order ID: " + order.getId());
    }
}
