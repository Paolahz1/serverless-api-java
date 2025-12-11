package org.example.infrastructure.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.domain.model.User;
import org.example.domain.port.api.IFetchUserServicePort;
import org.example.domain.port.spi.IUserPersistencePort;
import org.example.domain.usecase.FetchUserUseCase;
import org.example.infrastructure.output.adapter.UserDynamoAdapter;

import java.util.Map;

public class GetUserHandler implements RequestHandler<Map<String, Object>, APIGatewayProxyResponseEvent> {

    private final IFetchUserServicePort fetchUserServicePort;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GetUserHandler() {
        IUserPersistencePort persistencePort = new UserDynamoAdapter();
        this.fetchUserServicePort = new FetchUserUseCase(persistencePort);
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(Map<String, Object> input, Context context) {

        try {
            Map<String, String> pathParams = (Map<String, String>) input.get("pathParameters");

            if (pathParams == null || !pathParams.containsKey("id")) {
                return new APIGatewayProxyResponseEvent()
                        .withStatusCode(400)
                        .withBody("{\"error\": \"Missing path parameter: id\"}")
                        .withHeaders(Map.of("Content-Type", "application/json"));
            }

            String id = pathParams.get("id");
            User user = fetchUserServicePort.fetchUser(id);

            if (user == null) {
                return new APIGatewayProxyResponseEvent()
                        .withStatusCode(404)
                        .withBody("{\"error\": \"User not found\"}")
                        .withHeaders(Map.of("Content-Type", "application/json"));
            }

            String responseBody = objectMapper.writeValueAsString(user);

            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withBody(responseBody)
                    .withHeaders(Map.of("Content-Type", "application/json"));

        } catch (Exception e) {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(500)
                    .withBody("{\"error\": \"Internal server error\"}")
                    .withHeaders(Map.of("Content-Type", "application/json"));
        }
    }
}
