package com.eloancn.framework.orm.mybatis.paginator.dialect;

import com.eloancn.framework.orm.mybatis.paginator.domain.PageBounds;
import org.apache.ibatis.mapping.MappedStatement;

/**
 *  @author badqiu
 *  @author miemiedev
 */
public class DerbyDialect extends Dialect{

    public DerbyDialect(MappedStatement mappedStatement, Object parameterObject, PageBounds pageBounds) {
        super(mappedStatement, parameterObject, pageBounds);
    }
	
    protected String getLimitString(String sql, String offsetName,int offset, String limitName, int limit) {
        StringBuffer buffer = new StringBuffer( sql.length()+20 ).append(sql);
        if (offset > 0) {
            buffer.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
            setPageParameter(offsetName, offset, Integer.class);
            setPageParameter(limitName, limit, Integer.class);
        } else {
            buffer.append(" OFFSET 0 ROWS FETCH NEXT ? ROWS ONLY");
            setPageParameter(limitName, limit, Integer.class);
        }
        return buffer.toString();
	}   
  
}
