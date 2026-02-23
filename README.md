# Employee Qualification App

Spring Boot + Thymeleaf + JPA を使った社員・資格管理アプリです。

## Features
- 社員一覧表示
- 社員検索
- 社員詳細表示
- 資格一覧表示
- 資格に紐づく社員検索
- 社員削除機能（ROLE_ADMIN のみ）

---

## Current Specification（現在の仕様）

### 権限制御
- ROLE_ADMIN：削除可能
- ROLE_USER：閲覧のみ

### 削除機能
- 一覧画面から削除可能
- ADMIN ログイン時のみ削除ボタン表示
- セキュリティ制限あり

### 一覧表示仕様
- 検索ワードにより表示内容が変化
- 見出しロジックは改善予定（Issue参照）

---

## Future Plans（今後の予定）
- 編集モードの追加
- 管理画面（/admin/employees）の分離
- UI整合性の改善

---

## Tech Stack
- Java 17
- Spring Boot
- Thymeleaf
- Spring Data JPA
- H2 Database

## How to Run
```bash
./mvnw spring-boot:run
