package org.example.domain.usecase;

import org.example.domain.exception.NotFoundException;
import org.example.domain.model.User;
import org.example.domain.port.api.IFetchUserServicePort;
import org.example.domain.port.spi.IUserPersistencePort;
import org.example.infrastructure.exceptionhandler.ExceptionResponse;

import java.util.Map;


public class FetchUserUseCase implements IFetchUserServicePort {

    private final IUserPersistencePort persistencePort;

    public FetchUserUseCase(IUserPersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    @Override
    public User fetchUser(String identification) {
        User response = persistencePort.getUser(identification);

        if(response == null){
            throw new NotFoundException(ExceptionResponse.USER_NOT_FOUND,
                    Map.of("User not found with this id", identification));
        }

        return response;
    }
}
