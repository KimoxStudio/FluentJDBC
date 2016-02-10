package connection.builder.helpers;

import connection.builder.QueryBuilder;

public interface As {
    QueryBuilder.SelectBuilder as(String aka);
}
