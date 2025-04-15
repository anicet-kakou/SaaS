package com.devolution.saas.common.i18n;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Fournisseur de messages pour le module Common.
 */
@Component
public class CommonMessageProvider extends AbstractModuleMessageProvider {

    private static final String MODULE_NAME = "common";
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
        return "com/devolution/saas/common/i18n";
    }

    @Override
    public boolean canHandle(String code) {
        // Le module Common peut gérer les codes qui commencent par "common."
        // ou qui ne commencent pas par un nom de module connu
        if (code.startsWith(MODULE_NAME + ".")) {
            return true;
        }

        // Vérifier si le code ne commence pas par un nom de module connu
        String[] knownModules = {"core", "insurance"};
        for (String module : knownModules) {
            if (code.startsWith(module + ".")) {
                return false;
            }
        }

        // Si le code ne commence pas par un nom de module connu,
        // le module Common peut le gérer
        return true;
    }
}
