package connection.builder;

import connection.exceptions.MalformedSelectException;

public class Field implements Parameter {
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
    public QueryBuilder.EndQuery from(String table) throws MalformedSelectException {
        return selectBuilder.from(table);
    }

    public Field parameter(String name) {
        return selectBuilder.parameter(name);
    }

    public class FieldBuilder implements Parameter{
        public QueryBuilder.SelectBuilder as(String aka) {
            Field.this.aka = aka;
            return selectBuilder;
        }

        public Field parameter(String name) {
            return selectBuilder.parameter(name);
        }

        public QueryBuilder.SelectBuilder.AllBuilder all() {
            return selectBuilder.all();
        }

        public QueryBuilder.EndQuery from(String table) throws MalformedSelectException {
            return selectBuilder.from(table);
        }
    }



    public String build() {
        return String.format("%s%s", name, aka == null ? "" : " AS " + aka);
    }
}
