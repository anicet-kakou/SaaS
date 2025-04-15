package com.devolution.saas.insurance.common.i18n;

import com.devolution.saas.common.i18n.AbstractModuleMessageProvider;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Fournisseur de messages pour le module Insurance.
 */
@Component
public class InsuranceMessageProvider extends AbstractModuleMessageProvider {

    private static final String MODULE_NAME = "insurance";
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
        return "com/devolution/saas/insurance/common/i18n";
    }

    @Override
    public boolean canHandle(String code) {
        // Le module Insurance peut gérer les codes qui commencent par "insurance."
        // mais pas ceux qui sont spécifiques à un sous-module
        if (code.startsWith(MODULE_NAME + ".")) {
            // Vérifier si le code est spécifique à un sous-module
            String[] submodules = {"auto", "home", "life"};
            for (String submodule : submodules) {
                if (code.startsWith(MODULE_NAME + "." + submodule + ".")) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }
}
