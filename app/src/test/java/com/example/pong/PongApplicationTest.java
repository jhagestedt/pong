package com.example.pong;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PongApplication.class)
public class PongApplicationTest {

    @Autowired
    private Environment environment;

    @Test
    public void environment_available() {
        Assert.assertNotNull(environment);
    }

}
