package connection.builder;

import connection.exceptions.MalformedSelectException;

public interface Parameter {
    Field parameter(String name);
    QueryBuilder.SelectBuilder from(String table) throws MalformedSelectException;
}
