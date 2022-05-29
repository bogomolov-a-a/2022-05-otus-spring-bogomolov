package ru.otus.group202205.homework.spring02.test.util;

import java.io.IOException;
import java.util.Objects;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePropertySource;

/**
 * Basic context initializer with user input imitation. Path for resource with input read from properties and substitute with {@link System#in} Close it(user
 * input) after each test method execution.
 */
public class BasicSystemInputApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

  private static final String TEST_SYSTEM_INPUT_RESOURCE_NAME = "test.user-input.filename";
  private final String testPropertiesResourceName;

  protected BasicSystemInputApplicationContextInitializer(String testPropertiesResourceName) {
    this.testPropertiesResourceName = testPropertiesResourceName;
  }

  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
    Resource testPropertyResource = new ClassPathResource(testPropertiesResourceName, this.getClass().getClassLoader());
    ResourcePropertySource propertiesSource;
    try {
      propertiesSource = new ResourcePropertySource(testPropertyResource);
    } catch (IOException e) {
      throw new IllegalStateException("not found properties at '" + testPropertiesResourceName + "'");
    }
    MutablePropertySources propertySources = applicationContext.getEnvironment().getPropertySources();
    propertySources.addFirst(propertiesSource);
    String testInputResourceLocation = (String) propertiesSource.getProperty(TEST_SYSTEM_INPUT_RESOURCE_NAME);
    Resource testInputResource = new ClassPathResource(Objects.requireNonNull(testInputResourceLocation), this.getClass().getClassLoader());
    try {
      System.setIn(testInputResource.getInputStream());
    } catch (IOException e) {
      throw new IllegalStateException("not found test user input  at '" + testInputResourceLocation + "'");
    }
  }
}
