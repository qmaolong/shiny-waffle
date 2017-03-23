package com.covilla.common;

/**
 * Created by LongLongAgo on 2016/8/23.
 */
public enum ModuleEnum {
    shop("shop", "门店管理", 1, 1, null, null),
    product("product", "菜品管理", 1, 1, null, null),
    staff("staff", "员工管理", 1, 1, null, null),
    trans("trans", "收银管理", 1, 1, null, null),
    card("card", "会员卡管理", 1, 1, null, null),
    shopList("shopList", "门店列表", 2, 1, "shop", "/shop/shopList"),;

    private String moduleCode;

    private String moduleName;//模块名

    private Integer displayLevel;//显示级别

    private Integer authLevel;//权限等级

    private String parantCode;//上级码

    private String url;

    private ModuleEnum(String moduleCode, String moduleName, Integer displayLevel, Integer authLevel, String parantCode, String url){
        this.moduleCode = moduleCode;
        this.moduleName = moduleName;
        this.displayLevel = displayLevel;
        this.authLevel = authLevel;
        this.parantCode = parantCode;
        this.url = url;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Integer getAuthLevel() {
        return authLevel;
    }

    public void setAuthLevel(Integer authLevel) {
        this.authLevel = authLevel;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public Integer getDisplayLevel() {
        return displayLevel;
    }

    public void setDisplayLevel(Integer displayLevel) {
        this.displayLevel = displayLevel;
    }

    public String getParantCode() {
        return parantCode;
    }

    public void setParantCode(String parantCode) {
        this.parantCode = parantCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
