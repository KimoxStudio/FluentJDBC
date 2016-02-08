package connection;

import connection.exceptions.ConnectionException;
import connection.exceptions.MalformedSelectException;
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

    @Test(expected=MalformedSelectException.class)
    public void should_not_allow_call_from_after_select() throws Exception, MalformedSelectException {
        FluentConnection.build().select().from("user");
        assertThat(instance.query().toUpperCase(), is("SELECT"));
    }

    @Test
    public void should_form_query_with_values_and_table() throws Exception, MalformedSelectException {
        FluentConnection.build().
                select().
                parameter("name").as("a").
                parameter("age").of("user").
                from("user");
        assertThat(instance.query().toUpperCase(), is("SELECT (NAME) AS A , (USER.AGE) FROM USER"));
    }

}
