package dev.keter.tree;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;

public class TableSubquery extends QueryBody {
    private final Query query;

    public TableSubquery(Query query) {
        this(null, query);
    }

    public TableSubquery(NodeLocation location, Query query) {
        super(location);
        this.query = query;
    }

    public Query getQuery() {
        return query;
    }

    @Override
    public <R, C> R accept(AstVisitor<R, C> visitor, C context) {
        return visitor.visitTableSubquery(this, context);
    }

    @Override
    public List<Node> getChildren() {
        return ImmutableList.of(query);
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .addValue(query)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TableSubquery tableSubquery = (TableSubquery) o;
        return Objects.equals(query, tableSubquery.query);
    }

    @Override
    public int hashCode() {
        return query.hashCode();
    }

    @Override
    public boolean shallowEquals(Node other) {
        return sameClass(this, other);
    }
}
