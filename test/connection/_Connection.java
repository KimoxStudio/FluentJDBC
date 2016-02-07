package connection;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class _Connection {

    @Test
    public void testName() throws Exception {
        Connection connection = new Connection("localhost", "test", "root", "");
        assertThat(connection.connect(), is(true));
        connection = new Connection("localhost", "test", "root", "root");
        assertThat(connection.connect(), is(false));
    }
}
