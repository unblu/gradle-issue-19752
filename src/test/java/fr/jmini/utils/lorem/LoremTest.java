package fr.jmini.utils.lorem;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class LoremTest {

    @Test
    public void testDefault() throws Exception {
        String content = Lorem.getDefault();
        assertThat(content).isEqualTo("Lorem ipsum");
    }
}
