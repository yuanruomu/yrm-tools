package cn.yrm.tools.common.service;

import java.util.List;

public interface ISysUserApi {
    List<String> queryUserRoles(String sysUserId);

}
