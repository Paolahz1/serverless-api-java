package org.example.domain.port.spi;


import org.example.domain.model.User;

public interface IUserPersistencePort {
    void save(User user);
    User getUser(String identification);
}
