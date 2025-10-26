package homework.second.dao;

import homework.second.model.UserEntity;
import homework.second.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserPostgrsDao implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserPostgrsDao.class);

    @Override
    public UserEntity create(UserEntity userEntity) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(userEntity);
            transaction.commit();

            return userEntity;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Ошибка при создании пользователя", e);

            throw e;
        }
    }

    @Override
    public UserEntity findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.find(UserEntity.class, id);
        } catch (HibernateException e) {
            logger.error("Ошибка при поиске пользователя по id", e);
            throw e;
        }
    }

    @Override
    public List<UserEntity> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from User", UserEntity.class).list();
        } catch (HibernateException e) {
            logger.error("Ошибка при получении списка пользователей", e);
            throw e;
        }
    }

    @Override
    public UserEntity update(UserEntity userEntity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(userEntity);
            transaction.commit();

            return userEntity;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Ошибка при обновлении пользователя", e);

            throw e;
        }
    }

    @Override
    public boolean delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            UserEntity userEntity = session.find(UserEntity.class, id);
            if (userEntity == null) {
                return false;
            }
            transaction = session.beginTransaction();
            session.remove(userEntity);
            transaction.commit();

            return true;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Ошибка при удалении пользователя", e);

            throw e;
        }
    }
}
