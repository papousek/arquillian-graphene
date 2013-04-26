package org.jboss.arquillian.graphene.ftest.enhanced;

import java.net.URL;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(Arquillian.class)
public abstract class AbstractEnhancedElementTest {

    @Drone
    protected WebDriver browser;

    @Before
    public void loadPage() {
        URL page = this.getClass().getClassLoader()
            .getResource("org/jboss/arquillian/graphene/ftest/enhanced/index.html");

        browser.get(page.toExternalForm());
    }

}
