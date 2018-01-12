package com.eloancn.framework.cache;
/**
 * @Package com.eloancn.framework.cache
 * @Title: RedisNameSpace
 * @author hapic
 * @date 2018/1/3 14:23
 * @version V1.0
 */

/**
 * @Descriptions: Redis的命名前缀
 */
public class RedisNameSpace {

    private String nameSpace;

    public RedisNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    @Override
    public String toString() {
        return nameSpace;
    }
}
