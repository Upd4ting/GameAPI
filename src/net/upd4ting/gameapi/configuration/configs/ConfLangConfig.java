package net.upd4ting.gameapi.configuration.configs;

import net.upd4ting.gameapi.configuration.Configuration;

public class ConfLangConfig extends Configuration {

    public ConfLangConfig() {
        super(ConfType.ENABLE, "configlang.yml");
    }

    @Override
    public void loadData() {
    }
}
