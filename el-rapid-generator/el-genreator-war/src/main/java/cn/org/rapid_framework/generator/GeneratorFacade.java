package cn.org.rapid_framework.generator;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.org.rapid_framework.generator.Generator.GeneratorModel;
import cn.org.rapid_framework.generator.provider.db.sql.model.Sql;
import cn.org.rapid_framework.generator.provider.db.table.TableFactory;
import cn.org.rapid_framework.generator.provider.db.table.model.Table;
import cn.org.rapid_framework.generator.provider.java.model.JavaClass;
import cn.org.rapid_framework.generator.util.BeanHelper;
import cn.org.rapid_framework.generator.util.GLogger;
import cn.org.rapid_framework.generator.util.GeneratorException;
import cn.org.rapid_framework.generator.util.typemapping.DatabaseTypeUtils;
import org.springframework.util.StringUtils;

/**
 * 
 * @author badqiu
 *
 */
public class GeneratorFacade {

	public static String projectName;

	public Generator g = new Generator();
	public GeneratorFacade(){
		String abspath= System.getProperty("user.dir");
//		GeneratorProperties.getProperty("outRoot");
		abspath += "\\src\\main\\webapp\\outRoot\\";
		GeneratorProperties.setProperty("outRoot", abspath);
		g.setOutRootDir(abspath);
	}

	public GeneratorFacade(String basePackage, String projectName, String tableRemovePrefixes, String jdbcIp,
						   String jdbcPort,String jdbcDatabase,String jdbcUserName,String jdbcPassWord,String realPath,String tables,String systemCode){
		String abspath= realPath + "outRoot/";
		g.setOutRootDir(abspath);
		g.setProjectName(projectName);
		GeneratorProperties.setProperty("basepackage", basePackage);
		GeneratorProperties.setProperty("project_name", projectName);
		GeneratorProperties.setProperty("systemCode", systemCode);
		GeneratorProperties.setProperty("tableRemovePrefixes", tableRemovePrefixes);
		String jdbc_url = "jdbc:mysql://"+jdbcIp+":"+jdbcPort+"/"+jdbcDatabase+"?useUnicode=true&amp;characterEncoding=UTF-8";
		GeneratorProperties.setProperty("jdbc_url", jdbc_url);
		GeneratorProperties.setProperty("jdbc_username", jdbcUserName);
		GeneratorProperties.setProperty("jdbc_password", jdbcPassWord);
		GeneratorProperties.setProperty("outRoot", abspath);
		if(!StringUtils.isEmpty(tableRemovePrefixes)){
			String [] pres = tableRemovePrefixes.split(",");
			for (String pre:pres) {
				tables = tables.replaceAll(pre,"");
			}
		}
		GeneratorProperties.setProperty("tables", tables);
		this.projectName = projectName;
		GeneratorProperties.projectName = projectName;
	}
	
	public static void printAllTableNames() throws Exception {
		PrintUtils.printAllTableNames(TableFactory.getInstance().getAllTables(),projectName);
	}
	
	public void deleteOutRootDir() throws IOException {
		g.deleteOutRootDir();
	}
	
	public void generateByMap(Map map,String templateRootDir) throws Exception {
		new ProcessUtils().processByMap(map, templateRootDir,false);
	}

	public void deleteByMap(Map map,String templateRootDir) throws Exception {
		new ProcessUtils().processByMap(map, templateRootDir,true);
	}
	
	public void generateByAllTable(String templateRootDir) throws Exception {
		new GeneratorFacade.ProcessUtils().processByAllTable(templateRootDir,false,projectName);
	}
	
	public void deleteByAllTable(String templateRootDir) throws Exception {
		new ProcessUtils().processByAllTable(templateRootDir,true,projectName);
	}
	
    public void generateByTable(String tableName,String templateRootDir) throws Exception {
    	new GeneratorFacade.ProcessUtils().processByTable(tableName,templateRootDir,false,projectName);
	}

    public void deleteByTable(String tableName,String templateRootDir) throws Exception {
    	new ProcessUtils().processByTable(tableName,templateRootDir,true,projectName);
	}
    
	public void generateByClass(Class clazz,String templateRootDir) throws Exception {
		new ProcessUtils().processByClass(clazz, templateRootDir,false,projectName);
	}

	public void deleteByClass(Class clazz,String templateRootDir) throws Exception {
		new ProcessUtils().processByClass(clazz, templateRootDir,true,projectName);
	}
	
