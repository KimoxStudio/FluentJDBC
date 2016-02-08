package connection;

import connection.exceptions.MalformedSelectException;

import java.util.ArrayList;

public class QueryBuilder {

    protected static String query;

    protected QueryBuilder() {

    }

    public SelectBuilder select(){
        query = "SELECT";
        return new SelectBuilder();
    }

    protected void add(String query){
        QueryBuilder.query += " " + query;
    }


    protected class SelectBuilder {
        private ArrayList<Field> fields = new ArrayList<>();

        public SelectBuilder all(){
            add("*");
            return this;
        }

        public SelectBuilder all(String table){
            add(table + ".*");
            return this;
        }

        public SelectBuilder values(String values) {
            add(values);
            return this;
        }

        public SelectBuilder from(String table) throws MalformedSelectException {
            if (fields.size() == 0)
                throw new MalformedSelectException();
            build();
            add("FROM");
            add(table);
            return new FromBuilder();
        }

        private void build() {
            for (Field field : fields) {
                add(field.build());
                if(!fields.get(fields.size() -1).equals(field))
                    add(",");
            }
        }

        public Field parameter(String name) {
            Field field = new Field(name, this);
            fields.add(field);
            return field;
        }
    }

    protected class FromBuilder extends SelectBuilder {
    }
}
