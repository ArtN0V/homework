package homework.second.service;

import homework.second.dao.UserDao;
import homework.second.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserDao dao = new UserDao();

    public User createUser(String name, String email, Integer age) {
        User user = new User(name, email, age);

        return dao.create(user);
    }

    public User getUser(Long id) {
        return dao.findById(id);
    }

    public List<User> listUsers() {
        return dao.findAll();
    }

    public User updateUser(Long id, String name, String email, Integer age) {
        User user = dao.findById(id);
        if (user == null) return null;
        if (name != null && !name.isBlank()) user.setName(name);
        if (email != null && !email.isBlank()) user.setEmail(email);
        if (age != null) user.setAge(age);

        return dao.update(user);
    }

    public boolean deleteUser(Long id) {
        return dao.delete(id);
    }
}