	public void generateBySql(Sql sql,String templateRootDir) throws Exception {
		new ProcessUtils().processBySql(sql,templateRootDir,false,projectName);
	}

	public void deleteBySql(Sql sql,String templateRootDir) throws Exception {
		new ProcessUtils().processBySql(sql,templateRootDir,true,projectName);
	}
	
    private Generator getGenerator(String templateRootDir) {
        g.setTemplateRootDir(new File(templateRootDir).getAbsoluteFile());
        return g;
    }
    
    /** 生成器的上下文，存放的变量将可以在模板中引用 */
    public static class GeneratorContext {
        static ThreadLocal<Map> context = new ThreadLocal<Map>();
        public static void clear() {
            Map m = context.get();
            if(m != null) m.clear();
        }
        public static Map getContext() {
            Map map = context.get();
            if(map == null) {
                setContext(new HashMap());
            }
            return context.get();
        }
        public static void setContext(Map map) {
            context.set(map);
        }
        public static void put(String key,Object value) {
            getContext().put(key, value);
        }
    }
    
    public class ProcessUtils {
    	public void processByMap(Map params, String templateRootDir,boolean isDelete) throws Exception, FileNotFoundException {
			Generator g = getGenerator(templateRootDir);
			GeneratorModel m = GeneratorModelUtils.newFromMap(params);
			try {
				if(isDelete)
					g.deleteBy(m.templateModel, m.filePathModel);
				else
					g.generateBy(m.templateModel, m.filePathModel);
			}catch(GeneratorException ge) {
				PrintUtils.printExceptionsSumary(ge.getMessage(),getGenerator(templateRootDir).getOutRootDir(),ge.getExceptions());
			}
    	}

    	public void processBySql(Sql sql,String templateRootDir,boolean isDelete,String projectName) throws Exception {
    		Generator g = getGenerator(templateRootDir);
    		GeneratorModel m = GeneratorModelUtils.newFromSql(sql);
    		PrintUtils.printBeginProcess("sql:"+sql.getSourceSql(),isDelete,projectName);
    		try {
    			if(isDelete) {
    				g.deleteBy(m.templateModel, m.filePathModel);
    			}else {
    				g.generateBy(m.templateModel, m.filePathModel);
    			}
    		}catch(GeneratorException ge) {
    			PrintUtils.printExceptionsSumary(ge.getMessage(),getGenerator(templateRootDir).getOutRootDir(),ge.getExceptions());
    		}
    	}

    	public void processByClass(Class clazz, String templateRootDir,boolean isDelete,String projectName) throws Exception, FileNotFoundException {
			Generator g = getGenerator(templateRootDir);
			GeneratorModel m = GeneratorModelUtils.newFromClass(clazz);
			PrintUtils.printBeginProcess("JavaClass:"+clazz.getSimpleName(),isDelete,projectName);
			try {
				if(isDelete)
					g.deleteBy(m.templateModel, m.filePathModel);
				else
					g.generateBy(m.templateModel, m.filePathModel);
			}catch(GeneratorException ge) {
				PrintUtils.printExceptionsSumary(ge.getMessage(),getGenerator(templateRootDir).getOutRootDir(),ge.getExceptions());
			}
    	}

        public void processByTable(String tableName,String templateRootDir,boolean isDelete,String projectName) throws Exception {
        	if("*".equals(tableName)) {
        		generateByAllTable(templateRootDir);
        		return;
        	}
    		Generator g = getGenerator(templateRootDir);
    		Table table = TableFactory.getInstance().getTable(tableName);
    		try {
    			processByTable(g,table,isDelete,projectName);
    		}catch(GeneratorException ge) {
    			PrintUtils.printExceptionsSumary(ge.getMessage(),getGenerator(templateRootDir).getOutRootDir(),ge.getExceptions());
    		}
    	}

		public void processByAllTable(String templateRootDir,boolean isDelete,String projectName) throws Exception {
			List<Table> tables = TableFactory.getInstance().getAllTables();
			List exceptions = new ArrayList();
			for(int i = 0; i < tables.size(); i++ ) {
				try {
					processByTable(getGenerator(templateRootDir),tables.get(i),isDelete,projectName);
				}catch(GeneratorException ge) {
					exceptions.addAll(ge.getExceptions());
				}
			}
			PrintUtils.printExceptionsSumary("",getGenerator(templateRootDir).getOutRootDir(),exceptions);
		}

