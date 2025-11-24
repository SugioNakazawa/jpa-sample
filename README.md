# Tsurugi JPA サンプルプロジェクト

Tsurugi の JDBC ドライバーと Hibernate 方言を使用して、JPA を利用した基本的な CRUD 操作を行うサンプルプロジェクトです。

## Tsurugiで注意点
JPA + Hibernate を使用して Tsurugi データベースに接続する際、以下の点に注意してください。
- Hibernate による自動DDL生成機能はサポートされていません。DDLは手動で作成してください。
  - `hibernate.hbm2ddl.auto` の設定は `none` にしてください。
- カラムの自動採番を使用する方法
  - エンティティクラス
    - `@GeneratedValue(strategy = GenerationType.AUTO)`
  - エンティティテーブル
    - `CREATE TABLE users ( id BIGINT PRIMARY KEY, ... );`
    - GENERATED ALWAYS AS IDENTITY は使用しないで作成します。
  - 採番テーブル
    - `CREATE TABLE users_SEQ ( next_val BIGINT );`
    - `INSERT INTO users_SEQ ( next_val ) VALUES ( 1 );`

## プロジェクト構成

```
jpa-sample/
├── build.gradle                         # Gradle設定ファイル
├── settings.gradle                      # Gradleプロジェクト設定
├── gradlew                              # Gradle Wrapper (Unix/Mac)
├── gradlew.bat                          # Gradle Wrapper (Windows)
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
            ├── ddl/                     # DDLスクリプト（Tsurugi用）
            │   ├── create_products_SEQ.sql
            │   ├── create_products.sql
            │   ├── create_users_SEQ.sql
            │   └── create_users.sql
            └── META-INF/
                └── persistence.xml      # JPA設定ファイル
```

## 使用技術

- **Java 17**
- **JPA (Jakarta Persistence API) 3.2**
- **Hibernate 7.1.4** - JPA実装
- **Tsurugi 0.1.0** - Tsurugiデータベース対応
- **Gradle 8.5** - ビルドツール
- **PostgreSQL 42.7.3** - PostgreSQLデータベース対応
- **H2 Database 2.2.224** - H2データベース対応

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

### 前提条件

Tsurugiデータベースが必要です。Dockerで起動する場合：

```bash
# コンテナ起動
docker container run -d -p 12345:12345 --name tsurugi1 ghcr.io/project-tsurugi/tsurugidb

# テーブル作成
tgsql --script -c tcp://localhost:12345 src/main/resources/ddl/create_products.sql
tgsql --script -c tcp://localhost:12345 src/main/resources/ddl/create_products_SEQ.sql
tgsql --script -c tcp://localhost:12345 src/main/resources/ddl/insert_products_SEQ.sql

tgsql --script -c tcp://localhost:12345 src/main/resources/ddl/create_users.sql
tgsql --script -c tcp://localhost:12345 src/main/resources/ddl/create_users_SEQ.sql
tgsql --script -c tcp://localhost:12345 src/main/resources/ddl/insert_users_SEQ.sql
```

### 1. プロジェクトのビルド

```bash
./gradlew clean build
```

### 2. アプリケーションの実行

```bash
./gradlew run
```

または、IDEから `Main.java` を直接実行することも可能です。

### 3. その他の便利なコマンド

```bash
# 依存関係の確認
./gradlew dependencies

# テストの実行
./gradlew test

# クリーンビルド
./gradlew clean build --no-daemon
```

## データベース設定

このプロジェクトは複数のデータベースに対応しています。
設定は `src/main/resources/META-INF/persistence.xml` で切り替えます。

### デフォルト設定（Tsurugi）

```xml
<property name="jakarta.persistence.jdbc.driver" value="com.tsurugidb.jdbc.TsurugiDriver"/>
<property name="jakarta.persistence.jdbc.url" value="jdbc:tsurugi:tcp://localhost:12345"/>
<property name="jakarta.persistence.jdbc.user" value="user"/>
<property name="jakarta.persistence.jdbc.password" value="password"/>
<property name="hibernate.dialect" value="com.tsurugidb.hibernate.TsurugiDialect"/>
```

### H2、 PostgreSQL に切り替える場合
- persistence.xml の変更
  - `hibernate.hbm2ddl.auto` の設定を `create-drop` に変更。（テーブル自動作成）
  - コメントアウトの切り替え。
- エンティティクラスの変更
  -  `@GeneratedValue(strategy = GenerationType.IDENTITY)` に変更。

### 主な設定項目

- `hibernate.hbm2ddl.auto=none`: アプリケーション起動時にテーブルを作成、終了時に削除を行わない。
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

## 対応データベース

### Tsurugi（デフォルト）
```xml
<property name="jakarta.persistence.jdbc.driver" value="com.tsurugidb.jdbc.TsurugiDriver"/>
<property name="jakarta.persistence.jdbc.url" value="jdbc:tsurugi:tcp://localhost:12345"/>
<property name="hibernate.dialect" value="com.tsurugidb.hibernate.TsurugiDialect"/>
```

### PostgreSQL（デフォルト）
```xml
<property name="jakarta.persistence.jdbc.driver" value="org.postgresql.Driver"/>
<property name="jakarta.persistence.jdbc.url" value="jdbc:postgresql://localhost:5432/jpa_sample"/>
<property name="hibernate.dialect" value="org.hibernate.dialect.PostgreSQLDialect"/>
```

### H2（インメモリ）
```xml
<property name="jakarta.persistence.jdbc.driver" value="org.h2.Driver"/>
<property name="jakarta.persistence.jdbc.url" value="jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1"/>
<property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect"/>
```

新しいデータベースを追加する場合は、`build.gradle`に該当するJDBCドライバーの依存関係を追加してください。

## 参考資料

- [Tsurugi JDBC 公式ドキュメント](https://github.com/project-tsurugi/tsurugi-jdbc?tab=readme-ov-file)
- [Jakarta Persistence API 公式ドキュメント](https://jakarta.ee/specifications/persistence/)
- [Hibernate Documentation](https://hibernate.org/orm/documentation/)
- [H2 Database Documentation](https://www.h2database.com/)

## GenerationType.IDENTITY でのエラー原因

Tsurugi JDBC ドライバーは `getGeneratedKeys` メソッドをサポートしていないため、`GenerationType.IDENTITY` を使用するとエラーが発生します。
Hibernate は insert 文の実行後に 以下の SQL 発行します。

```sql
select
    currval('users_id_seq')
```

## ライセンス

このサンプルプロジェクトは学習目的で自由に使用できます。
