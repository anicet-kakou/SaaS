# Guide d'Architecture Hexagonale

## Introduction

Ce document décrit les principes et les règles à suivre pour respecter l'architecture hexagonale (ou architecture en
ports et adaptateurs) dans notre projet.

## Principes de l'Architecture Hexagonale

L'architecture hexagonale, également connue sous le nom d'architecture en ports et adaptateurs, est un modèle
architectural qui permet de séparer clairement les préoccupations dans une application. Elle est basée sur les principes
suivants:

1. **Indépendance du domaine**: Le domaine métier est au centre de l'architecture et ne dépend d'aucune couche externe.
2. **Inversion de dépendance**: Les dépendances pointent vers l'intérieur, vers le domaine.
3. **Ports et adaptateurs**: Les ports définissent les interfaces que le domaine utilise pour communiquer avec
   l'extérieur, et les adaptateurs implémentent ces interfaces.

## Structure du Projet

Notre projet est organisé selon les couches suivantes:

### 1. Couche Domaine (`domain`)

La couche domaine contient la logique métier pure de l'application. Elle est indépendante de toute infrastructure ou
détail technique.

#### 1.1 Modèles (`domain.model`)

Les entités et objets de valeur qui représentent les concepts métier.

```java
package com.devolution.saas.insurance.nonlife.auto.domain.model;

public class Vehicle {
    // Propriétés et méthodes
}
```

#### 1.2 Ports (`domain.port`)

Interfaces définissant les besoins du domaine pour communiquer avec l'extérieur.

```java
package com.devolution.saas.insurance.nonlife.auto.domain.port;

public interface VehicleProvider {
    Optional<Vehicle> findVehicleById(UUID id, UUID organizationId);
}
```

#### 1.3 Services de Domaine (`domain.service`)

Services contenant la logique métier pure.

```java
package com.devolution.saas.insurance.nonlife.auto.domain.service;

public interface PricingCalculator {
    BigDecimal calculateBasePremium(Vehicle vehicle, CoverageType coverageType);
}
```

### 2. Couche Application (`application`)

La couche application orchestre les cas d'utilisation en coordonnant les appels aux services de domaine.

#### 2.1 Services d'Application (`application.service`)

Services qui orchestrent les cas d'utilisation.

```java
package com.devolution.saas.insurance.nonlife.auto.application.service;

@Service
@Transactional
public class AutoPricingServiceImpl implements AutoPricingService {
    private final VehicleProvider vehicleProvider;
    private final PricingCalculator pricingCalculator;
    
    // Méthodes qui délèguent la logique métier aux services de domaine
}
```

#### 2.2 DTOs (`application.dto`)

Objets de transfert de données utilisés pour communiquer avec la couche API.

```java
package com.devolution.saas.insurance.nonlife.auto.application.dto;

public record VehicleDTO(UUID id, String registrationNumber, /* ... */) {
}
```

#### 2.3 Mappers (`application.mapper`)

Convertisseurs entre les entités du domaine et les DTOs.

```java
package com.devolution.saas.insurance.nonlife.auto.application.mapper;

@Component
public class VehicleMapper {
    public VehicleDTO toDto(Vehicle vehicle) {
        // Conversion
    }
}
```

### 3. Couche Infrastructure (`infrastructure`)

La couche infrastructure contient les détails techniques et les implémentations des ports définis dans le domaine.

#### 3.1 Adaptateurs (`infrastructure.adapter`)

Implémentations des ports définis dans le domaine.

```java
package com.devolution.saas.insurance.nonlife.auto.infrastructure.adapter;

@Component
public class VehicleProviderAdapter implements VehicleProvider {
    private final VehicleRepository vehicleRepository;
    
    // Implémentation des méthodes du port
}
```

#### 3.2 Persistence (`infrastructure.persistence`)

Repositories JPA et autres détails de persistance.

```java
package com.devolution.saas.insurance.nonlife.auto.infrastructure.persistence;

@Repository
public interface JpaVehicleRepository extends JpaRepository<Vehicle, UUID>, VehicleRepository {
    // Méthodes de repository
}
```

