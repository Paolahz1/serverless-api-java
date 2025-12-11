package org.example.infrastructure.output.adapter;


import org.example.domain.model.User;
import org.example.domain.port.spi.IUserPersistencePort;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.Map;


public class UserDynamoAdapter implements IUserPersistencePort {

    private final DynamoDbClient dynamoDbClient;
    private final String tableName;

    public UserDynamoAdapter() {
        this.dynamoDbClient = DynamoDbClient.builder()
                .region(Region.US_EAST_2)
                .build();
        this.tableName = System.getenv("USERS_TABLE");
        if (this.tableName == null || this.tableName.isBlank()) {
            throw new IllegalStateException("Environment variable USERS_TABLE is not set or empty");
        }
    }

    @Override
    public void save(User user) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.builder().s(user.getIdentification()).build());
        item.put("name", AttributeValue.builder().s(user.getName()).build());
        item.put("email", AttributeValue.builder().s(user.getEmail()).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(item)
                .build();

        try {
            dynamoDbClient.putItem(request);
        } catch (DynamoDbException e) {
            throw new RuntimeException("Failed to save user in DynamoDB: " + e.getMessage(), e);
        }

    }

    @Override
    public User getUser(String identification) {
        Map<String, AttributeValue> key = Map.of(
                "id", AttributeValue.builder().s(identification).build()
        );

        GetItemRequest request = GetItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .build();

        try {
            GetItemResponse response = dynamoDbClient.getItem(request);

            if (!response.hasItem() || response.item().isEmpty()) {
                return null;
            }

            Map<String, AttributeValue> item = response.item();
            String name = item.getOrDefault("name", AttributeValue.builder().s("").build()).s();
            String email = item.getOrDefault("email", AttributeValue.builder().s("").build()).s();

            return new User(identification, name, email);

        } catch (DynamoDbException e) {
            throw new RuntimeException("Failed to fetch user from DynamoDB: " + e.getMessage(), e);
        }

    }
}
