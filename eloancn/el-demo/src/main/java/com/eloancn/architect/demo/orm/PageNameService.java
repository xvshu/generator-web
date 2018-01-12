package com.eloancn.architect.demo.orm;

import com.eloancn.architect.constant.Constants;
import com.eloancn.architect.dao.NameDao;
import com.eloancn.architect.model.Name;
import com.eloancn.framework.orm.mybatis.paginator.domain.Order;
import com.eloancn.framework.orm.mybatis.paginator.domain.PageBounds;
import com.eloancn.framework.orm.mybatis.paginator.domain.PageList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * PageNameService 分页查询Name对象
 * Created by qinxf on 2017/11/6.
 */
@Service
public class PageNameService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PageNameService.class);

    @Autowired
    private NameDao nameDao;

    /**
     * 分页查询Name
     * @param param
     * @param pageNo
     * @param pageSize
     * @return
     */
    public PageList<Name> searchPage(Map param, Integer pageNo, Integer pageSize){
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("=PageNameService.searchPage=>param[{}],pageNo[{}],pageSize[{}] start!",param.toString(), pageNo, pageSize);
        }

        //如果没有传分页信息使用默认分页
        if(pageNo == null || pageNo == 0){
            pageNo = Constants.DEFAULT_PAGE_NO;
        }
        if(pageSize == null || pageSize == 0){
            pageSize = Constants.DEFAULT_PAGE_SIZE;
        }

        //分页，排序
        //true：标记需要分页
        PageBounds pageBounds = new PageBounds(pageNo,pageSize,true);
        List<Order> orders = new ArrayList<Order>();
        Order order = new Order("id", Order.Direction.DESC, null);
        orders.add(order);
        pageBounds.setOrders(orders);

        PageList<Name> result = nameDao.search(param, pageBounds);
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("=PageNameService.searchPage=>param[{}],pageNo[{}],pageSize[{}] end!",param.toString(), pageNo, pageSize);
        }
        return result;
    }

    /**
     * 不使用分页查询
     * @param param
     * @return
     */
    public PageList<Name> searchNoPage(Map param){
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("=PageNameService.searchPage=>param[{}] start!",param.toString());
        }

        //不使用分页方式，直接new PageBounds()
        PageList<Name> result = nameDao.search(param, new PageBounds());
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("=PageNameService.searchPage=>param[{}] end!",param.toString());
        }
        return result;
    }

    /**
     * 查询list
     * @param param
     * @return
     */
    public List<Name> list(Map param){
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("=PageNameService.list=>param[{}] start!",param.toString());
        }

        List<Name> result = nameDao.list(param);
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("=PageNameService.list=>param[{}] end!",param.toString());
        }
        return result;
    }
}
