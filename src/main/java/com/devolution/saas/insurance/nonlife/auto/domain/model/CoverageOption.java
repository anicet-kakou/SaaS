package com.devolution.saas.insurance.nonlife.auto.domain.model;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

/**
 * Enum représentant les options de couverture additionnelles disponibles pour une police auto.
 */
public enum CoverageOption {
    GLASS_COVERAGE("glassCoverage", new BigDecimal("80.00"), "Bris de glace"),
    ASSISTANCE_COVERAGE("assistanceCoverage", new BigDecimal("120.00"), "Assistance routière"),
    DRIVER_COVERAGE("driverCoverage", new BigDecimal("150.00"), "Protection du conducteur");

    private final String key;
    private final BigDecimal premium;
    private final String description;

    CoverageOption(String key, BigDecimal premium, String description) {
        this.key = key;
        this.premium = premium;
        this.description = description;
    }

    /**
     * Trouve une option de couverture par sa clé.
     *
     * @param key la clé à rechercher
     * @return l'option de couverture correspondante, si elle existe
     */
    public static Optional<CoverageOption> fromKey(String key) {
        return Arrays.stream(values())
                .filter(option -> option.key.equals(key))
                .findFirst();
    }

    /**
     * Retourne la clé de l'option de couverture.
     *
     * @return la clé
     */
    public String getKey() {
        return key;
    }

    /**
     * Retourne le montant de la prime pour cette option.
     *
     * @return le montant de la prime
     */
    public BigDecimal getPremium() {
        return premium;
    }

    /**
     * Retourne la description de l'option de couverture.
     *
     * @return la description
     */
    public String getDescription() {
        return description;
    }
}
