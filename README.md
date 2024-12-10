# Subscription Management System

A web-based subscription management system that helps users track their subscription services, renewal dates, and fees. This project uses **Kakao Social Login** for user authentication and is designed for both general users and administrators.

## Features

### General User
- View and manage personal subscriptions.
- Input renewal dates for subscriptions.
- Receive notifications for upcoming subscription renewals.

### Administrator
- Manage subscription services (add, edit, delete).
- View all user subscriptions.

### Common Features
- Responsive web application for desktop and mobile.
- User authentication via **Kakao Social Login**.

---

## Technology Stack

### Backend
- **Spring Boot 3.3**
    - REST API for subscription management.
    - JPA for database interaction.
    - Spring Security with OAuth2 for Kakao login.

### Frontend
- **React**
    - Responsive UI for general users and administrators.
    - Kakao Login button integration.

### Database
- **MariaDB**
    - Stores user, subscription, and user-subscription mapping data.

### Infrastructure
- **AWS**
    - EC2: Backend server hosting.
    - RDS: MariaDB instance.
    - S3 & CloudFront: Frontend static assets hosting.

### CI/CD
- **GitHub Actions**
    - Automated build, test, and deployment pipeline.

---

## Database Schema

```MariaDB
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    kakao_id BIGINT NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    profile_image_url VARCHAR(255),
    nickname VARCHAR(50)
);

CREATE TABLE subscriptions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    service_name VARCHAR(100) NOT NULL,
    fee DECIMAL(10, 2) NOT NULL,
    description VARCHAR(255)
);

CREATE TABLE user_subscriptions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    subscription_id INT,
    renewal_date DATE,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (subscription_id) REFERENCES subscriptions(id)
);

```