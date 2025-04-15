package com.devolution.saas.core.security.i18n;

import com.devolution.saas.common.i18n.AbstractModuleMessageProvider;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Fournisseur de messages pour le module Security.
 */
@Component
public class SecurityMessageProvider extends AbstractModuleMessageProvider {

    private static final String MODULE_NAME = "security";
    private static final List<String> MESSAGE_TYPES = Arrays.asList("errors", "validation", "notifications");

    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }

    @Override
    public List<String> getMessageTypes() {
        return MESSAGE_TYPES;
    }

    @Override
    protected String getBasePath() {
        return "com/devolution/saas/core/security/i18n";
    }

    @Override
    public boolean canHandle(String code) {
        // Le module Security peut gérer les codes qui commencent par "security."
        // ou qui sont liés à la sécurité
        if (code.startsWith(MODULE_NAME + ".")) {
            return true;
        }

        // Vérifier si le code est lié à la sécurité
        String[] securityPrefixes = {"user.", "role.", "permission.", "api.key."};
        for (String prefix : securityPrefixes) {
            if (code.startsWith(prefix)) {
                return true;
            }
        }

        return false;
    }
}
