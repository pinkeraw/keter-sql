package dev.keter;

import com.google.common.collect.ImmutableSet;
import dev.keter.parser.ParsingException;
import dev.keter.parser.ParsingOptions;
import dev.keter.parser.SqlBaseLexer;
import dev.keter.parser.SqlParser;
import dev.keter.tree.Identifier;
import org.antlr.v4.runtime.Vocabulary;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Strings.nullToEmpty;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static java.lang.String.format;

public final class ReservedIdentifiers {
    private static final Pattern IDENTIFIER = Pattern.compile("'([A-Z_]+)'");
    private static final Pattern TABLE_ROW = Pattern.compile("``([A-Z_]+)``.*");
    private static final String TABLE_PREFIX = "============================== ";

    private static final SqlParser PARSER = new SqlParser();

    private ReservedIdentifiers() {
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) {
        if ((args.length == 2) && args[0].equals("validateDocs")) {
            try {
                validateDocs(Paths.get(args[1]));
            } catch (Throwable t) {
                t.printStackTrace();
                System.exit(100);
            }
        } else {
            for (String name : reservedIdentifiers()) {
                System.out.println(name);
            }
        }
    }

    private static void validateDocs(Path path)
            throws IOException {
        System.out.println("Validating " + path);
        List<String> lines = Files.readAllLines(path);

        if (lines.stream().filter(s -> s.startsWith(TABLE_PREFIX)).count() != 3) {
            throw new RuntimeException("Failed to find exactly one table");
        }

        Iterator<String> iterator = lines.iterator();

        // find table and skip header
        while (!iterator.next().startsWith(TABLE_PREFIX)) {
            // skip
        }
        if (iterator.next().startsWith(TABLE_PREFIX)) {
            throw new RuntimeException("Expected to find a header line");
        }
        if (!iterator.next().startsWith(TABLE_PREFIX)) {
            throw new RuntimeException("Found multiple header lines");
        }

        Set<String> reserved = reservedIdentifiers();
        Set<String> found = new HashSet<>();
        while (true) {
            String line = iterator.next();
            if (line.startsWith(TABLE_PREFIX)) {
                break;
            }

            Matcher matcher = TABLE_ROW.matcher(line);
            if (!matcher.matches()) {
                throw new RuntimeException("Invalid table line: " + line);
            }
            String name = matcher.group(1);

            if (!reserved.contains(name)) {
                throw new RuntimeException("Documented identifier is not reserved: " + name);
            }
            if (!found.add(name)) {
                throw new RuntimeException("Duplicate documented identifier: " + name);
            }
        }

        for (String name : reserved) {
            if (!found.contains(name)) {
                throw new RuntimeException("Reserved identifier is not documented: " + name);
            }
        }

        System.out.println(format("Validated %s reserved identifiers", reserved.size()));
    }

    public static Set<String> reservedIdentifiers() {
        return sqlKeywords().stream()
                .filter(ReservedIdentifiers::reserved)
                .sorted()
                .collect(toImmutableSet());
    }

    public static Set<String> sqlKeywords() {
        ImmutableSet.Builder<String> names = ImmutableSet.builder();
        Vocabulary vocabulary = SqlBaseLexer.VOCABULARY;
        for (int i = 0; i <= vocabulary.getMaxTokenType(); i++) {
            String name = nullToEmpty(vocabulary.getLiteralName(i));
            Matcher matcher = IDENTIFIER.matcher(name);
            if (matcher.matches()) {
                names.add(matcher.group(1));
            }
        }
        return names.build();
    }

    private static boolean reserved(String name) {
        try {
            return !(PARSER.createExpression(name, new ParsingOptions()) instanceof Identifier);
        } catch (ParsingException ignored) {
            return true;
        }
    }
}
