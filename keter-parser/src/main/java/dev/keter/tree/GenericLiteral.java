package dev.keter.tree;

import dev.keter.parser.ParsingException;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public final class GenericLiteral
        extends Literal {
    private final String type;
    private final String value;

    public GenericLiteral(String type, String value) {
        this(null, type, value);
    }

    public GenericLiteral(NodeLocation location, String type, String value) {
        super(location);
        requireNonNull(type, "type is null");
        requireNonNull(value, "value is null");
        if (type.equalsIgnoreCase("X")) {
            // we explicitly disallow "X" as type name, so if the user arrived here,
            // it must be because that he intended to give a binaryLiteral instead, but
            // added whitespace between the X and quote
            throw new ParsingException("Spaces are not allowed between 'X' and the starting quote of a binary literal", location);
        }
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public <R, C> R accept(AstVisitor<R, C> visitor, C context) {
        return visitor.visitGenericLiteral(this, context);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, type);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        GenericLiteral other = (GenericLiteral) obj;
        return Objects.equals(this.value, other.value) &&
                Objects.equals(this.type, other.type);
    }

    @Override
    public boolean shallowEquals(Node other) {
        if (!sameClass(this, other)) {
            return false;
        }

        GenericLiteral otherLiteral = (GenericLiteral) other;

        return value.equals(otherLiteral.value) && type.equals(otherLiteral.type);
    }
}
