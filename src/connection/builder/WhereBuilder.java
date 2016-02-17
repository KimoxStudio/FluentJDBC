package connection.builder;

import connection.builder.helpers.Query;

public class WhereBuilder implements Query{

    private QueryBuilder queryBuilder;

    public WhereBuilder(QueryBuilder queryBuilder) {
        this.queryBuilder = queryBuilder;
    }

    @Override
    public String query() {
        return queryBuilder.query();
    }
}
