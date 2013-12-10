/*
 * Copyright 2013 Alexei Barantsev
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package ru.st.selenium.wait;

import org.openqa.selenium.*;
import ru.st.selenium.wait.ActionRepeater;
import ru.st.selenium.wrapper.WebDriverWrapper;

import static ru.st.selenium.wait.ActionRepeater.*;
import static ru.st.selenium.wait.RepeatableActions.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ClientSideImplicitWaitWrapper extends WebDriverWrapper {

  @Override
  protected Class<? extends WebElementWrapper> getElementWrapperClass() {
    return ImplicitWaitElementWrapper.class;
  }

  @Override
  protected Class<? extends TargetLocatorWrapper> getTargetLocatorWrapperClass() {
    return ImplicitWaitTargetLocatorWrapper.class;
  }

  private static final int DEFAULT_TIMEOUT = 10;

  private int timeout = DEFAULT_TIMEOUT;

  public ClientSideImplicitWaitWrapper(final WebDriver driver) {
    this(driver, DEFAULT_TIMEOUT);
  }

  public ClientSideImplicitWaitWrapper(final WebDriver driver, int timeoutInSeconds) {
    super(driver);
    this.timeout = timeoutInSeconds;
    driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
  }

  public int getTimeout() {
    return timeout;
  }

  public void setTimeout(int timeout) {
    this.timeout = timeout;
  }

  protected ActionRepeater<WebDriver> withWebDriver() {
    return with(getWrappedDriver(), timeout);
  }

  @Override
  public WebElement findElement(By locator) {
    return withWebDriver().tryTo(performFindElement(locator));
  }

  @Override
  public List<WebElement> findElements(By locator) {
    return withWebDriver().tryTo(performFindElements(locator));
  }

  public class ImplicitWaitElementWrapper extends WebElementWrapper {

    public ImplicitWaitElementWrapper(WebDriverWrapper driverWrapper, WebElement element) {
      super(driverWrapper, element);
    }

    protected ActionRepeater<WebElement> withWebElement() {
      return with(getWrappedElement(), timeout);
    }

    @Override
    public void click() {
      withWebElement().tryTo(performClick());
    }

    @Override
    public void submit() {
      withWebElement().tryTo(performSubmit());
    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
      withWebElement().tryTo(performSendKeys(keysToSend));
    }

    @Override
    public void clear() {
      withWebElement().tryTo(performClear());
    }

    @Override
    public boolean isSelected() {
      return withWebElement().tryTo(checkIsSelected());
    }

    @Override
    public boolean isEnabled() {
      return withWebElement().tryTo(checkIsEnabled());
    }

    @Override
    public WebElement findElement(By locator) {
      return withWebElement().tryTo(performFindElement(locator));
    }

    @Override
    public List<WebElement> findElements(By locator) {
      return withWebElement().tryTo(performFindElements(locator));
    }
  }

  public class ImplicitWaitTargetLocatorWrapper extends TargetLocatorWrapper {

    public ImplicitWaitTargetLocatorWrapper(WebDriverWrapper driverWrapper, TargetLocator targetLocator) {
      super(driverWrapper, targetLocator);
    }

    @Override
    public Alert alert() {
      return withWebDriver().tryTo(performSwitchToAlert());
    }
  }
}