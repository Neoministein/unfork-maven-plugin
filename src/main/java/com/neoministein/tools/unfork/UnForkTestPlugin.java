package com.neoministein.tools.unfork;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "unfork-test",
        defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class UnForkTestPlugin extends AbstractUnForkOperation {

    @Override
    public void execute() throws MojoExecutionException {
        extract("/src/test/java", "/src/test/resources", "test-sources", "test-resources");
    }
}
