package connection;

import connection.builder.WhereBuilder;
import connection.builder.helpers.Query;
import connection.exceptions.ConnectionException;
import connection.exceptions.MalformedSelectException;
import org.junit.Before;
import org.junit.Test;

import static connection.FluentConnection.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class _FluentConnection {

    private FluentConnection instance;
    private WhereBuilder query;

    @Before
    public void setUp() throws ConnectionException {
        init("localhost", "test", "root", "root");
        instance = connect();
    }

    @Test(expected=ConnectionException.class)
    public void should_connect_to_data_base() throws ConnectionException {
        assertThat(instance.isClosed(), is(false));
        instance.close();
        init("localhost", "test", "root", "");
        assertThat(connect().isClosed(), is(true));
    }

    @Test(expected=MalformedSelectException.class)
    public void should_not_allow_call_from_after_select() throws Exception, MalformedSelectException {
        query = with().select().from("user").where();
        assertThat(query.query().toUpperCase(), is("SELECT"));
    }

    @Test
    public void should_generate_a_query_with_values_and_table() throws Exception, MalformedSelectException {
        query = with().
                select().
                all().of("user").
                field("name").as("a").
                field("age").of("user").
                from("user").where();
        assertThat(query.query().toUpperCase(), is("SELECT USER.* , NAME AS A , USER.AGE FROM USER WHERE"));
    }

    @Test
    public void should_generate_a_basic_query() throws Exception, MalformedSelectException {
        query = with()
                .select()
                .all()
                .from("X").where();
        assertThat(query.query().toUpperCase(), is("SELECT * FROM X WHERE"));
    }

    @Test
    public void should_generate_a_query_with_values() throws Exception, MalformedSelectException {
        query = with()
                .select()
                .field("Z")
                .field("K")
                .from("X").where();
        assertThat(query.query().toUpperCase(), is("SELECT Z , K FROM X WHERE"));
    }

    @Test
    public void should_generate_a_query_with_values_with_names() throws Exception, MalformedSelectException {
        query = with()
                .select()
                .field("Z").as("F")
                .field("K").as("Z")
                .from("X").where();
        assertThat(query.query().toUpperCase(), is("SELECT Z AS F , K AS Z FROM X WHERE"));
    }

    @Test
    public void should_generate_a_query_with_queries() throws Exception, MalformedSelectException {
        query = with()
                .select()
                .field("Z").as("F")
                .field("K").as("Z")
                .field(with()
                        .select().all().from("pepe")).as("query")
                .from("X").where();
        assertThat(query.query().toUpperCase(), is("SELECT Z AS F , K AS Z , (SELECT * FROM PEPE) AS QUERY FROM X WHERE"));
    }

    @Test
    public void should_generate_a_query_with_if_sentence_with_one_condition() throws Exception, MalformedSelectException {
        query = with()
                .select()
                .field("Z").as("F")
                .field("K").as("Z")
                .condition(with()
                        .select().field("COUNT(*)").from("pepe")).isMoreThan("5").then("YES")
                .ifNot("NO")
                .as("query")
                .from("X").where();
        assertThat(query.query().toUpperCase(), is("SELECT Z AS F , K AS Z , IF((SELECT COUNT(*) FROM PEPE) > 5,\"YES\",\"NO\") AS QUERY FROM X WHERE"));
    }

    @Test
    public void should_generate_a_query_with_if_sentence_with_ands_conditions() throws Exception, MalformedSelectException {
        query = with()
                .select()
                .field("Z").as("F")
                .field("K").as("Z")
                .condition(with()
                        .select().field("COUNT(*)").from("pepe")).isMoreThan("5").and().isLessThan("10").then("YES")
                .ifNot("NO")
                .as("query")
                .from("X").where();
        assertThat(query.query().toUpperCase(), is("SELECT Z AS F , K AS Z , IF((SELECT COUNT(*) FROM PEPE) > 5 AND (SELECT COUNT(*) FROM PEPE) < 10,\"YES\",\"NO\") AS QUERY FROM X WHERE"));

        query = with()
                .select()
                .field("Z").as("F")
                .field("K").as("Z")
                .condition(with()
                        .select().field("COUNT(*)").from("pepe")).isMoreThan("5").and(with().select().field("COUNT(*)").from("juan")).isLessThan("10").then("YES")
                .ifNot("NO")
                .as("query")
                .from("X").where();
        assertThat(query.query().toUpperCase(), is("SELECT Z AS F , K AS Z , IF((SELECT COUNT(*) FROM PEPE) > 5 AND (SELECT COUNT(*) FROM JUAN) < 10,\"YES\",\"NO\") AS QUERY FROM X WHERE"));
    }
    @Test
    public void should_generate_a_query_with_if_sentence_with_ors_conditions() throws Exception, MalformedSelectException {
        Query condition = with()
                .select().field("COUNT(*)").from("pepe");

        query = with()
                .select()
                .field("Z").as("F")
                .field("K").as("Z")
                .condition(condition).isMoreThan("5").or().isLessThan("10").then("YES")
                .ifNot("NO")
                .as("query")
                .from("X").where();
        assertThat(query.query().toUpperCase(), is("SELECT Z AS F , K AS Z , IF((SELECT COUNT(*) FROM PEPE) > 5 OR (SELECT COUNT(*) FROM PEPE) < 10,\"YES\",\"NO\") AS QUERY FROM X WHERE"));

        query = with()
                .select()
                .field("Z").as("F")
                .field("K").as("Z")
                .condition(condition).isMoreThan("5")
                        .or(with().select().field("COUNT(*)").from("juan")).isLessThan("10")
                        .then("YES")
                        .ifNot("NO")
                .as("query")
                .from("X").where();
        assertThat(query.query().toUpperCase(), is("SELECT Z AS F , K AS Z , IF((SELECT COUNT(*) FROM PEPE) > 5 OR (SELECT COUNT(*) FROM JUAN) < 10,\"YES\",\"NO\") AS QUERY FROM X WHERE"));
    }

    @Test
    public void should_generate_from_sentence_with_inner_query() throws Exception, MalformedSelectException {
        query = with()
                .select()
                .all()
                .from(with().select().all().from("juan")).as("query").where();
        assertThat(query.query().toUpperCase(), is("SELECT * FROM (SELECT * FROM JUAN) AS QUERY WHERE"));
    }

    @Test (expected=MalformedSelectException.class)
    public void should_generate_an_exception_when_from_is_called_after_select() throws Exception, MalformedSelectException {
        query = with().select().from("juan").where();
    }

    @Test
    public void should_generate_multiple_from_sentences() throws Exception, MalformedSelectException {
        query = with()
                .select()
                .all()
                .from(with().select().all().from("juan")).as("query")
                .andOf("Juan")
                .andOf(with().select().all().from("pepe")).as("luis")
                .andWithAs("nene").as("Loco").where();
        assertThat(query.query().toUpperCase(), is("SELECT * FROM (SELECT * FROM JUAN) AS QUERY , JUAN , (SELECT * FROM PEPE) AS LUIS , NENE AS LOCO WHERE"));
    }

    @Test
    public void where_sentence_pending() throws Exception, MalformedSelectException {
        query = with()
                .select()
                .all()
                .from(with().select().all().from("juan")).as("query")
                .andOf("Juan")
                .andOf(with().select().all().from("pepe")).as("luis")
                .andWithAs("nene").as("Loco")
                .where();
        assertThat(query.query().toUpperCase(), is("SELECT * FROM (SELECT * FROM JUAN) AS QUERY , JUAN , (SELECT * FROM PEPE) AS LUIS , NENE AS LOCO WHERE"));

    }
}
