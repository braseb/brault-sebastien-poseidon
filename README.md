# Poseidon

[![Java](https://img.shields.io/badge/Java-21-blue)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-Project-orange)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-lightgrey)](LICENSE)

**Poseidon** est une application web sécurisée développée avec **Spring Boot 3**, destinée à la gestion d’entités financières. L’application utilise **JWT** pour l’authentification stateless avec un cookie contenant le token de session et supporte la connexion via **OAuth2 (Google)**, tout en maintenant la gestion des rôles internes en base de données (`ADMIN` et `USER`).

---

## ⚙️ Technologies utilisées

- **Java 21**  
- **Spring Boot 3.5.7** : Web, Security, Data JPA, Validation  
- **JWT** pour l’authentification stateless  
- **OAuth2 (Google)** pour l’authentification externe  
- **MySQL** pour la persistance des données  
- **Thymeleaf** pour le rendu des pages web  
- **Lombok** pour simplifier le code  
- **Maven** pour la gestion du projet  
- **JUnit 5 & Mockito** pour les tests unitaires et d’intégration  

---

## 🗄️ Configuration

### Base de données MySQL

Créez la base de données et l’utilisateur :

```sql
CREATE DATABASE poseidon;
CREATE USER 'user'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON poseidon.* TO 'user'@'localhost';
FLUSH PRIVILEGES;
```

Configurez `application.properties` :

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/poseidon
spring.datasource.username=user
spring.datasource.password=password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### JWT

```properties
jwt.secret=UneCleTresLonguePourHS256DoitFaireAuMoins32Caracteres
```

### OAuth2 Google

```properties
spring.security.oauth2.client.registration.google.client-id=VOTRE_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=VOTRE_CLIENT_SECRET
spring.security.oauth2.client.registration.google.scope=openid,profile,email
```

> L’application utilise son propre JWT pour gérer les rôles, indépendamment de Google OAuth2.

---

## 🧱 Schéma des tables

### `bidlist`

| Colonne         | Type        | Contraintes                   |
|-----------------|------------|-------------------------------|
| id              | TINYINT     | PRIMARY KEY, AUTO_INCREMENT   |
| account         | VARCHAR(30)| NOT NULL                      |
| type            | VARCHAR(30)| NOT NULL                      |
| bid_quantity    | DOUBLE     |                               |
| ask_quantity    | DOUBLE     |                               |
| bid             | DOUBLE     |                               |
| ask             | DOUBLE     |                               |
| benchmark       | VARCHAR(125)|                               |
| bid_list_date   | TIMESTAMP  |                               |
| commentary      | VARCHAR(125)|                               |
| security        | VARCHAR(125)|                               |
| status          | VARCHAR(10)|                               |
| trader          | VARCHAR(125)|                               |
| book            | VARCHAR(125)|                               |
| creation_name   | VARCHAR(125)|                               |
| creation_date   | TIMESTAMP  |                               |
| revision_name   | VARCHAR(125)|                               |
| revision_date   | TIMESTAMP  |                               |
| deal_name       | VARCHAR(125)|                               |
| deal_type       | VARCHAR(125)|                               |
| source_list_id  | VARCHAR(125)|                               |
| side            | VARCHAR(125)|                               |

### `trade`

| Colonne         | Type        | Contraintes                   |
|-----------------|------------|-------------------------------|
| trade_id        | TINYINT     | PRIMARY KEY, AUTO_INCREMENT   |
| account         | VARCHAR(30)| NOT NULL                      |
| type            | VARCHAR(30)| NOT NULL                      |
| buy_quantity    | DOUBLE     |                               |
| sell_quantity   | DOUBLE     |                               |
| buy_price       | DOUBLE     |                               |
| sell_price      | DOUBLE     |                               |
| trade_date      | TIMESTAMP  |                               |
| security        | VARCHAR(125)|                               |
| status          | VARCHAR(10)|                               |
| trader          | VARCHAR(125)|                               |
| benchmark       | VARCHAR(125)|                               |
| book            | VARCHAR(125)|                               |
| creation_name   | VARCHAR(125)|                               |
| creation_date   | TIMESTAMP  |                               |
| revision_name   | VARCHAR(125)|                               |
| revision_date   | TIMESTAMP  |                               |
| deal_name       | VARCHAR(125)|                               |
| deal_type       | VARCHAR(125)|                               |
| source_list_id  | VARCHAR(125)|                               |
| side            | VARCHAR(125)|                               |

### `curvepoint`

| Colonne       | Type       | Contraintes                   |
|---------------|-----------|-------------------------------|
| id            | TINYINT   | PRIMARY KEY, AUTO_INCREMENT   |
| curve_id      | TINYINT   |                               |
| as_of_date    | TIMESTAMP |                               |
| term          | DOUBLE    |                               |
| value         | DOUBLE    |                               |
| creation_date | TIMESTAMP |                               |

### `rating`

| Colonne       | Type       | Contraintes                   |
|---------------|-----------|-------------------------------|
| id            | TINYINT   | PRIMARY KEY, AUTO_INCREMENT   |
| moodys_rating | VARCHAR(125)|                              |
| sand_p_rating | VARCHAR(125)|                              |
| fitch_rating  | VARCHAR(125)|                              |
| order_number  | TINYINT   |                               |

### `rulename`

| Colonne       | Type       | Contraintes                   |
|---------------|-----------|-------------------------------|
| id            | TINYINT   | PRIMARY KEY, AUTO_INCREMENT   |
| name          | VARCHAR(125)|                              |
| description   | VARCHAR(125)|                              |
| json          | VARCHAR(125)|                              |
| template      | VARCHAR(512)|                              |
| sql_str       | VARCHAR(125)|                              |
| sql_part      | VARCHAR(125)|                              |

### `users`

| Colonne   | Type       | Contraintes                   |
|-----------|-----------|-------------------------------|
| id        | TINYINT   | PRIMARY KEY, AUTO_INCREMENT   |
| username  | VARCHAR(125)| NOT NULL, UNIQUE             |
| password  | VARCHAR(125)| NOT NULL                     |
| fullname  | VARCHAR(125)| NOT NULL                     |
| role      | VARCHAR(125)| NOT NULL                     |

Exemple d’insertion initiale :

```sql
insert into users(fullname, username, password, role)
values("Administrator", "admin", "$2a$10$pBV8ILO/s/nao4wVnGLrh.sa/rnr5pDpbeC4E.KNzQWoy8obFZdaa", "ADMIN");

insert into users(fullname, username, password, role)
values("User", "user", "$2a$10$pBV8ILO/s/nao4wVnGLrh.sa/rnr5pDpbeC4E.KNzQWoy8obFZdaa", "USER");
```

---

## 📌 Endpoints principaux

Tous les endpoints sont accessibles via les **contrôleurs MVC** et permettent la gestion complète des entités : création, modification, consultation et suppression.  

- **BidList** : `/bidList/list`, `/bidList/add`, `/bidList/validate`, `/bidList/update/{id}`, `/bidList/delete/{id}`  
- **CurvePoint** : `/curvePoint/list`, `/curvePoint/add`, `/curvePoint/validate`, `/curvePoint/update/{id}`, `/curvePoint/delete/{id}`  
- **Rating** : `/rating/list`, `/rating/add`, `/rating/validate`, `/rating/update/{id}`, `/rating/delete/{id}`  
- **RuleName** : `/ruleName/list`, `/ruleName/add`, `/ruleName/validate`, `/ruleName/update/{id}`, `/ruleName/delete/{id}`  
- **Trade** : `/trade/list`, `/trade/add`, `/trade/validate`, `/trade/update/{id}`, `/trade/delete/{id}`  
- **User** : `/user/list`, `/user/add`, `/user/validate`, `/user/update/{id}`, `/user/delete/{id}`  
- **Login** : `/auth2/google`, `/app/login`, `/app/secure/article-details`, `/app/error`  

---

## 🧪 Tests

- Les **repositories** sont testés avec la base de données réelle pour valider le CRUD complet.  
- Les **services** sont testés avec **Mockito** pour simuler les interactions et garantir la logique métier.  

```bash
mvn test
```

---

## 📄 Licence

MIT License – voir le fichier [LICENSE](LICENSE) pour plus de détails.

