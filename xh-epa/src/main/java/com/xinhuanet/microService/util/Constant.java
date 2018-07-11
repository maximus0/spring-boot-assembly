package com.xinhuanet.microService.util;

/**
 * 常量定义
 */
public class Constant {
    /**
     * 未认证 初始状态,不含修改待审核;
     */
    public static final Integer USER_NO_PASS = 0;

    /**
     * 企业,机构,媒体认证标识 通过认证;
     */
    public static final Integer USER_ENTERPRISE_INSTITUTION_MEDIA_PASS = 1;

    /**
     * 个人认证实名 通过认证;
     */
    public static final Integer USER_PERSONALREALNAME_PASS = 11;

    /**
     * 个人认证身份 通过认证;
     */
    public static final Integer USER_PERSONALIDENTITY_PASS = 12;

    public static String USER_SERVICE_KEY_AUTHCODE = "authCode";

    public static String USER_SERVICE_KEY_REALNAME = "realName";

    public static String USER_SERVICE_KEY_AVATAR_50 = "avatar50";

    public static String USER_SERVICE_KEY_AVATAR_100 = "avatar100";

    public static String USER_SERVICE_KEY_AVATAR_120 = "avatar120";

    public static String USER_SERVICE_KEY_AVATAR_180 = "avatar180";

    public static String USER_SERVICE_KEY_INDUSTRYCODE = "industryCode";

    public static String USER_SERVICE_KEY_EMAIL = "email";

    public static String USER_SERVICE_KEY_LOCATION = "location";

    public static String USER_SERVICE_KEY_BIRTHDAY = "birthday";

    public static String USER_SERVICE_KEY_SEX = "genderCode";

}
