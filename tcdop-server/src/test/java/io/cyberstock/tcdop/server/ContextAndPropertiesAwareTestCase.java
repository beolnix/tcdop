package io.cyberstock.tcdop.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.BeforeTest;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by beolnix on 08/05/15.
 */
public class ContextAndPropertiesAwareTestCase {
    protected ApplicationContext context;
    protected Properties props = new Properties();

    Logger logger = LoggerFactory.getLogger(ContextAndPropertiesAwareTestCase.class);

    @BeforeTest
    public void prepareTestCase() throws Exception {
        prepareOriginalProperties();
        initializeContext();
    }

    private void prepareOriginalProperties() throws Exception {
        InputStream input = getClass().getResourceAsStream("/tcdop.properties");
        this.props.load(input);
    }

    private void initializeContext() {
        this.context = new ClassPathXmlApplicationContext("classpath:/META-INF/tcdop-root-context.xml");
    }

}
