package connection;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class _FluentConnection {

    private FluentConnection instance;

    @Before
    public void setUp() throws ConnectionException {
        FluentConnection.init("localhost", "test", "root", "root");
        instance = FluentConnection.instance();
    }

    @Test(expected=ConnectionException.class)
    public void should_connect_to_data_base() throws ConnectionException {
        assertThat(instance.isClosed(), is(false));
        instance.close();
        FluentConnection.init("localhost", "test", "root", "");
        assertThat(FluentConnection.instance().isClosed(), is(true));
    }
}
