package com.nowcoder.communityy;

import com.nowcoder.communityy.util.SensitiveFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityyApplication.class)
public class SensitiveFilterTests {

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void testSensitiveFilter() {
        String text = "这里可以*喝）奶&&茶，可以可口可乐，嘻嘻嘻嘻！";
        text = sensitiveFilter.filter(text);
        System.out.println(text);
    }
}
