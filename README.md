# Spring Boot JWT Authentication with PostgreSQL

Bu proje, Spring Boot kullanılarak geliştirilmiş bir JWT (JSON Web Token) tabanlı kimlik doğrulama sistemi örneğidir. PostgreSQL veritabanı kullanılmaktadır.

## Özellikler

- JWT tabanlı kimlik doğrulama
- Kullanıcı kaydı ve girişi
- Rol tabanlı yetkilendirme (ADMIN ve USER rolleri)
- Spring Security entegrasyonu
- PostgreSQL veritabanı desteği
- RESTful API endpoints

## Teknolojiler

- Spring Boot
- Spring Security
- JWT (JSON Web Token)
- PostgreSQL
- Maven
- Java 17

## Başlangıç

### Gereksinimler

- Java 17 veya üzeri
- PostgreSQL
- Maven

### Kurulum

1. Projeyi klonlayın:
```bash
git clone [repo-url]
```

2. PostgreSQL veritabanı bağlantı ayarlarını `src/main/resources/application.properties` dosyasında güncelleyin:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. Projeyi derleyin ve çalıştırın:
```bash
mvn spring-boot:run
```

## API Endpoints

### Auth Controller

- `POST /api/auth/register` - Yeni kullanıcı kaydı
- `POST /api/auth/login` - Kullanıcı girişi

### Admin Controller (ADMIN rolü gerektirir)

- `GET /api/admin/dashboard` - Admin kontrol paneli
- `GET /api/admin/users` - Kullanıcı listesi

### User Controller (USER veya ADMIN rolü gerektirir)

- `GET /api/user/profile` - Kullanıcı profili görüntüleme
- `GET /api/user/settings` - Kullanıcı ayarları

### Test Controller

- `GET /api/test` - Test endpoint'i (Authentication gerektirir)

## Kullanım

1. Yeni bir kullanıcı kaydı için:
```bash
curl -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" -d "{\"username\":\"user\",\"password\":\"password\",\"role\":\"USER\"}"
```

2. Giriş yapıp token almak için:
```bash
curl -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d "{\"username\":\"user\",\"password\":\"password\"}"
```

3. Alınan token ile güvenli endpoint'e erişim:
```bash
curl -X GET http://localhost:8080/api/user/profile -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Güvenlik

- Şifreler BCrypt ile hashlenmektedir
- JWT tokenleri güvenli bir şekilde oluşturulmakta ve doğrulanmaktadır
- Spring Security ile rol tabanlı yetkilendirme (ADMIN ve USER rolleri)
- Güvenli endpoint'ler için rol bazlı erişim kontrolleri