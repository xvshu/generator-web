package com.eloancn.framework.orm.base.dao;

import com.eloancn.framework.orm.mybatis.paginator.domain.PageBounds;
import com.eloancn.framework.orm.mybatis.paginator.domain.PageList;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * base dao parent class
 * Created by qinxf on 2018/1/17.
 */
public interface IBaseDao<T extends Serializable> {

    /**
     * <insert one>
     * @param _t
     * @return
     */
    int insert(T _t);

    /**
     * <update one>
     * @param _t
     * @return
     */
    int update(T _t);

    /**
     * <get one>
     * @param _id
     * @return
     */
    T get(Long _id);

    /**
     * <delete one>
     * @param _id
     * @return
     */
    int delete(Long _id);

    /**
     * <search list>
     * @param _map
     * @return
     */
    List<T> list(Map _map);

    /**
     * <search page list>
     * @param _map
     * @param _page
     * @return
     */
    PageList<T> search(Map _map, PageBounds _page);

    /**
     * <insert batch>
     * @param _list
     * @return
     */
    int insertBatch(List<T> _list);

    /**
     * <update batch>
     * @param _list
     * @return
     */
    int updateBatch(List<T> _list);
}
