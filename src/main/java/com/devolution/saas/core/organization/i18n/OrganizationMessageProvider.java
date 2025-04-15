package com.devolution.saas.core.organization.i18n;

import com.devolution.saas.common.i18n.AbstractModuleMessageProvider;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Fournisseur de messages pour le module Organization.
 */
@Component
public class OrganizationMessageProvider extends AbstractModuleMessageProvider {

    private static final String MODULE_NAME = "organization";
    private static final List<String> MESSAGE_TYPES = Arrays.asList("errors", "validation");

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
        return "com/devolution/saas/core/organization/i18n";
    }

    @Override
    public boolean canHandle(String code) {
        // Le module Organization peut gérer les codes qui commencent par "organization." 
        // ou qui sont liés à l'organisation
        if (code.startsWith(MODULE_NAME + ".")) {
            return true;
        }

        // Vérifier si le code est lié à l'organisation
        String[] organizationPrefixes = {"organization.", "hierarchy."};
        for (String prefix : organizationPrefixes) {
            if (code.startsWith(prefix)) {
                return true;
            }
        }

        return false;
    }
}
