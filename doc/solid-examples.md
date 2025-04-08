# Exemples d'Application des Principes SOLID

Ce document présente des exemples concrets d'application des principes SOLID dans notre projet DEVOLUTION SaaS API.

## 1. Principe de Responsabilité Unique (SRP)

### Exemple: Séparation de UserService et UserDetailsService

#### Avant

```java

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Méthodes de gestion des utilisateurs
    public User createUser(CreateUserCommand command) { /* ... */ }

    public User findById(UUID id) { /* ... */ }

    // Méthode pour Spring Security
    @Override
    public UserDetails loadUserByUsername(String username) { /* ... */ }
}
```

#### Après

```java
// Service pour la gestion des utilisateurs
@Service
public class UserService implements UserCreator, UserFinder {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Méthodes de gestion des utilisateurs
    @Override
    public User createUser(CreateUserCommand command) { /* ... */ }

    @Override
    public User findById(UUID id) { /* ... */ }
}

// Service pour l'authentification Spring Security
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserFinder userFinder;

    @Override
    public UserDetails loadUserByUsername(String username) { /* ... */ }
}
```

### Exemple: Séparation de AuthenticationService et TokenService

#### Avant

```java

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    public AuthenticationResult authenticate(LoginCommand command) {
        // Authentification
        // Génération de tokens
    }

    public AuthenticationResult refreshToken(String refreshToken) {
        // Validation du token
        // Génération de nouveaux tokens
    }
}
```

#### Après

```java

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserFinder userFinder;
    private final TokenService tokenService;

    public AuthenticationResult authenticate(LoginCommand command) {
        // Authentification
        // Délégation de la génération de tokens à TokenService
    }
}

@Service
public class TokenService {
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    public TokenPair generateTokens(User user, UserDetails userDetails) {
        // Génération de tokens
    }

    public TokenPair refreshTokens(String refreshToken, User user, UserDetails userDetails) {
        // Validation et rafraîchissement des tokens
    }
}
```

## 2. Principe Ouvert/Fermé (OCP)

### Exemple: Stratégie de Notification

#### Avant

```java
public class NotificationService {
    public void sendNotification(User user, String message, String type) {
        if (type.equals("email")) {
            // Envoyer un email
        } else if (type.equals("sms")) {
            // Envoyer un SMS
        }
        // Pour ajouter un nouveau type, il faut modifier cette classe
    }
}
```

#### Après

```java
public interface NotificationChannel {
    void send(User user, String message);
}

public class EmailNotificationChannel implements NotificationChannel {
    public void send(User user, String message) {
        // Envoyer un email
    }
}

public class SmsNotificationChannel implements NotificationChannel {
    public void send(User user, String message) {
        // Envoyer un SMS
    }
}

// Pour ajouter un nouveau canal, il suffit de créer une nouvelle implémentation
public class PushNotificationChannel implements NotificationChannel {
    public void send(User user, String message) {
        // Envoyer une notification push
    }
}

public class NotificationService {
    private final Map<String, NotificationChannel> channels;

    public NotificationService(Map<String, NotificationChannel> channels) {
        this.channels = channels;
    }

    public void sendNotification(User user, String message, String channelType) {
        NotificationChannel channel = channels.get(channelType);
        if (channel != null) {
            channel.send(user, message);
        }
    }
}
```

## 3. Principe de Substitution de Liskov (LSP)

### Exemple: Repositories

```java
public interface Repository<T> {
    Optional<T> findById(UUID id);

    T save(T entity);

    void deleteById(UUID id);

    List<T> findAll();
}

public class UserRepository implements Repository<User> {
    // Implémentation qui respecte le contrat de Repository<T>
}

public class OrganizationRepository implements Repository<Organization> {
    // Implémentation qui respecte le contrat de Repository<T>
}

// Les deux implémentations peuvent être utilisées de manière interchangeable
public class GenericService<T> {
    private final Repository<T> repository;

    public GenericService(Repository<T> repository) {
        this.repository = repository;
    }

    public T save(T entity) {
        return repository.save(entity);
    }
}
```

## 4. Principe de Ségrégation des Interfaces (ISP)

### Exemple: Interfaces Spécifiques pour UserService

#### Avant

```java
public interface UserService {
    User createUser(CreateUserCommand command);

    User findById(UUID id);

    Optional<User> findByUsername(String username);

    List<User> findAllByOrganizationId(UUID organizationId);

    void deleteUser(UUID id);

    User updateUser(UpdateUserCommand command);
}
```

#### Après

```java
public interface UserCreator {
    User createUser(CreateUserCommand command);
}

public interface UserFinder {
    User findById(UUID id);

    Optional<User> findByUsername(String username);

    List<User> findAllByOrganizationId(UUID organizationId);
}

public interface UserUpdater {
    User updateUser(UpdateUserCommand command);
}

public interface UserDeleter {
    void deleteUser(UUID id);
}

// Les clients n'implémentent que les interfaces dont ils ont besoin
public class UserService implements UserCreator, UserFinder {
    // Implémentation des méthodes nécessaires uniquement
}

public class AdminUserService implements UserCreator, UserFinder, UserUpdater, UserDeleter {
    // Implémentation de toutes les méthodes
}
```

## 5. Principe d'Inversion des Dépendances (DIP)

### Exemple: Dépendance sur des Abstractions

#### Avant

```java
public class UserDetailsServiceImpl {
    private final PostgresUserRepository userRepository;

    public UserDetailsServiceImpl(PostgresUserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```

#### Après

```java
public class UserDetailsServiceImpl {
    private final UserFinder userFinder;

    public UserDetailsServiceImpl(UserFinder userFinder) {
        this.userFinder = userFinder;
    }
}
```

## Conclusion

L'application des principes SOLID dans notre code a permis d'améliorer significativement sa qualité, sa maintenabilité
et sa flexibilité. Ces exemples concrets montrent comment ces principes peuvent être appliqués dans notre contexte
spécifique.
