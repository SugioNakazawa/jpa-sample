package com.example.dao;

import com.example.entity.Product;
import com.example.entity.Product.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * 商品のDAO（Data Access Object）
 */
public class ProductDao {

    private final EntityManagerFactory entityManagerFactory;

    public ProductDao(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    /**
     * 商品を作成
     */
    public Product create(Product product) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(product);
            em.getTransaction().commit();
            return product;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * IDで商品を検索
     */
    public Optional<Product> findById(Long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            Product product = em.find(Product.class, id);
            return Optional.ofNullable(product);
        } finally {
            em.close();
        }
    }

    /**
     * 全商品を取得
     */
    public List<Product> findAll() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Product> query = em.createQuery("SELECT p FROM Product p", Product.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * カテゴリで商品を検索
     */
    public List<Product> findByCategory(Category category) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Product> query = em.createQuery(
                "SELECT p FROM Product p WHERE p.category = :category", Product.class);
            query.setParameter("category", category);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * 価格範囲で商品を検索
     */
    public List<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Product> query = em.createQuery(
                "SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice ORDER BY p.price", 
                Product.class);
            query.setParameter("minPrice", minPrice);
            query.setParameter("maxPrice", maxPrice);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * 商品を更新
     */
    public Product update(Product product) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            Product updatedProduct = em.merge(product);
            em.getTransaction().commit();
            return updatedProduct;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * 商品を削除
     */
    public void delete(Long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            Product product = em.find(Product.class, id);
            if (product != null) {
                em.remove(product);
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
     * 在庫が指定数以下の商品を検索
     */
    public List<Product> findLowStockProducts(int threshold) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Product> query = em.createQuery(
                "SELECT p FROM Product p WHERE p.stock <= :threshold ORDER BY p.stock", Product.class);
            query.setParameter("threshold", threshold);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
