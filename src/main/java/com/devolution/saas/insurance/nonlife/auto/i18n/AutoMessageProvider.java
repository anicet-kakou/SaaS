package com.devolution.saas.insurance.nonlife.auto.i18n;

import com.devolution.saas.common.i18n.AbstractModuleMessageProvider;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Fournisseur de messages pour le module Auto.
 */
@Component
public class AutoMessageProvider extends AbstractModuleMessageProvider {

    private static final String MODULE_NAME = "auto";
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
        return "com/devolution/saas/insurance/nonlife/auto/i18n";
    }

    @Override
    public boolean canHandle(String code) {
        // Le module Auto peut gérer les codes qui commencent par "insurance.auto."
        return code.startsWith("insurance." + MODULE_NAME + ".");
    }
}
