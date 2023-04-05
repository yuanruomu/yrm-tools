package cn.yrm.tools.common.service;

public interface IConfigManager {
    String getSetting(String configKey);
    <T> T getSetting(String configKey, Class<T> clz);
    void updateSiteSetting(String configKey, String configValue);
}
