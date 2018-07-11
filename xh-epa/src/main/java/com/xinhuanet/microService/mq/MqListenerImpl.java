package com.xinhuanet.microService.mq;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.xinhuanet.censor.api.config.WhitelistService;
import com.xinhuanet.microService.util.Constant;
import com.xinhuanet.microService.util.MethodUtils;
import com.xinhuanet.user.common.exception.XhucException;
import com.xinhuanet.user.dubbo.service.UserService;
import com.xinhuanet.user.model.AdministrativeDivision;

/**
 * Created by conanca on 15-6-3.
 */
public class MqListenerImpl implements MqListener {
    private static Logger logger = LoggerFactory.getLogger(MqListenerImpl.class);

    @Autowired
    private UserService userService;
    @Autowired
    private WhitelistService whitelistService;

    @Override
    public void receive(@SuppressWarnings("rawtypes") Map message) throws Exception {
        logger.info("===从队列中获取到消息：" + JSON.toJSONString(message));
        handlewhitelist(message);// 处理企业认证白名单
        handleMessageUserCensor(message);
    }

    private void handlewhitelist(Map message) throws Exception {
        String flag = message.get("newState") + "";
        if (message.get("authEntity") != null) {
            HashMap<String, Object> authEntity = (HashMap<String, Object>) message.get("authEntity");
            HashMap<String, Object> para = new HashMap<String, Object>();
            if ("11".equals(flag) || "12".equals(flag)) {// 通过认证
                if (authEntity.get("userId") != null) {
                    Integer userId = (Integer) authEntity.get("userId");
                    para.put("userId", userId);
                    para.put("auth", "1");
                    whitelistService.save(para);
                }
            } else if ("7".equals(flag)) {// 取消认证
                if (authEntity.get("userId") != null) {
                    Integer userId = (Integer) authEntity.get("userId");
                    para.put("userId", userId);
                   whitelistService.delWhiteListByUserIdANDappId(para);//暂时注销，等dubbo重新打包再放开
                }
            }
        }

    }

    private void handleMessageUserCensor(Map message) throws XhucException {
        Integer num = MethodUtils.getMethodNum(message);
        if (num != null) {
            switch (num) {
            case 1:
                handleMessage1(message);
                break;
            case 2:
                handleMessage2(message);
                break;
            case 3:
                handleMessage3(message);
                break;
            case 4:
                handleMessage4(message);
                break;
            case 5:
                handleMessage5(message);
                break;
            case 6:
                handleMessage6(message);
                break;
            case 7:
                handleMessage7(message);
                break;
            case 8:
                handleMessage8(message);
                break;
            }
        }
    }

    /**
     * 企业 、机构、 媒体 更新状态
     * 
     * @throws XhucException
     */
    private void handleMessage1(Map message) throws XhucException {
        updateEnterpriseIndustryMedia(message, true);
    }

    /**
     * 个人实名 更新状态
     * 
     * @throws XhucException
     */
    private void handleMessage2(Map message) throws XhucException {
        updatePersonalRealname(message, true);
    }

    /**
     * 个人身份
     * 
     * @throws XhucException
     */
    private void handleMessage3(Map message) throws XhucException {
        Map<String, Object> authEntity = (Map<String, Object>) message.get("authEntity");

        Map<String, Object> updateUserMap = new HashMap<String, Object>();
        updateUserMap.put(Constant.USER_SERVICE_KEY_AUTHCODE, Constant.USER_PERSONALIDENTITY_PASS);

        userService.updateUser((Integer) authEntity.get("userId"), updateUserMap);
    }

    /**
     * 企业 、机构、 媒体 不更新状态
     * 
     * @throws XhucException
     */
    private void handleMessage4(Map message) throws XhucException {
        updateEnterpriseIndustryMedia(message, false);
    }

    /**
     * 个人实名 不更新状态
     * 
     * @throws XhucException
     */
    private void handleMessage5(Map message) throws XhucException {
        updatePersonalRealname(message, false);
    }

    private void handleMessage6(Map message) throws XhucException {
        Map<String, Object> authEntity = (Map<String, Object>) message.get("authEntity");

        Map<String, Object> updateUserMap = new HashMap<String, Object>();
        updateUserMap.put(Constant.USER_SERVICE_KEY_AUTHCODE, Constant.USER_NO_PASS);

        userService.updateUser((Integer) authEntity.get("userId"), updateUserMap);
    }

