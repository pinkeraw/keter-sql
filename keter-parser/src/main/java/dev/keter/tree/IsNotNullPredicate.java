package dev.keter.tree;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class IsNotNullPredicate extends Predicate {
    private final Expression value;

    public IsNotNullPredicate(Expression value) {
        this(null, value);
    }

    public IsNotNullPredicate(NodeLocation location, Expression value) {
        super(location);
        requireNonNull(value, "value is null");
        this.value = value;
    }

    public Expression getValue() {
        return value;
    }

    @Override
    public <R, C> R accept(AstVisitor<R, C> visitor, C context) {
        return visitor.visitIsNotNullPredicate(this, context);
    }

    @Override
    public List<Node> getChildren() {
        return ImmutableList.of(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IsNotNullPredicate that = (IsNotNullPredicate) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean shallowEquals(Node other) {
        return sameClass(this, other);
    }
}
