package com.fjs.erp.service.user;

import com.fjs.erp.datasource.entities.User;
import com.fjs.erp.datasource.mappers.UserMapper;
import jakarta.annotation.Resource;
import com.fjs.erp.datasource.entities.UserExample;

import java.util.List;

public class UserService {

    @Resource
    private UserMapper userMapper;

    public int validateUser(String username, String password) throws Exception {
        /**默认是可以登录的*/
        List<User> list = null;
        try {
            UserExample example = new UserExample();
            example.createCriteria().andLoginameEqualTo(username).andStatusEqualTo(BusinessConstants.USER_STATUS_NORMAL);
            list = userMapper.selectByExample(example);
        } catch (Exception e) {
            logger.error(">>>>>>>>访问验证用户姓名是否存在后台信息异常", e);
            return ExceptionCodeConstants.UserExceptionCode.USER_ACCESS_EXCEPTION;
        }

        if (null != list && list.size() == 0) {
            return ExceptionCodeConstants.UserExceptionCode.USER_NOT_EXIST;
        }

        try {
            UserExample example = new UserExample();
            example.createCriteria().andLoginameEqualTo(username).andPasswordEqualTo(password)
                    .andStatusEqualTo(BusinessConstants.USER_STATUS_NORMAL);
            list = userMapper.selectByExample(example);
        } catch (Exception e) {
            logger.error(">>>>>>>>>>访问验证用户密码后台信息异常", e);
            return ExceptionCodeConstants.UserExceptionCode.USER_ACCESS_EXCEPTION;
        }

        if (null != list && list.size() == 0) {
            return ExceptionCodeConstants.UserExceptionCode.USER_PASSWORD_ERROR;
        }
        return ExceptionCodeConstants.UserExceptionCode.USER_CONDITION_FIT;
    }
}
