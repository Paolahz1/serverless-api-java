package org.example.domain.port.api;


import org.example.domain.model.User;

public interface IFetchUserServicePort {

    User fetchUser(String identification);

}