		public void processByTable(Generator g, Table table,boolean isDelete,String projectName) throws Exception {
	        GeneratorModel m = GeneratorModelUtils.newFromTable(table);
	        PrintUtils.printBeginProcess(table.getSqlName()+" => "+table.getClassName(),isDelete,projectName);
	        if(isDelete)
	        	g.deleteBy(m.templateModel,m.filePathModel);
	        else
	        	g.generateBy(m.templateModel,m.filePathModel);
	    }
    }
	
    @SuppressWarnings("all")
	public static class GeneratorModelUtils {
		
		public static GeneratorModel newFromTable(Table table) {
			Map templateModel = new HashMap();
			templateModel.put("table", table);
			setShareVars(templateModel);
			
			Map filePathModel = new HashMap();
			setShareVars(filePathModel);
			filePathModel.putAll(BeanHelper.describe(table));
			return new GeneratorModel(templateModel,filePathModel);
		}

		public static GeneratorModel newFromSql(Sql sql) throws Exception {
			Map templateModel = new HashMap();
			templateModel.put("sql", sql);
			setShareVars(templateModel);
			
			Map filePathModel = new HashMap();
			setShareVars(filePathModel);
			filePathModel.putAll(BeanHelper.describe(sql));
			return new GeneratorModel(templateModel,filePathModel);
		}

		public static GeneratorModel newFromClass(Class clazz) {
			Map templateModel = new HashMap();
			templateModel.put("clazz", new JavaClass(clazz));
			setShareVars(templateModel);
			
			Map filePathModel = new HashMap();
			setShareVars(filePathModel);
			filePathModel.putAll(BeanHelper.describe(new JavaClass(clazz)));
			return new GeneratorModel(templateModel,filePathModel);
		}
		
		public static GeneratorModel newFromMap(Map params) {
			Map templateModel = new HashMap();
			templateModel.putAll(params);
			setShareVars(templateModel);
			
			Map filePathModel = new HashMap();
			setShareVars(filePathModel);
			filePathModel.putAll(params);
			return new GeneratorModel(templateModel,filePathModel);
		}
		
		public static void setShareVars(Map templateModel) {
			templateModel.putAll(GeneratorProperties.getProperties());
			templateModel.putAll(System.getProperties());
			templateModel.put("env", System.getenv());
			templateModel.put("now", new Date());
			templateModel.put("databaseType", getDatabaseType("databaseType"));
			templateModel.putAll(GeneratorContext.getContext());
		}

		private static String getDatabaseType(String key) {
			return GeneratorProperties.getProperty(key,DatabaseTypeUtils.getDatabaseTypeByJdbcDriver(GeneratorProperties.getProperty("jdbc_driver")));
		}

	}
	
	private static class PrintUtils {
		
		private static void printExceptionsSumary(String msg,String outRoot,List<Exception> exceptions) throws FileNotFoundException {
			File errorFile = new File(outRoot,"generator_error.log");
			if(exceptions != null && exceptions.size() > 0) {
				System.err.println("[Generate Error Summary] : "+msg);
				PrintStream output = new PrintStream(new FileOutputStream(errorFile));
				for(int i = 0; i < exceptions.size(); i++) {
					Exception e = exceptions.get(i);
                    System.err.println("[GENERATE ERROR]:"+e);
					if(i == 0) e.printStackTrace();
					e.printStackTrace(output);
				}
				output.close();
				System.err.println("***************************************************************");
				System.err.println("* "+"* 输出目录已经生成generator_error.log用于查看错误 ");
				System.err.println("***************************************************************");
			}
		}
		
		private static void printBeginProcess(String displayText,boolean isDatele,String projectName) {
			GLogger.println(projectName,"***************************************************************");
			GLogger.println(projectName,"* BEGIN " + (isDatele ? " delete by " : " generate by ")+ displayText);
			GLogger.println(projectName,"***************************************************************");
		}
		
		public static void printAllTableNames(List<Table> tables,String projectName) throws Exception {
			GLogger.println(projectName,"\n----All TableNames BEGIN----");
			for(int i = 0; i < tables.size(); i++ ) {
				String sqlName = ((Table)tables.get(i)).getSqlName();
				GLogger.println(projectName,"g.generateTable(\""+sqlName+"\");");
			}
			GLogger.println(projectName,"----All TableNames END----");
		}
	}

}
