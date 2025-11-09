package com.example.dao;

import com.example.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * ユーザーのDAO（Data Access Object）
 */
public class UserDao {

    private final EntityManagerFactory entityManagerFactory;

    public UserDao(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    /**
     * ユーザーを作成
     */
    public User create(User user) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            return user;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * IDでユーザーを検索
     */
    public Optional<User> findById(Long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            User user = em.find(User.class, id);
            return Optional.ofNullable(user);
        } finally {
            em.close();
        }
    }

    /**
     * 全ユーザーを取得
     */
    public List<User> findAll() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * ユーザー名でユーザーを検索
     */
    public Optional<User> findByUsername(String username) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.username = :username", User.class);
            query.setParameter("username", username);
            List<User> users = query.getResultList();
            return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
        } finally {
            em.close();
        }
    }

    /**
     * ユーザーを更新
     */
    public User update(User user) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            User updatedUser = em.merge(user);
            em.getTransaction().commit();
            return updatedUser;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * ユーザーを削除
     */
    public void delete(Long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            User user = em.find(User.class, id);
            if (user != null) {
                em.remove(user);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * 年齢範囲でユーザーを検索
     */
    public List<User> findByAgeRange(int minAge, int maxAge) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.age BETWEEN :minAge AND :maxAge ORDER BY u.age", User.class);
            query.setParameter("minAge", minAge);
            query.setParameter("maxAge", maxAge);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
