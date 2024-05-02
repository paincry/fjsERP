package com.fjs.erp.service.user;

import com.fjs.erp.service.ICommonQuery;
import com.fjs.erp.utils.Constants;
import com.fjs.erp.utils.StringUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service(value = "user_component")
@UserResource
public class UserComponent implements ICommonQuery {

    @Resource
    private UserService userService;

    @Override
    public Object selectOne(Long id) throws Exception {
        return userService.getUser(id);
    }

    @Override
    public List<?> select(Map<String, String> map)throws Exception {
        return getUserList(map);
    }

    private List<?> getUserList(Map<String, String> map)throws Exception {
        String search = map.get(Constants.SEARCH);
        String userName = StringUtil.getInfo(search, "userName");
        String loginName = StringUtil.getInfo(search, "loginName");
        String order = QueryUtils.order(map);
        String filter = QueryUtils.filter(map);
        return userService.select(userName, loginName, QueryUtils.offset(map), QueryUtils.rows(map));
    }

    @Override
    public Long counts(Map<String, String> map)throws Exception {
        String search = map.get(Constants.SEARCH);
        String userName = StringUtil.getInfo(search, "userName");
        String loginName = StringUtil.getInfo(search, "loginName");
        return userService.countUser(userName, loginName);
    }

    @Override
    public int insert(String beanJson, HttpServletRequest request)throws Exception {
        return userService.insertUser(beanJson, request);
    }

    @Override
    public int update(String beanJson, Long id, HttpServletRequest request)throws Exception {
        return userService.updateUser(beanJson, id, request);
    }

    @Override
    public int delete(Long id, HttpServletRequest request)throws Exception {
        return userService.deleteUser(id, request);
    }

    @Override
    public int batchDelete(String ids, HttpServletRequest request)throws Exception {
        return userService.batchDeleteUser(ids, request);
    }

    @Override
    public int checkIsNameExist(Long id, String name)throws Exception {
        return userService.checkIsNameExist(id, name);
    }

