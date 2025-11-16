package homework.second.dao;

import homework.second.model.UserEntity;

import java.util.List;

public interface UserDao {

    /**
     * Сохраняет новую сущность пользователя в базе данных.
     *
     * @param user сущность пользователя, которая будет сохранена.
     * @return сохранённая сущность пользователя с присвоенным идентификатором.
     */
    UserEntity create(UserEntity user);

    /**
     * Находит пользователя по его уникальному идентификатору.
     *
     * @param id уникальный идентификатор пользователя.
     * @return Optional, который может содержать найденного пользователя или быть пустым.
     */
    UserEntity findById(Long id);

    /**
     * Получает список всех пользователей из базы данных.
     *
     * @return список всех пользователей.
     */
    List<UserEntity> findAll();

    /**
     * Обновляет существующего пользователя в базе данных.
     *
     * @param user сущность пользователя, содержащая обновленные данные.
     * @return обновлённая сущность пользователя.
     */
    UserEntity update(UserEntity user);

    /**
     * Удаляет пользователя из базы данных.
     *
     * @param id уникальный идентификатор пользователя, которого нужно удалить.
     */
    boolean delete(Long id);
}
