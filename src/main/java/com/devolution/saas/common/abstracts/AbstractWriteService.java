package com.devolution.saas.common.abstracts;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service abstrait pour les opérations d'écriture.
 * Fournit une configuration de transaction standardisée pour les opérations d'écriture.
 */
public abstract class AbstractWriteService {

    /**
     * Configuration de transaction par défaut pour les opérations d'écriture.
     * - readOnly = false : Indique que la méthode modifie des données.
     * - propagation = Propagation.REQUIRED : S'assure que la méthode s'exécute dans une transaction.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    protected void executeWriteOperation() {
        // Cette méthode est destinée à être surchargée par les classes concrètes
        // Elle définit simplement la configuration de transaction par défaut
    }
}
