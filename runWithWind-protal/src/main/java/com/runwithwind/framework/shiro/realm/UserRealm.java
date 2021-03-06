package com.runwithwind.framework.shiro.realm;

import com.runwithwind.common.exception.user.*;
import com.runwithwind.common.utils.security.ShiroUtils;
import com.runwithwind.framework.shiro.service.LoginService;
import com.runwithwind.project.portal.user.domain.RunUser;
import com.runwithwind.project.system.role.service.IRoleService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义Realm 处理登录 权限
 *
 * @author runwithwind
 */
public class UserRealm extends AuthorizingRealm {
  private static final Logger log = LoggerFactory.getLogger(UserRealm.class);

  @Autowired private IRoleService roleService;

  @Autowired private LoginService loginService;

  /** 授权 */
  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
    RunUser user = ShiroUtils.getSysUser();

    // 角色列表
    Set<String> roles = new HashSet<String>();
    // 功能列表
    Set<String> menus = new HashSet<String>();
    SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
    // 暂时去掉管理员拥有所有权限，如需要给管理员全部权限，打开下边注释
    /*if (user.isAdmin())
    {
        info.addRole("admin");
        info.addStringPermission("*:*:*");
    }
    else
    {*/
    // roles = roleService.selectRoleKeys(user.getUserId());
    // 角色加入AuthorizationInfo认证对象
    /* info.setRoles(roles);
    // 权限加入AuthorizationInfo认证对象
    info.setStringPermissions(menus);*/
    /* }*/
    return info;
  }

  /** 登录认证 */
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
      throws AuthenticationException {
    UsernamePasswordToken upToken = (UsernamePasswordToken) token;
    String username = upToken.getUsername();
    String password = "";
    if (upToken.getPassword() != null) {
      password = new String(upToken.getPassword());
    }

    RunUser user = null;
    try {
      user = loginService.login(username, password);
    } catch (CaptchaException e) {
      throw new AuthenticationException(e.getMessage(), e);
    } catch (UserNotExistsException e) {
      throw new UnknownAccountException(e.getMessage(), e);
    } catch (UserPasswordNotMatchException e) {
      throw new IncorrectCredentialsException(e.getMessage(), e);
    } catch (UserPasswordRetryLimitExceedException e) {
      throw new ExcessiveAttemptsException(e.getMessage(), e);
    } catch (UserBlockedException e) {
      throw new LockedAccountException(e.getMessage(), e);
    } catch (RoleBlockedException e) {
      throw new LockedAccountException(e.getMessage(), e);
    } catch (Exception e) {
      log.info("对用户[" + username + "]进行登录验证..验证未通过{}", e.getMessage());
      throw new AuthenticationException(e.getMessage(), e);
    }
    SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, password, getName());
    return info;
  }

  /** 清理缓存权限 */
  public void clearCachedAuthorizationInfo() {
    this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
  }
}
