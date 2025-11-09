package com.example;

import com.example.dao.ProductDao;
import com.example.dao.UserDao;
import com.example.entity.Product;
import com.example.entity.Product.Category;
import com.example.entity.User;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.math.BigDecimal;
import java.util.List;

/**
 * JPAサンプルアプリケーションのメインクラス
 */
public class Main {

    public static void main(String[] args) {
        // EntityManagerFactoryの作成
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-sample-pu");

        try {
            // DAOのインスタンス化
            UserDao userDao = new UserDao(emf);
            ProductDao productDao = new ProductDao(emf);

            System.out.println("=== JPAサンプルアプリケーション ===\n");

            // ユーザーのCRUD操作デモ
            demoUserOperations(userDao);

            System.out.println("\n" + "=".repeat(50) + "\n");

            // 商品のCRUD操作デモ
            demoProductOperations(productDao);

        } finally {
            // EntityManagerFactoryのクローズ
            emf.close();
        }
    }

    /**
     * ユーザー操作のデモ
     */
    private static void demoUserOperations(UserDao userDao) {
        System.out.println("【ユーザー操作デモ】\n");

        // 1. ユーザーの作成
        System.out.println("1. ユーザーを作成:");
        User user1 = new User("yamada_taro", "yamada@example.com", 25);
        User user2 = new User("sato_hanako", "sato@example.com", 30);
        User user3 = new User("tanaka_jiro", "tanaka@example.com", 35);

        userDao.create(user1);
        userDao.create(user2);
        userDao.create(user3);
        System.out.println("  ✓ 3人のユーザーを作成しました\n");

        // 2. 全ユーザーの取得
        System.out.println("2. 全ユーザーを取得:");
        List<User> allUsers = userDao.findAll();
        allUsers.forEach(u -> System.out.println("  - " + u));
        System.out.println();

        // 3. IDでユーザーを検索
        System.out.println("3. ID=1のユーザーを検索:");
        userDao.findById(1L).ifPresent(u -> System.out.println("  - " + u));
        System.out.println();

        // 4. ユーザー名で検索
        System.out.println("4. ユーザー名で検索 (yamada_taro):");
        userDao.findByUsername("yamada_taro").ifPresent(u -> System.out.println("  - " + u));
        System.out.println();

        // 5. 年齢範囲で検索
        System.out.println("5. 年齢範囲で検索 (25-32歳):");
        List<User> usersInRange = userDao.findByAgeRange(25, 32);
        usersInRange.forEach(u -> System.out.println("  - " + u.getUsername() + " (年齢: " + u.getAge() + ")"));
        System.out.println();

        // 6. ユーザーの更新
        System.out.println("6. ユーザー情報を更新:");
        userDao.findById(1L).ifPresent(user -> {
            user.setAge(26);
            user.setEmail("yamada_updated@example.com");
            userDao.update(user);
            System.out.println("  ✓ ユーザーを更新しました: " + user);
        });
        System.out.println();

        // 7. ユーザーの削除
        System.out.println("7. ユーザーを削除 (ID=3):");
        userDao.delete(3L);
        System.out.println("  ✓ ユーザーを削除しました");
        System.out.println("  削除後のユーザー数: " + userDao.findAll().size());
    }

    /**
     * 商品操作のデモ
     */
    private static void demoProductOperations(ProductDao productDao) {
        System.out.println("【商品操作デモ】\n");

        // 1. 商品の作成
        System.out.println("1. 商品を作成:");
        Product product1 = new Product("ノートパソコン", "高性能なノートPC", 
                                       new BigDecimal("120000.00"), 10, Category.ELECTRONICS);
        Product product2 = new Product("Javaプログラミング入門", "Java学習書", 
                                       new BigDecimal("3500.00"), 25, Category.BOOKS);
        Product product3 = new Product("Tシャツ", "コットン100%", 
                                       new BigDecimal("2000.00"), 5, Category.CLOTHING);
        Product product4 = new Product("スマートフォン", "最新モデル", 
                                       new BigDecimal("80000.00"), 15, Category.ELECTRONICS);

        productDao.create(product1);
        productDao.create(product2);
        productDao.create(product3);
        productDao.create(product4);
        System.out.println("  ✓ 4つの商品を作成しました\n");

        // 2. 全商品の取得
        System.out.println("2. 全商品を取得:");
        List<Product> allProducts = productDao.findAll();
        allProducts.forEach(p -> System.out.println("  - " + p.getName() + " (¥" + p.getPrice() + ")"));
        System.out.println();

        // 3. カテゴリで検索
        System.out.println("3. カテゴリで検索 (ELECTRONICS):");
        List<Product> electronics = productDao.findByCategory(Category.ELECTRONICS);
        electronics.forEach(p -> System.out.println("  - " + p.getName() + " (在庫: " + p.getStock() + ")"));
        System.out.println();

        // 4. 価格範囲で検索
        System.out.println("4. 価格範囲で検索 (¥2000-¥50000):");
        List<Product> productsInRange = productDao.findByPriceRange(
                new BigDecimal("2000.00"), new BigDecimal("50000.00"));
        productsInRange.forEach(p -> System.out.println("  - " + p.getName() + " (¥" + p.getPrice() + ")"));
        System.out.println();

        // 5. 在庫が少ない商品を検索
        System.out.println("5. 在庫が10個以下の商品:");
        List<Product> lowStockProducts = productDao.findLowStockProducts(10);
        lowStockProducts.forEach(p -> System.out.println("  - " + p.getName() + " (在庫: " + p.getStock() + ")"));
        System.out.println();

        // 6. 商品の更新
        System.out.println("6. 商品を更新:");
        productDao.findById(1L).ifPresent(product -> {
            product.setPrice(new BigDecimal("115000.00"));
            product.setStock(8);
            productDao.update(product);
            System.out.println("  ✓ 商品を更新しました: " + product.getName());
        });
        System.out.println();

        // 7. 商品の削除
        System.out.println("7. 商品を削除 (ID=3):");
        productDao.delete(3L);
        System.out.println("  ✓ 商品を削除しました");
        System.out.println("  削除後の商品数: " + productDao.findAll().size());
    }
}
