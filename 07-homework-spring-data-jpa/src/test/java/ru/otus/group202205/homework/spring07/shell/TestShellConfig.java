package ru.otus.group202205.homework.spring07.shell;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.shell.boot.CommandCatalogAutoConfiguration;
import org.springframework.shell.boot.CompleterAutoConfiguration;
import org.springframework.shell.boot.ComponentFlowAutoConfiguration;
import org.springframework.shell.boot.ExitCodeAutoConfiguration;
import org.springframework.shell.boot.JLineShellAutoConfiguration;
import org.springframework.shell.boot.ParameterResolverAutoConfiguration;
import org.springframework.shell.boot.ShellContextAutoConfiguration;
import org.springframework.shell.boot.SpringShellAutoConfiguration;
import org.springframework.shell.boot.StandardAPIAutoConfiguration;
import org.springframework.shell.result.ResultHandlerConfig;

@Configuration
@Import({SpringShellAutoConfiguration.class, JLineShellAutoConfiguration.class,
    ResultHandlerConfig.class, CommandCatalogAutoConfiguration.class, ShellContextAutoConfiguration.class, ExitCodeAutoConfiguration.class,
    CompleterAutoConfiguration.class, ParameterResolverAutoConfiguration.class, ComponentFlowAutoConfiguration.class, StandardAPIAutoConfiguration.class})
public class TestShellConfig {

}