    private void handleMessage7(Map message) throws XhucException {
        Map<String, Object> authEntity = (Map<String, Object>) message.get("authEntity");

        Map<String, Object> updateUserMap = new HashMap<String, Object>();
        updateUserMap.put(Constant.USER_SERVICE_KEY_AUTHCODE, Constant.USER_NO_PASS);

        userService.updateUser((Integer) authEntity.get("userId"), updateUserMap);
    }

    private void handleMessage8(Map message) throws XhucException {
        Map<String, Object> authEntity = (Map<String, Object>) message.get("authEntity");

        Map<String, Object> updateUserMap = new HashMap<String, Object>();
        updateUserMap.put(Constant.USER_SERVICE_KEY_AUTHCODE, Constant.USER_PERSONALREALNAME_PASS);

        userService.updateUser((Integer) authEntity.get("userId"), updateUserMap);
    }

    private void updateEnterpriseIndustryMedia(Map message, boolean flag) throws XhucException {
        Map<String, Object> authEntity = (Map<String, Object>) message.get("authEntity");
        String authType = (String) authEntity.get("authType");

        Map<String, Object> updateUserMap = new HashMap<String, Object>();
        if (flag) {
            updateUserMap.put(Constant.USER_SERVICE_KEY_AUTHCODE, Constant.USER_ENTERPRISE_INSTITUTION_MEDIA_PASS);
        }
        updateUserMap.put(Constant.USER_SERVICE_KEY_AVATAR_50, authEntity.get("logo50"));
        updateUserMap.put(Constant.USER_SERVICE_KEY_AVATAR_100, authEntity.get("logo100"));
        updateUserMap.put(Constant.USER_SERVICE_KEY_AVATAR_120, authEntity.get("logo120"));
        updateUserMap.put(Constant.USER_SERVICE_KEY_AVATAR_180, authEntity.get("logo180"));
        updateUserMap.put(Constant.USER_SERVICE_KEY_EMAIL, authEntity.get("email"));
        updateUserMap.put(Constant.USER_SERVICE_KEY_REALNAME, authEntity.get("nameChi"));
        if ("authEnterprise".equals(authType)) {
            updateUserMap.put(Constant.USER_SERVICE_KEY_INDUSTRYCODE, authEntity.get("classIndustry_02"));
        }

        AdministrativeDivision administrativeDivision = new AdministrativeDivision();
        administrativeDivision.setCountryCode((String) authEntity.get("country"));
        administrativeDivision.setProvinceCode((String) authEntity.get("province"));
        administrativeDivision.setCityCode((String) authEntity.get("city"));
        administrativeDivision.setDistrictCode((String) authEntity.get("county"));
        updateUserMap.put(Constant.USER_SERVICE_KEY_LOCATION, administrativeDivision);

        userService.updateUser((Integer) authEntity.get("userId"), updateUserMap);
    }

    private void updatePersonalRealname(Map message, boolean flag) throws XhucException {
        Map<String, Object> authEntity = (Map<String, Object>) message.get("authEntity");

        Map<String, Object> updateUserMap = new HashMap<String, Object>();
        if (flag) {
            updateUserMap.put(Constant.USER_SERVICE_KEY_AUTHCODE, Constant.USER_PERSONALREALNAME_PASS);
        }
        updateUserMap.put(Constant.USER_SERVICE_KEY_REALNAME, authEntity.get("realname"));
        updateUserMap.put(Constant.USER_SERVICE_KEY_BIRTHDAY, authEntity.get("birthday"));
        updateUserMap.put(Constant.USER_SERVICE_KEY_SEX, authEntity.get("sex"));

        AdministrativeDivision administrativeDivision = new AdministrativeDivision();
        administrativeDivision.setCountryCode((String) authEntity.get("country"));
        administrativeDivision.setProvinceCode((String) authEntity.get("province"));
        administrativeDivision.setCityCode((String) authEntity.get("city"));
        administrativeDivision.setDistrictCode((String) authEntity.get("county"));
        updateUserMap.put(Constant.USER_SERVICE_KEY_LOCATION, administrativeDivision);

        userService.updateUser((Integer) authEntity.get("userId"), updateUserMap);
    }

}
