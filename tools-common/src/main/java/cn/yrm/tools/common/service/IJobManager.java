package cn.yrm.tools.common.service;

import java.util.Date;

public interface IJobManager {

    void addCronJob(String jobKey, String cronExpression, String className, String parameter);

    void addCronJob(String jobKey, String cronExpression, Class clz, String parameter);

    void addTimeJob(String jobKey, Date runTime, Class clz, String parameter);

    void removeJob(String jobKey);

    void executeJob(String jobKey, String className, String parameter);
}
