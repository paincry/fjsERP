package com.fjs.erp.service.log;

import com.alibaba.fastjson.JSONObject;
import com.fjs.erp.utils.Tools;
import com.fjs.erp.datasource.entities.Log;
import com.fjs.erp.datasource.entities.User;
import com.fjs.erp.datasource.mappers.LogMapper;
import com.fjs.erp.exception.LogException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static com.fjs.erp.utils.Tools.getLocalIp;

@Service
public class LogService {
    private Logger logger = LoggerFactory.getLogger(LogService.class);

    @Resource
    private LogMapper logMapper;

    public Long getUserId(HttpServletRequest request) throws Exception{
        Object userInfo = request.getSession().getAttribute("user");
        if(userInfo!=null) {
            User user = (User) userInfo;
            return user.getId();
        } else {
            return null;
        }
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int insertLog(String beanJson, HttpServletRequest request) throws Exception{
        Log log = JSONObject.parseObject(beanJson, Log.class);
        int result=0;
        try{
            result=logMapper.insertSelective(log);
        }catch(Exception e){
            LogException.writeFail(logger, e);
        }
        return result;
    }

    public void insertLog(String moduleName, String type, HttpServletRequest request)throws Exception{
        try{
            Long userId = getUserId(request);
            if(userId!=null) {
                Log log = new Log();
                log.setUserid(userId);
                log.setOperation(moduleName);
                log.setClientip(getLocalIp(request));
                log.setCreatetime(new Date());
                Byte status = 0;
                log.setStatus(status);
                log.setContentdetails(type + moduleName);
                logMapper.insertSelective(log);
            }
        }catch(Exception e){
            LogException.writeFail(logger, e);
        }
    }


}
