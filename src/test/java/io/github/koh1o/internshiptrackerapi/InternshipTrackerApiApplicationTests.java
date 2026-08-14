package io.github.koh1o.internshiptrackerapi;

import io.github.koh1o.internshiptrackerapi.configuration.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class InternshipTrackerApiApplicationTests {

    @Test
    void contextLoads() {
    }

}
