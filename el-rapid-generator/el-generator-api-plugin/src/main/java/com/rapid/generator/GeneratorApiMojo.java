package com.rapid.generator;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * @goal api
 */
public class GeneratorApiMojo extends GeneratorMojo{

    public String template = "template-api";

    public void execute() throws MojoExecutionException, MojoFailureException {
        super.run(template);
    }
}
