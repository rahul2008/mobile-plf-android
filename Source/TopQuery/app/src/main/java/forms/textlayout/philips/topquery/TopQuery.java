package forms.textlayout.philips.topquery;

import android.support.annotation.NonNull;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class TopQuery {
    public final static int DB_ACCESS = 1;
    public final static int DB_ORACLE = 2;
    public final static int DB_SQL_SERVER = 3;
    public final static int DB_SYBASE = 4;
    public final static int DB_OTHER = 5;

    private int dbType = -1;
    Query query = null;

    public static final int NO_MAXIMUM_LIMIT = -1;
    private int maxResults = NO_MAXIMUM_LIMIT;

    private boolean isValidDBType(int db) {
        switch(db) {
            case DB_ACCESS     : return true;
            case DB_ORACLE     : return true;
            case DB_SQL_SERVER : return true;
            case DB_SYBASE     : return true;
            case DB_OTHER      : return true;
            default : return false;
        }
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = (maxResults < 0) ? NO_MAXIMUM_LIMIT : maxResults;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public int getDbType() {
        return dbType;
    }

    public void setDbType(int dbType) {
        query = createQuery(dbType);
    }

    public String query(final String sql) {
        if(noLimit())
            return sql;

        return this.query.topQuery(sql, maxResults);
    }

    @NonNull
    private Query createQuery(final int dbType) {
        //final Query q;
        switch(dbType) {
            case DB_ACCESS:
                return new Access();
            case DB_SYBASE:
            case DB_SQL_SERVER:
                new SybaseSqlServer();
            case DB_ORACLE:
                new Oracle();
            case DB_OTHER:
            default :
                return new Default();
        }
    }

    private boolean noLimit() {
        return maxResults == NO_MAXIMUM_LIMIT;
    }
}
