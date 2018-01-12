//
// Powered By [rapid-generator-framework]
// (By:qinxf)
//

package ${basepackage}.base;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InitDbUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InitDbUtils.class);
	

    public static void execute(DruidDataSource dataSource, String path) throws Exception {
        Statement stmt = dataSource.getConnection().createStatement();
        List<String> list = InitDbUtils.loadSql(path);
        for (String str : list) {
            stmt.addBatch(str);
        }
        LOGGER.info("=InitDbUtils.execute=>sql string:[]", list.toString());
        int[] rows = stmt.executeBatch();
        LOGGER.info("=InitDbUtils.execute=>Row count:[]", Arrays.toString(rows));
    }

    public static List<String> loadSql(String sqlFile) throws Exception {
        List<String> sqlList = new ArrayList<String>();
        File f = new File(InitDbUtils.class.getResource("/").getPath());
        FileReader fr= null;
        BufferedReader br = null;
        try {
            fr=new FileReader(f.getPath()+ sqlFile);
            br=new BufferedReader(fr);
            String line="";
            while ((line=br.readLine())!=null) {
                String sql = line.replaceAll("--.*", "").trim();
                if (!sql.equals("")) {
                    sqlList.add(sql);
                }
            }
            return sqlList;
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally{
            br.close();
            fr.close();
        }
    }
}
