package cn.yrm.tools.common.service;

public interface IConfigManager {
    /**
     * 获得某个配置项
     * @param configKey
     * @return
     */
    String getSetting(String configKey);
    /**
     * 获得某个配置项
     * @param configKey
     * @return
     */
    <T> T getSetting(String configKey, Class<T> clz);
    /**
     * 更新某个配置项的缓存
     * @param configKey
     * @param configValue
     * @Param configName
     */
    boolean updateSiteSetting(String configKey, String configValue, String configName, String description);
    /**
     * 获得某个配置组
     * @param clz
     * @param <T>
     * @return
     */
    <T> T getSettingGroup(Class<T> clz);

}
