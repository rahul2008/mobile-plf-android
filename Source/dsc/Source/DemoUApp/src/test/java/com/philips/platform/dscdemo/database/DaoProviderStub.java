package com.philips.platform.dscdemo.database;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.CloseableWrappedIterable;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DatabaseResultsMapper;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.ObjectCache;
import com.j256.ormlite.dao.RawRowMapper;
import com.j256.ormlite.dao.RawRowObjectMapper;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.GenericRowMapper;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.PreparedUpdate;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.support.DatabaseResults;
import com.j256.ormlite.table.ObjectFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

class DaoProviderStub implements DaoProvider {
    public List<Class<?>> classes = new ArrayList<>();
    @Override
    public Dao getOrmDao(final Class<?> clazz) throws SQLException {
        classes.add(clazz);
        return new Dao() {
            @Override
            public Object queryForId(final Object o) throws SQLException {
                return null;
            }

            @Override
            public Object queryForFirst(final PreparedQuery preparedQuery) throws SQLException {
                return null;
            }

            @Override
            public List queryForAll() throws SQLException {
                return null;
            }

            @Override
            public List queryForEq(final String fieldName, final Object value) throws SQLException {
                return null;
            }

            @Override
            public List queryForMatching(final Object matchObj) throws SQLException {
                return null;
            }

            @Override
            public List queryForMatchingArgs(final Object matchObj) throws SQLException {
                return null;
            }

            @Override
            public List queryForFieldValues(final Map fieldValues) throws SQLException {
                return null;
            }

            @Override
            public List queryForFieldValuesArgs(final Map fieldValues) throws SQLException {
                return null;
            }

            @Override
            public Object queryForSameId(final Object data) throws SQLException {
                return null;
            }

            @Override
            public QueryBuilder queryBuilder() {
                return null;
            }

            @Override
            public UpdateBuilder updateBuilder() {
                return null;
            }

            @Override
            public DeleteBuilder deleteBuilder() {
                return null;
            }

            @Override
            public List query(final PreparedQuery preparedQuery) throws SQLException {
                return null;
            }

            @Override
            public int create(final Object data) throws SQLException {
                return 0;
            }

            @Override
            public int create(final Collection datas) throws SQLException {
                return 0;
            }

            @Override
            public Object createIfNotExists(final Object data) throws SQLException {
                return null;
            }

            @Override
            public CreateOrUpdateStatus createOrUpdate(final Object data) throws SQLException {
                return null;
            }

            @Override
            public int update(final Object data) throws SQLException {
                return 0;
            }

            @Override
            public int updateId(final Object data, final Object newId) throws SQLException {
                return 0;
            }

            @Override
            public int update(final PreparedUpdate preparedUpdate) throws SQLException {
                return 0;
            }

            @Override
            public int refresh(final Object data) throws SQLException {
                return 0;
            }

            @Override
            public int delete(final Object data) throws SQLException {
                return 0;
            }

            @Override
            public int deleteById(final Object o) throws SQLException {
                return 0;
            }

            @Override
            public int delete(final Collection datas) throws SQLException {
                return 0;
            }

            @Override
            public int deleteIds(final Collection collection) throws SQLException {
                return 0;
            }

            @Override
            public int delete(final PreparedDelete preparedDelete) throws SQLException {
                return 0;
            }

            @Override
            public CloseableIterator iterator() {
                return null;
            }

            @Override
            public CloseableIterator iterator(final int resultFlags) {
                return null;
            }

            @Override
            public CloseableIterator iterator(final PreparedQuery preparedQuery) throws SQLException {
                return null;
            }

            @Override
            public CloseableIterator iterator(final PreparedQuery preparedQuery, final int resultFlags) throws SQLException {
                return null;
            }

            @Override
            public CloseableWrappedIterable getWrappedIterable() {
                return null;
            }

            @Override
            public CloseableWrappedIterable getWrappedIterable(final PreparedQuery preparedQuery) {
                return null;
            }

            @Override
            public void closeLastIterator() throws IOException {

            }

            @Override
            public GenericRawResults<String[]> queryRaw(final String query, final String... arguments) throws SQLException {
                return null;
            }

            @Override
            public GenericRawResults queryRaw(final String query, final RawRowMapper mapper, final String... arguments) throws SQLException {
                return null;
            }

            @Override
            public GenericRawResults queryRaw(final String query, final DataType[] columnTypes, final RawRowObjectMapper mapper, final String... arguments) throws SQLException {
                return null;
            }

            @Override
            public GenericRawResults<Object[]> queryRaw(final String query, final DataType[] columnTypes, final String... arguments) throws SQLException {
                return null;
            }

            @Override
            public GenericRawResults queryRaw(final String query, final DatabaseResultsMapper mapper, final String... arguments) throws SQLException {
                return null;
            }

            @Override
            public long queryRawValue(final String query, final String... arguments) throws SQLException {
                return 0;
            }

            @Override
            public int executeRaw(final String statement, final String... arguments) throws SQLException {
                return 0;
            }

            @Override
            public int executeRawNoArgs(final String statement) throws SQLException {
                return 0;
            }

            @Override
            public int updateRaw(final String statement, final String... arguments) throws SQLException {
                return 0;
            }

            @Override
            public Object callBatchTasks(final Callable callable) throws Exception {
                return null;
            }

            @Override
            public String objectToString(final Object data) {
                return null;
            }

            @Override
            public boolean objectsEqual(final Object data1, final Object data2) throws SQLException {
                return false;
            }

            @Override
            public Object extractId(final Object data) throws SQLException {
                return null;
            }

            @Override
            public Class getDataClass() {
                return null;
            }

            @Override
            public FieldType findForeignFieldType(final Class clazz) {
                return null;
            }

            @Override
            public boolean isUpdatable() {
                return false;
            }

            @Override
            public boolean isTableExists() throws SQLException {
                return false;
            }

            @Override
            public long countOf() throws SQLException {
                return 0;
            }

            @Override
            public long countOf(final PreparedQuery preparedQuery) throws SQLException {
                return 0;
            }

            @Override
            public void assignEmptyForeignCollection(final Object parent, final String fieldName) throws SQLException {

            }

            @Override
            public ForeignCollection getEmptyForeignCollection(final String fieldName) throws SQLException {
                return null;
            }

            @Override
            public void setObjectCache(final boolean enabled) throws SQLException {

            }

            @Override
            public void setObjectCache(final ObjectCache objectCache) throws SQLException {

            }

            @Override
            public ObjectCache getObjectCache() {
                return null;
            }

            @Override
            public void clearObjectCache() {

            }

            @Override
            public Object mapSelectStarRow(final DatabaseResults results) throws SQLException {
                return null;
            }

            @Override
            public GenericRowMapper getSelectStarRowMapper() throws SQLException {
                return null;
            }

            @Override
            public RawRowMapper getRawRowMapper() {
                return null;
            }

            @Override
            public boolean idExists(final Object o) throws SQLException {
                return false;
            }

            @Override
            public DatabaseConnection startThreadConnection() throws SQLException {
                return null;
            }

            @Override
            public void endThreadConnection(final DatabaseConnection connection) throws SQLException {

            }

            @Override
            public void setAutoCommit(final DatabaseConnection connection, final boolean autoCommit) throws SQLException {

            }

            @Override
            public boolean isAutoCommit(final DatabaseConnection connection) throws SQLException {
                return false;
            }

            @Override
            public void commit(final DatabaseConnection connection) throws SQLException {

            }

            @Override
            public void rollBack(final DatabaseConnection connection) throws SQLException {

            }

            @Override
            public ConnectionSource getConnectionSource() {
                return null;
            }

            @Override
            public void setObjectFactory(final ObjectFactory objectFactory) {

            }

            @Override
            public void registerObserver(final DaoObserver observer) {

            }

            @Override
            public void unregisterObserver(final DaoObserver observer) {

            }

            @Override
            public String getTableName() {
                return null;
            }

            @Override
            public void notifyChanges() {

            }

            @Override
            public CloseableIterator closeableIterator() {
                return null;
            }
        };
    }
}
