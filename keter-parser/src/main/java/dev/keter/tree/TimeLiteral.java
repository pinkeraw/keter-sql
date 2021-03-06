package dev.keter.tree;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class TimeLiteral extends Literal {
    private final String value;

    public TimeLiteral(String value) {
        this(null, value);
    }

    public TimeLiteral(NodeLocation location, String value) {
        super(location);
        requireNonNull(value, "value is null");
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public <R, C> R accept(AstVisitor<R, C> visitor, C context) {
        return visitor.visitTimeLiteral(this, context);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TimeLiteral that = (TimeLiteral) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean shallowEquals(Node other) {
        if (!sameClass(this, other)) {
            return false;
        }

        TimeLiteral otherLiteral = (TimeLiteral) other;
        return Objects.equals(this.value, otherLiteral.value);
    }
}
