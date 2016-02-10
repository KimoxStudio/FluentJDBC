package connection.builder.helpers;

import connection.builder.QueryBuilder;
import connection.exceptions.MalformedSelectException;

public interface From {
    QueryBuilder.SelectBuilder from(String table) throws MalformedSelectException;
}
