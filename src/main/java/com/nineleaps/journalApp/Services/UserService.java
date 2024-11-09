package com.nineleaps.journalApp.Services;


import com.nineleaps.journalApp.Entities.User;


import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User retrieve(String id);

    User create(User user);

    User update(String id, User user);

    void delete(String id);

    void saveNewUser(User user);

    List<User> getUsersForAnalysis();

    void sendSentimentMail();
}
