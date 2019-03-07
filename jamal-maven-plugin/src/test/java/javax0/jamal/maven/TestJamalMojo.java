package javax0.jamal.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class TestJamalMojo {

    private JamalMojo sut;

    private void set(String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = JamalMojo.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(sut, value);
    }

    @Test
    public void testNothing() {
        sut = new JamalMojo();
//        sut.execute();
    }

}
