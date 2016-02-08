package connection;

import connection.exceptions.MalformedSelectException;

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
            if (query.toUpperCase().equals("SELECT"))
                throw new MalformedSelectException();
            add("FROM");
            add(table);
            return new FromBuilder();
        }
    }

    protected class FromBuilder extends SelectBuilder {
    }
}
