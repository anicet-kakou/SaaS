package com.devolution.saas.common.abstracts;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service abstrait pour les opérations de lecture.
 * Fournit une configuration de transaction standardisée pour les opérations de lecture.
 */
public abstract class AbstractReadService {

    /**
     * Configuration de transaction par défaut pour les opérations de lecture.
     * - readOnly = true : Optimise les performances pour les opérations de lecture.
     * - propagation = Propagation.SUPPORTS : Utilise une transaction existante si elle existe, sinon s'exécute sans transaction.
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    protected void executeReadOperation() {
        // Cette méthode est destinée à être surchargée par les classes concrètes
        // Elle définit simplement la configuration de transaction par défaut
    }
}
