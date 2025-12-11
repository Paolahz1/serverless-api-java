package org.example.infrastructure.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.domain.model.User;
import org.example.domain.port.api.ISaveUserServicePort;
import org.example.domain.port.spi.IUserPersistencePort;
import org.example.domain.usecase.SaveUserUseCase;
import org.example.infrastructure.output.adapter.UserDynamoAdapter;

import java.util.Map;

public class SaveUserHandler implements RequestHandler<Map<String, Object>, APIGatewayProxyResponseEvent> {

    private final ISaveUserServicePort saveUserServicePort;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SaveUserHandler() {
        IUserPersistencePort persistencePort = new UserDynamoAdapter();
        this.saveUserServicePort = new SaveUserUseCase(persistencePort);
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(Map<String, Object> input, Context context) {
        try {
            String body = (String) input.get("body");

            User user = objectMapper.readValue(body, User.class);

            if (user.getIdentification() == null || user.getName() == null || user.getEmail() == null) {
                return new APIGatewayProxyResponseEvent()
                        .withStatusCode(400)
                        .withBody("{\"error\": \"identification, name, and email are required\"}")
                        .withHeaders(Map.of("Content-Type", "application/json"));
            }

            saveUserServicePort.saveUser(user);

            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(201)
                    .withBody("{\"message\": \"User created successfully\"}")
                    .withHeaders(Map.of("Content-Type", "application/json"));

        } catch (Exception e) {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(500)
                    .withBody("{\"error\": \"Internal server error\"}")
                    .withHeaders(Map.of("Content-Type", "application/json"));
        }
    }
}
