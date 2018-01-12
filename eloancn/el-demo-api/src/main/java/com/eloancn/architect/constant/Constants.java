package com.eloancn.architect.constant;

/**
 * Constants常量帮助类
 * Created by qinxf on 2017/11/1.
 */
public interface Constants {

    /** rabbitmq的exchange名称 */
    String RABBITMQ_EXCHANGE = "test_xf_exchange";
    /** rabbitmq的routekey名称 */
    String RABBITMQ_ROUTEKEY = "test_xf_key";

    /** 默认分页-页码 */
    Integer DEFAULT_PAGE_NO = 1;
    /** 默认分页-每页记录数 */
    Integer DEFAULT_PAGE_SIZE = 10;
}
