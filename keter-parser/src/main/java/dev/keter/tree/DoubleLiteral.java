package dev.keter.tree;

import static java.util.Objects.requireNonNull;

public class DoubleLiteral extends Literal {
    private final double value;

    public DoubleLiteral(String value) {
        this(null, value);
    }

    public DoubleLiteral(NodeLocation location, String value) {
        super(location);
        requireNonNull(value, "value is null");
        this.value = Double.parseDouble(value);
    }

    public double getValue() {
        return value;
    }

    @Override
    public <R, C> R accept(AstVisitor<R, C> visitor, C context) {
        return visitor.visitDoubleLiteral(this, context);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DoubleLiteral that = (DoubleLiteral) o;

        if (Double.compare(that.value, value) != 0) {
            return false;
        }

        return true;
    }

    @SuppressWarnings("UnaryPlus")
    @Override
    public int hashCode() {
        long temp = value != +0.0d ? Double.doubleToLongBits(value) : 0L;
        return (int) (temp ^ (temp >>> 32));
    }

    @Override
    public boolean shallowEquals(Node other) {
        if (!sameClass(this, other)) {
            return false;
        }

        return value == ((DoubleLiteral) other).value;
    }
}
