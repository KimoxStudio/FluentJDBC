package connection;

public class Field {
    private String name;
    private QueryBuilder.SelectBuilder selectBuilder;
    private String aka;

    public Field(String name, QueryBuilder.SelectBuilder selectBuilder) {
        this.name = name;
        this.selectBuilder = selectBuilder;
    }

    public QueryBuilder.SelectBuilder as(String aka) {
        this.aka = aka;
        return selectBuilder;
    }

    public QueryBuilder.SelectBuilder of(String table) {
        name = String.format("%s.%s", table, name);
        return selectBuilder;
    }

    public String build() {
        return String.format("(%s)%s", name, aka == null ? "" : " AS " + aka);
    }
}
