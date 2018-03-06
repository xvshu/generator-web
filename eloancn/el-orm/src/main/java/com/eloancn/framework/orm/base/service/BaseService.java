package com.eloancn.framework.orm.base.service;

import com.eloancn.framework.orm.base.dao.IBaseDao;
import com.eloancn.framework.orm.mybatis.paginator.domain.PageBounds;
import com.eloancn.framework.orm.mybatis.paginator.domain.PageList;
import com.eloancn.framework.utils.ObjectUtils;
import com.eloancn.framework.utils.ReflectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *  base Service parent class
 * Created by qinxf on 2018/1/17.
 */
public class BaseService<T extends Serializable> implements ApplicationContextAware, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseService.class);
    private ApplicationContext applicationContext;
    private IBaseDao<T> baseDao;

    /**
     * <insert one>
     * @param _t
     * @return
     */
    public int insert(T _t){
        if(ObjectUtils.isNullOrEmpty(_t)){
            LOGGER.error("insert parameter is null!");
        }
        return this.baseDao.insert(_t);
    }

    /**
     * <update one>
     * @param _t
     * @return
     */
    public int update(T _t){
        if(ObjectUtils.isNullOrEmpty(_t)){
            LOGGER.error("update parameter is null!");
        }
        return this.baseDao.update(_t);
    }

    /**
     * <get one>
     * @param _id
     * @return
     */
    public T get(Long _id){
        if(ObjectUtils.isNullOrEmpty(_id)){
            LOGGER.error("get parameter is null!");
        }
        return this.baseDao.get(_id);
    }

    /**
     * <delete one>
     * @param _id
     * @return
     */
    public int delete(Long _id){
        if(ObjectUtils.isNullOrEmpty(_id)){
            LOGGER.error("delete parameter is null!");
        }
        return this.baseDao.delete(_id);
    }

    /**
     * <search list>
     * @param _map
     * @return
     */
    public List<T> list(Map _map){
        if(ObjectUtils.isNullOrEmpty(_map)){
            LOGGER.error("search list parameter is null!");
        }
        return this.baseDao.list(_map);
    }

    /**
     * <search page list>
     * @param _map
     * @param _page
     * @return
     */
    public PageList<T> search(Map _map, PageBounds _page){
        if(ObjectUtils.isNullOrEmpty(_map)){
            LOGGER.error("search page parameter is null!");
        }
        return this.baseDao.search(_map,_page);
    }

    /**
     * <insert batch>
     * @param _list
     * @return
     */
    public int insertBatch(List<T> _list){
        if(ObjectUtils.isNullOrEmpty(_list)){
            LOGGER.error("insert batch parameter is null!");
        }
        return this.baseDao.insertBatch(_list);
    }

    /**
     * <update batch>
     * @param _list
     * @return
     */
    public int updateBatch(List<T> _list){
        if(ObjectUtils.isNullOrEmpty(_list)){
            LOGGER.error("update batch parameter is null!");
        }
        return this.baseDao.updateBatch(_list);
    }

    public void setBaseDao(IBaseDao<T> baseDao) {
        this.baseDao = baseDao;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void afterPropertiesSet() throws Exception {
        String className = ReflectUtils.getSuperClassGenricType(this.getClass()).getSimpleName();
        className = className.substring(0, 1).toLowerCase() + className.substring(1);
        if(this.applicationContext.containsBean(className + "Dao")) {
            this.baseDao = (IBaseDao<T>) this.applicationContext.getBean(className + "Dao");
        } else {
            this.LOGGER.error("no bean " + className + ",notice: saf client not need it.");
        }
    }

    public static void main(String[] args) {
        BaseService bs = new BaseService();
        try {
            bs.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
