package com.rapid.generator;

import cn.org.rapid_framework.generator.GeneratorFacade;
import cn.org.rapid_framework.generator.GeneratorProperties;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;


public class GeneratorMojo extends AbstarctGeneratorMojo{

    public String template = "template";
    private GeneratorFacade generator;

    public void execute() throws MojoExecutionException, MojoFailureException {
        this.run(template);
    }

    public void run(String template){
        Thread currentThread = Thread.currentThread();
        ClassLoader oldClassLoader = currentThread.getContextClassLoader();
        try{
            currentThread.setContextClassLoader(getClassLoader());
            System.out.println("current project to build: " + getProject().getName() + "\n" + getProject().getFile().getParent());

            String projectUrl = getProject().getFile().getParent();
            String projectName = projectUrl.substring(projectUrl.lastIndexOf('\\')+1,projectUrl.length());
            GeneratorProperties.setProperty("projectName",projectName);
            System.out.println("projectName-----------------------------"+projectName);
            GeneratorProperties.setProperty("outRoot","../../");
            System.out.println("outRoot-----------------------------[./]");

            GeneratorProperties.setProperty("java_typemapping.java.sql.Timestamp","java.util.Date");
            GeneratorProperties.setProperty("java_typemapping.java.sql.Date","java.util.Date");
            GeneratorProperties.setProperty("java_typemapping.java.sql.Time","java.util.Date");
            GeneratorProperties.setProperty("java_typemapping.java.lang.Byte","Integer");
            GeneratorProperties.setProperty("java_typemapping.java.lang.Short","Integer");
            GeneratorProperties.setProperty("java_typemapping.java.math.BigDecimal","Double");

            this.generator = new GeneratorFacade();
            this.generator.getGenerator().setTemplateRootDirs(new String[] { "classpath:" + template });

            try {
                this.generator.generateByAllTable();
            } catch (Exception e) {
                e.printStackTrace();
            }
//            genByTable(parseStringArray(getTableParameter()));
        } finally {
            currentThread.setContextClassLoader(oldClassLoader);
        }
    }

    public void genByTable(String... table) {
        try {
            this.generator.generateByTable(table);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String projectUrl = "D:\\workspace\\MicroService";
        String projectName = projectUrl.substring(projectUrl.lastIndexOf('\\')+1,projectUrl.length());
        System.out.println(projectName);
    }
}