### 4. Couche API (`api`)

La couche API expose les fonctionnalités de l'application au monde extérieur.

```java
package com.devolution.saas.insurance.nonlife.auto.api;

@RestController
@RequestMapping("/api/v1/auto/vehicles")
public class VehicleController {
    private final VehicleService vehicleService;
    
    // Endpoints REST
}
```

## Règles à Suivre

Pour respecter l'architecture hexagonale, suivez ces règles:

### 1. Dépendances

- Le domaine ne doit dépendre d'aucune autre couche
- L'application peut dépendre du domaine, mais pas de l'infrastructure ou de l'API
- L'infrastructure peut dépendre du domaine et de l'application
- L'API peut dépendre du domaine et de l'application, mais pas de l'infrastructure

### 2. Services de Domaine

- Doivent contenir uniquement la logique métier pure
- Ne doivent pas dépendre des repositories directement
- Doivent utiliser des ports pour accéder aux données
- Ne doivent pas utiliser `@Transactional`

### 3. Services d'Application

- Doivent orchestrer les cas d'utilisation
- Doivent déléguer la logique métier aux services de domaine
- Peuvent utiliser `@Transactional`
- Doivent gérer la conversion entre DTOs et entités

### 4. Adaptateurs

- Doivent implémenter les ports définis dans le domaine
- Doivent encapsuler les détails d'infrastructure

## Exemples

### Exemple 1: Validation d'une Entité

**Incorrect**:

```java
@Service
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository vehicleRepository;
    
    public void createVehicle(Vehicle vehicle) {
        // Validation directe dans le service d'application
        if (vehicle.getRegistrationNumber() == null) {
            throw new IllegalArgumentException("Registration number is required");
        }
        
        vehicleRepository.save(vehicle);
    }
}
```

**Correct**:

```java
// Service de domaine
@Service
public class VehicleValidatorImpl implements VehicleValidator {
    public List<String> validate(Vehicle vehicle) {
        List<String> errors = new ArrayList<>();
        if (vehicle.getRegistrationNumber() == null) {
            errors.add("Registration number is required");
        }
        return errors;
    }
}

// Service d'application
@Service
@Transactional
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final VehicleValidator vehicleValidator;
    
    public void createVehicle(Vehicle vehicle) {
        // Délégation de la validation au service de domaine
        List<String> errors = vehicleValidator.validate(vehicle);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
        
        vehicleRepository.save(vehicle);
    }
}
```

### Exemple 2: Accès aux Données

**Incorrect**:

```java
@Service
public class PolicyValidatorImpl implements PolicyValidator {
    private final VehicleRepository vehicleRepository;
    
    public List<String> validateReferences(Policy policy) {
        List<String> errors = new ArrayList<>();
        if (!vehicleRepository.findById(policy.getVehicleId()).isPresent()) {
            errors.add("Vehicle not found");
        }
        return errors;
    }
}
```

**Correct**:

```java
// Port dans le domaine
public interface VehicleProvider {
    boolean vehicleExists(UUID id, UUID organizationId);
}

// Service de domaine
@Service
public class PolicyValidatorImpl implements PolicyValidator {
    private final VehicleProvider vehicleProvider;
    
    public List<String> validateReferences(Policy policy, UUID organizationId) {
        List<String> errors = new ArrayList<>();
        if (!vehicleProvider.vehicleExists(policy.getVehicleId(), organizationId)) {
            errors.add("Vehicle not found");
        }
        return errors;
    }
}

// Adaptateur dans l'infrastructure
@Component
public class VehicleProviderAdapter implements VehicleProvider {
    private final VehicleRepository vehicleRepository;
    
    public boolean vehicleExists(UUID id, UUID organizationId) {
        return vehicleRepository.findById(id)
                .filter(v -> v.getOrganizationId().equals(organizationId))
                .isPresent();
    }
}
```

## Conclusion

L'architecture hexagonale nous permet de créer un code plus maintenable, testable et évolutif. En suivant ces principes
et règles, nous pouvons garantir que notre domaine métier reste pur et indépendant des détails techniques, ce qui
facilite les tests et les évolutions futures.
