package org.example.domain.usecase;

import org.example.domain.exception.BadRequestException;
import org.example.domain.model.User;
import org.example.domain.port.api.ISaveUserServicePort;
import org.example.domain.port.spi.IUserPersistencePort;
import org.example.infrastructure.exceptionhandler.ExceptionResponse;

import java.util.Map;


public class SaveUserUseCase implements ISaveUserServicePort {

    private final IUserPersistencePort persistencePort;

    public SaveUserUseCase(IUserPersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    @Override
    public void saveUser(User user) {

        if (user == null || user.getIdentification() == null || user.getName() == null || user.getEmail() == null) {
            throw new BadRequestException(ExceptionResponse.BAD_USER , Map.of("", ""));
        }
        
        persistencePort.save(user);
    }
}
