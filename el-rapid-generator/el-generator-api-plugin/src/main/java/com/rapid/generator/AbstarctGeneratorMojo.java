package com.rapid.generator;

import cn.org.rapid_framework.generator.GeneratorProperties;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;


public abstract class AbstarctGeneratorMojo extends AbstractMojo {
    /**
     * The Maven project to analyze.
     *
     * @parameter expression="${project}"
     * @required
     */
    private MavenProject project;

    private String tableParameter;

    protected ClassLoader getClassLoader()
    {
        try
        {
            Collection<URL> urls = new ArrayList();
            for (Object element : this.project.getCompileClasspathElements()) {
                urls.add(new File(String.valueOf(element)).toURI().toURL());
            }
            Set<Artifact> artifacts = this.project.getArtifacts();
            for (Artifact artifact : artifacts) {
                urls.add(artifact.getFile().toURI().toURL());
            }
            System.out.println("urls = \n" + urls.toString().replace(",", "\n"));
            return new URLClassLoader((URL[])urls.toArray(new URL[urls.size()]), getClass().getClassLoader());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("failed to get the classloader.");
        }
        return getClass().getClassLoader();
    }

    public MavenProject getProject()
    {
        return this.project;
    }

    protected String[] parseStringArray(String string)
    {
        return MojoUtil.parseStringArray(string);
    }

    protected boolean isOsWindows()
    {
        return MojoUtil.isOsWindows();
    }

    public String getTableParameter() {
        return GeneratorProperties.getRequiredProperty("tableParameter");
    }

    public void setTableParameter(String tableParameter) {
        this.tableParameter = tableParameter;
    }

    public static class MojoUtil
    {
        public static String[] parseStringArray(String string)
        {
            return string.split(",");
        }

        public static boolean isOsWindows()
        {
            return System.getProperty("os.name").toUpperCase().contains("Windows".toUpperCase());
        }

        public static String toConsistentFilePath(String path)
        {
            return new File(path).getAbsolutePath();
        }

        public static void openFileIfOnWindows()
                throws IOException
        {
            if (isOsWindows()) {
                Runtime.getRuntime().exec("cmd.exe /c start " + toConsistentFilePath(GeneratorProperties.getRequiredProperty("outRoot")));
            }
        }
    }
}
