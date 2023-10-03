package com.neo.util.tools.unfork;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.*;

@Mojo(name = "unfork-source",
        defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class UnForkSourcePlugin extends AbstractUnForkOperation {

    @Override
    public void execute() throws MojoExecutionException {
        extract("/src/main/java", "/src/main/resources", "sources", "resources");
    }
}
