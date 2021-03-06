package dev.keter.tree;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Objects;

public class InPredicate extends Predicate {
    private final Expression value;
    private final Expression valueList;

    public InPredicate(Expression value, Expression valueList) {
        this(null, value, valueList);
    }

    public InPredicate(NodeLocation location, Expression value, Expression valueList) {
        super(location);
        this.value = value;
        this.valueList = valueList;
    }

    public Expression getValue() {
        return value;
    }

    public Expression getValueList() {
        return valueList;
    }

    @Override
    public <R, C> R accept(AstVisitor<R, C> visitor, C context) {
        return visitor.visitInPredicate(this, context);
    }

    @Override
    public List<Node> getChildren() {
        return ImmutableList.of(value, valueList);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InPredicate that = (InPredicate) o;
        return Objects.equals(value, that.value) &&
                Objects.equals(valueList, that.valueList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, valueList);
    }

    @Override
    public boolean shallowEquals(Node other) {
        return sameClass(this, other);
    }
}
