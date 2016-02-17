package connection.builder.helpers;

import connection.builder.QueryBuilder;

public interface SelectAs {
    QueryBuilder.SelectBuilder as(String aka);
}
