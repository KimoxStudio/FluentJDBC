package connection.builder;

import connection.builder.helpers.As;
import connection.builder.helpers.From;
import connection.builder.helpers.Parameter;
import connection.exceptions.MalformedSelectException;

public class Field implements Parameter, From {
    private String name;
    private static QueryBuilder.SelectBuilder selectBuilder;
    private String aka;

    public Field(String name, QueryBuilder.SelectBuilder selectBuilder) {
        this.name = name;
        Field.selectBuilder = selectBuilder;
    }

    public FieldBuilder of(String table) {
        name = String.format("%s.%s", table, name);
        return new FieldBuilder();
    }

    public QueryBuilder.SelectBuilder as(String aka) {
        this.aka = aka;
        return selectBuilder;
    }

    @Override
    public QueryBuilder.Query from(String table) throws MalformedSelectException {
        return selectBuilder.from(table);
    }

    public Field parameter(String name) {
        return selectBuilder.field(name);
    }

    public class FieldBuilder implements Parameter, As {

        @Override
        public QueryBuilder.SelectBuilder as(String aka) {
            Field.this.aka = aka;
            return selectBuilder;
        }

        @Override
        public Field parameter(String name) {
            return selectBuilder.field(name);
        }

        public QueryBuilder.SelectBuilder.AllBuilder all() {
            return selectBuilder.all();
        }

        public QueryBuilder.Query from(String table) throws MalformedSelectException {
            return selectBuilder.from(table);
        }
    }



    public String build() {
        return String.format("%s%s", name, aka == null ? "" : " AS " + aka);
    }
}
