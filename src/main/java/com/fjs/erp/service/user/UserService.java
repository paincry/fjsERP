package com.fjs.erp.service.user;

import com.fjs.erp.constans.BusinessConstants;
import com.fjs.erp.datasource.entities.User;
import com.fjs.erp.datasource.mappers.UserMapper;
import com.fjs.erp.datasource.mappers.UserMapperEx;
import com.fjs.erp.exception.LogException;
import com.fjs.erp.utils.ExceptionCodeConstants;
import jakarta.annotation.Resource;
import com.fjs.erp.datasource.entities.UserExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private Logger logger = LoggerFactory.getLogger(UserService.class);
    @Resource
    private UserMapper userMapper;

    @Resource
    private UserMapperEx userMapperEx;


    public User getUser(long id)throws Exception {
        User result=null;
        try{
            result=userMapper.selectByPrimaryKey(id);
        }catch(Exception e){
            LogException.readFail(logger, e);
        }
        return result;
    }

    public List<User> getUser()throws Exception {
        UserExample example = new UserExample();
        List<User> list=null;
        try{
            list=userMapper.selectByExample(example);
        }catch(Exception e){
            LogException.readFail(logger, e);
        }
        return list;
    }

    public List<User> select(String userName, String loginName, int offset, int rows)throws Exception {
        List<User> list=null;
        try{
            list=userMapperEx.selectByConditionUser(userName, loginName, offset, rows);
        }catch(Exception e){
            LogException.readFail(logger, e);
        }
        return list;
    }

    public Long countUser(String userName, String loginName)throws Exception {
        Long result=null;
        try{
            result=userMapperEx.countsByUser(userName, loginName);
        }catch(Exception e){
            LogException.readFail(logger, e);
        }
        return result;
    }


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

    public User getUserByUserName(String username)throws Exception {
        UserExample example = new UserExample();
        example.createCriteria().andLoginameEqualTo(username).andStatusEqualTo(BusinessConstants.USER_STATUS_NORMAL);
        List<User> list=null;
        try{
            list= userMapper.selectByExample(example);
        }catch(Exception e){
            LogException.readFail(logger, e);
        }
        User user =null;
        if(list!=null&&list.size()>0){
            user = list.get(0);
        }
        return user;
    }
}
