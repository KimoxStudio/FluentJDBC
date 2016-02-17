package connection.builder;


import connection.builder.helpers.FromAs;
import connection.builder.helpers.Query;

public class FromBuilder implements Query {

    private QueryBuilder queryBuilder;

    public FromBuilder(QueryBuilder queryBuilder) {
        this.queryBuilder = queryBuilder;
    }

    @Override
    public String query() {
        return queryBuilder.query();
    }

    public FromBuilder andOf(String table) {
        queryBuilder.add(", " + table);
        return this;
    }

    public FromAs andWithAs(String table) {
        queryBuilder.add(", " + table);
        return new FromAs() {
            @Override
            public FromBuilder as(String aka) {
                FromBuilder.this.queryBuilder.add("AS " + aka);
                return FromBuilder.this;
            }
        };
    }



    public FromAs andOf(FromBuilder query) {
        queryBuilder.add(", (" + query.query() + ")");
        return new FromAs() {
            @Override
            public FromBuilder as(String aka) {
                queryBuilder.add("AS " + aka);
                return FromBuilder.this;
            }
        };
    }

    public WhereBuilder where() {
        queryBuilder.add("WHERE");
        return new WhereBuilder(queryBuilder);
    }
}
