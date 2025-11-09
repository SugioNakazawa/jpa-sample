# JPA サンプルプロジェクト

このプロジェクトは、Java Persistence API (JPA)を使用した基本的なCRUD操作のサンプルです。

## プロジェクト構成

```
jpa/
├── pom.xml                              # Maven設定ファイル
├── README.md                            # このファイル
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── example/
        │           ├── Main.java        # メインクラス
        │           ├── entity/          # エンティティクラス
        │           │   ├── User.java
        │           │   └── Product.java
        │           └── dao/             # Data Access Object
        │               ├── UserDao.java
        │               └── ProductDao.java
        └── resources/
            └── META-INF/
                └── persistence.xml      # JPA設定ファイル
```

## 使用技術

- **Java 17**
- **JPA (Jakarta Persistence API) 3.1**
- **Hibernate 6.2.7** - JPA実装
- **H2 Database 2.2.224** - インメモリデータベース
- **Maven** - ビルドツール

## 主な機能

### エンティティ

1. **User (ユーザー)**
   - ID、ユーザー名、メールアドレス、年齢
   - 作成日時、更新日時の自動管理
   - ライフサイクルコールバック (@PrePersist, @PreUpdate)

2. **Product (商品)**
   - ID、商品名、説明、価格、在庫数
   - カテゴリ (Enum型)
   - BigDecimalによる正確な価格管理

### CRUD操作

各エンティティに対して以下の操作が可能:
- **Create** - データの作成
- **Read** - データの取得（ID検索、全件取得、条件検索）
- **Update** - データの更新
- **Delete** - データの削除

### クエリ例

- ユーザー名での検索
- 年齢範囲での検索
- カテゴリでの商品検索
- 価格範囲での商品検索
- 在庫が少ない商品の検索

## セットアップと実行

### 1. プロジェクトのビルド

```bash
mvn clean compile
```

### 2. アプリケーションの実行

```bash
mvn exec:java -Dexec.mainClass="com.example.Main"
```

または、IDEから `Main.java` を直接実行することも可能です。

## データベース設定

このプロジェクトではH2インメモリデータベースを使用しています。
設定は `src/main/resources/META-INF/persistence.xml` で確認できます。

### 主な設定項目

- `hibernate.hbm2ddl.auto=create-drop`: アプリケーション起動時にテーブルを作成、終了時に削除
- `hibernate.show_sql=true`: 実行されるSQLを表示
- `hibernate.format_sql=true`: SQLを整形して表示

## JPAの主要アノテーション

### エンティティ関連
- `@Entity` - エンティティクラスを示す
- `@Table` - テーブル名を指定
- `@Id` - 主キーを示す
- `@GeneratedValue` - 主キーの自動生成戦略を指定
- `@Column` - カラムの詳細設定

### データ型関連
- `@Enumerated` - Enum型のマッピング方法を指定
- `@Temporal` - 日付/時刻型のマッピング（Java 8以降は不要）

### ライフサイクル関連
- `@PrePersist` - エンティティ保存前に実行
- `@PreUpdate` - エンティティ更新前に実行
- `@PostLoad` - エンティティ読み込み後に実行
- `@PreRemove` - エンティティ削除前に実行

## 学習ポイント

1. **EntityManagerFactory と EntityManager の使い分け**
   - EntityManagerFactory: アプリケーション全体で1つ
   - EntityManager: トランザクションごとに作成・破棄

2. **トランザクション管理**
   - `begin()` - トランザクション開始
   - `commit()` - コミット
   - `rollback()` - ロールバック

3. **JPQL (Java Persistence Query Language)**
   - SQLライクなクエリ言語
   - データベース非依存
   - TypedQueryによる型安全なクエリ

4. **エンティティのライフサイクル**
   - New (新規)
   - Managed (管理対象)
   - Detached (切り離し)
   - Removed (削除)

## カスタマイズ

### 異なるデータベースを使用する場合

`persistence.xml` のJDBC設定を変更します:

#### MySQL の例:
```xml
<property name="jakarta.persistence.jdbc.driver" value="com.mysql.cj.jdbc.Driver"/>
<property name="jakarta.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/mydb"/>
<property name="hibernate.dialect" value="org.hibernate.dialect.MySQLDialect"/>
```

#### PostgreSQL の例:
```xml
<property name="jakarta.persistence.jdbc.driver" value="org.postgresql.Driver"/>
<property name="jakarta.persistence.jdbc.url" value="jdbc:postgresql://localhost:5432/mydb"/>
<property name="hibernate.dialect" value="org.hibernate.dialect.PostgreSQLDialect"/>
```

忘れずに `pom.xml` に該当するJDBCドライバーの依存関係を追加してください。

## 参考資料

- [Jakarta Persistence API 公式ドキュメント](https://jakarta.ee/specifications/persistence/)
- [Hibernate Documentation](https://hibernate.org/orm/documentation/)
- [H2 Database Documentation](https://www.h2database.com/)

## ライセンス

このサンプルプロジェクトは学習目的で自由に使用できます。
