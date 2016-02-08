package connection;

import connection.exceptions.MalformedSelectException;

import java.util.ArrayList;

public class QueryBuilder {

    protected String query;

    protected QueryBuilder() {

    }

    public SelectBuilder select(){
        query = "SELECT";
        return new SelectBuilder();
    }

    protected void add(String query){
        this.query += " " + query;
    }


    protected class SelectBuilder {
        private ArrayList<Field> fields = new ArrayList<>();

        public AllBuilder all(){
            field("*");
            return new AllBuilder();
        }

        public EndQuery from(String table) throws MalformedSelectException {
            if (fields.size() == 0)
                throw new MalformedSelectException();
            build();
            add("FROM");
            add(table);
            return new EndQuery();
        }

        private void build() {
            for (Field field : fields) {
                add(field.build());
                if(!fields.get(fields.size() -1).equals(field))
                    add(",");
            }
        }

        public Field parameter(String name) {
            return field(name);
        }
        public Field parameter(EndQuery query) {
            return field(String.format("(%s)", query.query()));
        }

        private Field field(String name) {
            Field field = new Field(name, this);
            fields.add(field);
            return field;
        }

        class AllBuilder implements Parameter {

            public SelectBuilder of(String table) {
                fields.get(fields.size() - 1).of(table);
                return SelectBuilder.this;
            }

            @Override
            public Field parameter(String name) {
                return SelectBuilder.this.parameter(name);
            }

            @Override
            public EndQuery from(String table) throws MalformedSelectException {
                return SelectBuilder.this.from(table);
            }
        }
    }

    protected class EndQuery extends SelectBuilder {
        public String query(){
            return query;
        }
    }
}
