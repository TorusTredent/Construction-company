package by.constructioncompany.service;

import by.constructioncompany.dao.jdbc.JdbcUserDao;
import by.constructioncompany.entity.person.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private JdbcUserDao userDao;

    public boolean save (User user) {
        return userDao.save(user);
    }

    public boolean update (User user, String email) {
        return userDao.update(user, email);
    }

    public boolean delete (String email) {
        return userDao.delete(email);
    }

    public boolean existByEmail (String email) {
        return userDao.existByEmail(email);
    }

    public boolean existByPhoneNumber (String phoneNumber) {
        return userDao.existByPhoneNumber(phoneNumber);
    }

    public User findByEmail (String email) {
        return userDao.findByEmail(email).orElse(null);
    }

    public List<User> findUsers () {
        return userDao.findUsers().orElse(null);
    }
}
