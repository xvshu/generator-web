package com.el.test.web.pubserver.realm;

import com.eloancn.framework.sevice.api.MessageStatus;
import com.eloancn.framework.sevice.api.ResultDTO;
import com.eloancn.framework.utils.PropertiesUtil;
import com.eloancn.organ.api.RightService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Set;

/**
 * shiro realm
 * Created by qinxf on 2018/3/2.
 */
public class PubCasRealm extends AuthorizingRealm {

    private static final String SYSTEM_CODE = PropertiesUtil.getProperties("system.code");
    private RightService rightService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String userId = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        ResultDTO<Set<String>> rights = rightService.getAuthorizationInfo(Integer.valueOf(userId),SYSTEM_CODE);
        if(rights != null && rights.getData() != null && rights.getStatus() == MessageStatus.SUCCESS.getValue()){
            authorizationInfo.setStringPermissions(rights.getData());
        }

        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken casToken = (UsernamePasswordToken) token;
        if (token == null) {
            return null;
        }
        return new SimpleAuthenticationInfo(casToken.getUsername(), casToken.getPassword() ,getName());
    }

    public RightService getRightService() {
        return rightService;
    }

    public void setRightService(RightService rightService) {
        this.rightService = rightService;
    }
}
