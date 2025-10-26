package homework.second.service;

import homework.second.dao.UserDao;
import homework.second.model.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserDao dao;

    public UserService(UserDao dao) {
        this.dao = dao;
    }

    public UserEntity createUser(String name, String email, Integer age) {
        UserEntity userEntity = new UserEntity(name, email, age);

        return dao.create(userEntity);
    }

    public UserEntity getUser(Long id) {
        return dao.findById(id);
    }

    public List<UserEntity> listUsers() {
        return dao.findAll();
    }

    public UserEntity updateUser(Long id, String name, String email, Integer age) {
        UserEntity userEntity = dao.findById(id);
        if (userEntity == null) return null;
        if (name != null && !name.isBlank()) userEntity.setName(name);
        if (email != null && !email.isBlank()) userEntity.setEmail(email);
        if (age != null) userEntity.setAge(age);

        return dao.update(userEntity);
    }

    public boolean deleteUser(Long id) {
        return dao.delete(id);
    }
}
