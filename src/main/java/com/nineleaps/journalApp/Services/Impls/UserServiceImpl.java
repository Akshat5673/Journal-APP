package com.nineleaps.journalApp.Services.Impls;

import com.nineleaps.journalApp.Dao.UserRepositoryDao;
import com.nineleaps.journalApp.Entities.User;
import com.nineleaps.journalApp.Enums.Role;
import com.nineleaps.journalApp.Exceptions.AlreadyExistsException;
import com.nineleaps.journalApp.Exceptions.ResourceNotFoundException;
import com.nineleaps.journalApp.Repositories.UserRepository;
import com.nineleaps.journalApp.Scheduler.UserScheduler;
import com.nineleaps.journalApp.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;

    private final PasswordEncoder passwordEncoder;

    private final UserRepositoryDao dao;

    private final UserScheduler scheduler;

    @Autowired
    public UserServiceImpl(UserRepository userRepo, PasswordEncoder passwordEncoder, UserRepositoryDao dao, UserScheduler scheduler) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.dao = dao;
        this.scheduler = scheduler;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User retrieve(String id) {
        return userRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User","Id", id));
    }

    @Override
    public User create(User user) {
        if(userRepo.existsByName(user.getName().trim())){
            throw new AlreadyExistsException("User","Name", user.getName());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ADMIN_USER);
        return userRepo.save(user);
    }

    @Override
    public void saveNewUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.NORMAL_USER);
        userRepo.save(user);
    }

    @Override
    public User update(String id, User user) {
        User oldUser = userRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User","Id", id));

        oldUser.setName(user.getName());
        oldUser.setPassword(passwordEncoder.encode(user.getPassword()));
        oldUser.setJournalEntries(user.getJournalEntries());
        oldUser.setEmail(user.getEmail());
        oldUser.setSentimentAnalysisOpted(user.isSentimentAnalysisOpted());
        return userRepo.save(oldUser);
    }

    @Override
    public void delete(String id) {
        User user = userRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User","Id", id));
        userRepo.delete(user);
    }

    @Override
    public List<User> getUsersForAnalysis(){
        return dao.getUsersForSentimentAnalysis();
    }

    @Override
    public void sendSentimentMail(){
        scheduler.fetchUsersAndSendMail();
    }

}
