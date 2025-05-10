package com.example.services;

import com.example.entities.User;

public interface UserService extends CrudService<User, Long> {

    boolean checkPassword(Long userId, String plainPassword);

}
