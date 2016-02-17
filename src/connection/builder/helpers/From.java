package connection.builder.helpers;

import connection.builder.FromBuilder;
import connection.exceptions.MalformedSelectException;

public interface From {
    FromBuilder from(String table) throws MalformedSelectException;
}
