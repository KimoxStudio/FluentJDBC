package connection.builder;

import connection.builder.helpers.*;
import connection.exceptions.MalformedSelectException;

import java.util.ArrayList;

public class QueryBuilder {

    protected String query;

    public QueryBuilder() {

    }

    public SelectBuilder select(){
        query = "SELECT";
        return new SelectBuilder();
    }

    protected void add(String query){
        this.query += " " + query;
    }

    public String query() {
        return query;
    }

    public class SelectBuilder {
        private ArrayList<Field> fields = new ArrayList<>();

        public AllBuilder all(){
            insert("*");
            return new AllBuilder();
        }

        public FromBuilder from(String table) throws MalformedSelectException {
            if (fields.size() == 0)
                throw new MalformedSelectException();
            build();
            add("FROM");
            add(table);
            return new FromBuilder(QueryBuilder.this);
        }

        public FromAs from(Query query) throws MalformedSelectException {
            if (fields.size() == 0)
                throw new MalformedSelectException();
            build();
            add("FROM");
            add("(" + query.query() + ")");
            return new FromAs() {
                @Override
                public FromBuilder as(String aka) {
                    add("AS " + aka);
                    return new FromBuilder(QueryBuilder.this);
                }
            };
        }

        private void build() {
            for (Field field : fields) {
                add(field.build());
                if(!fields.get(fields.size() -1).equals(field))
                    add(",");
            }
        }

        public Field field(String name) {
            return insert(name);
        }

        public Field field(connection.builder.helpers.Query query) {
            return insert(String.format("(%s)", query.query()));
        }

        private Field insert(String name) {
            Field field = new Field(name, this);
            fields.add(field);
            return field;
        }

        public IfBuilder condition(Query query) {
            return new IfBuilder(query.query());
        }

        public class AllBuilder implements Parameter, From {

            public SelectBuilder of(String table) throws MalformedSelectException {
                if (fields.size() == 0) throw new MalformedSelectException();
                fields.get(fields.size() - 1).of(table);
                return SelectBuilder.this;
            }

            @Override
            public Field field(String name) {
                return SelectBuilder.this.field(name);
            }

            @Override
            public FromBuilder from(String table) throws MalformedSelectException {
                return SelectBuilder.this.from(table);
            }

            public FromAs from(Query query) throws MalformedSelectException {
                return SelectBuilder.this.from(query);
            }
        }

        public class IfBuilder {

            private String query;
            private String sentence;

            public IfBuilder(String sentence) {
                this.sentence = sentence;
                this.query = String.format("IF((%s)", sentence);
            }

            public ConditionBuilder isMoreThan(String value) {
                this.query += String.format(" > %s",value);
                return new ConditionBuilder();
            }

            public ConditionBuilder isLessThan(String value) {
                this.query += String.format(" < %s",value);
                return new ConditionBuilder();
            }

            public ConditionBuilder isLessOrEqualsThan(String value) {
                this.query += String.format(" <= %s",value);
                return new ConditionBuilder();
            }

            public ConditionBuilder isMoreOrEqualsThan(String value) {
                this.query += String.format(" >= %s",value);
                return new ConditionBuilder();
            }

            public ConditionBuilder isEqualThan(String value) {
                this.query += String.format(" LIKE %s",value);
                return new ConditionBuilder();
            }

            public ConditionBuilder isLikeThan(String pattern) {
                this.query += String.format(" LIKE %s",pattern);
                return new ConditionBuilder();
            }

            public ConditionBuilder isDistinctOf(String value) {
                this.query += String.format(" <> %s",value);
                return new ConditionBuilder();
            }

            public class ConditionBuilder {

                public ifNot then(String yesCondition) {
                    IfBuilder.this.query += String.format(",\"%s\"", yesCondition);
                    return new ifNot() {
                        @Override
                        public SelectAs ifNot(final String noCondition) {
                            IfBuilder.this.query += String.format(",\"%s\")", noCondition);
                            return new SelectAs() {
                                @Override
                                public SelectBuilder as(String aka) {
                                    return SelectBuilder.this.field(IfBuilder.this.query).as(aka);
                                }
                            };
                        }
                    };
                }

                public IfBuilder and() {
                    query += String.format(" AND (%s)",sentence);
                    return IfBuilder.this;
                }

                public IfBuilder and(Query sentence) {
                    IfBuilder.this.sentence = sentence.query();
                    return and();
                }

                public IfBuilder or() {
                    query += String.format(" OR (%s)",sentence);
                    return IfBuilder.this;
                }

                public IfBuilder or(Query sentence) {
                    IfBuilder.this.sentence = sentence.query();
                    return or();
                }
            }
        }
    }
    public interface ifNot{
         SelectAs ifNot(String noCondition);
    }

    public class Result extends SelectBuilder {
        public String query(){
            return query;
        }
    }
}
