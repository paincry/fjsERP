package com.fjs.erp.controller;

import com.fjs.erp.constans.BusinessConstants;
import com.fjs.erp.datasource.entities.Tenant;
import com.fjs.erp.datasource.entities.User;
import com.fjs.erp.service.log.LogService;
import com.fjs.erp.service.tenant.TenantService;
import com.fjs.erp.service.user.UserService;
import com.fjs.erp.utils.BaseResponseInfo;
import com.fjs.erp.utils.ExceptionCodeConstants;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    //日志记录器
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserService userService;

    @Resource
    private LogService logService;

    @Resource
    private TenantService tenantService;


    @PostMapping(value = "/login")
    public BaseResponseInfo login(@RequestParam(value = "loginName", required = false) String loginName,
                                  @RequestParam(value = "password", required = false) String password,
                                  HttpServletRequest request)throws Exception {
        logger.info("**************用户登录 login 方法开始*****************");
        String msgTip = "";
        User user=null;
        BaseResponseInfo res = new BaseResponseInfo();
        try {
            String username = loginName.trim();
            password = password.trim();
            //判断用户是否已经登录过，登录过不再处理
            Object userInfo = request.getSession().getAttribute("user");
            User sessionUser = new User();
            if (userInfo != null) {
                sessionUser = (User) userInfo;
            }
            if (sessionUser != null && username.equalsIgnoreCase(sessionUser.getLoginame())) {
                logger.info("====用户 " + username + "已经登录过, login 方法调用结束====");
                msgTip = "user already login";
            }
            //获取用户状态
            int userStatus = -1;
            try {
                request.getSession().removeAttribute("tenantId");
                userStatus = userService.validateUser(username, password);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(">>>>>>>>>>>>>用户  " + username + " 登录 login 方法 访问服务层异常====", e);
                msgTip = "access service exception";
            }
            switch (userStatus) {
                case ExceptionCodeConstants.UserExceptionCode.USER_NOT_EXIST:
                    msgTip = "user is not exist";
                    break;
                case ExceptionCodeConstants.UserExceptionCode.USER_PASSWORD_ERROR:
                    msgTip = "user password error";
                    break;
                case ExceptionCodeConstants.UserExceptionCode.BLACK_USER:
                    msgTip = "user is black";
                    break;
                case ExceptionCodeConstants.UserExceptionCode.USER_ACCESS_EXCEPTION:
                    msgTip = "access service error";
                    break;
                default:
                    try {
                        msgTip = "user can login";
                        //验证通过 ，可以登录，放入session，记录登录日志
                        user = userService.getUserByUserName(username);
                        request.getSession().setAttribute("user",user);
                        if(user.getTenantId()!=null) {
                            Tenant tenant = tenantService.getTenantByTenantId(user.getTenantId());
                            if(tenant!=null) {
                                Long tenantId = tenant.getTenantId();
                                Integer userNumLimit = tenant.getUserNumLimit();
                                Integer billsNumLimit = tenant.getBillsNumLimit();
                                if(tenantId!=null) {
                                    request.getSession().setAttribute("tenantId",tenantId); //租户tenantId
                                    request.getSession().setAttribute("userNumLimit",userNumLimit); //用户限制数
                                    request.getSession().setAttribute("billsNumLimit",billsNumLimit); //单据限制数
                                }
                            }
                        }
                        logService.insertLog("用户",
                                new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_LOGIN).append(user.getId()).toString(),
                                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error(">>>>>>>>>>>>>>>查询用户名为:" + username + " ，用户信息异常", e);
                    }
                    break;
            }
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("msgTip", msgTip);
            /**
             * 在IE模式下，无法获取到user数据，
             * 在此处明确添加上user信息
             * */
            if(user!=null){
                data.put("user",user);
            }
            res.code = 200;
            res.data = data;
            logger.info("**************用户登录 login 方法结束*****************");
        } catch(Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            res.code = 500;
            res.data = "用户登录失败";
        }
        return res;
    }
}
