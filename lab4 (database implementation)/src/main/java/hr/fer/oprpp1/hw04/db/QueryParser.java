package hr.fer.oprpp1.hw04.db;

import hr.fer.oprpp1.hw04.db.lexer.QueryLexer;
import hr.fer.oprpp1.hw04.db.lexer.Token;
import hr.fer.oprpp1.hw04.db.lexer.TokenType;

import java.util.LinkedList;
import java.util.List;

/**
 * Parser that performs syntactical analysis of a query.
 *
 * @author Danijel Barišić
 */
public class QueryParser {

    /**
     * Lexer to get tokens from.
     */
    private QueryLexer lexer;

    /**
     * List of conditional expressions that is built by parsing the query text.
     */
    private List<ConditionalExpression> query;

    /**
     * Executes lexical and syntactical analysis.
     *
     * @param queryString string to perform analysis on
     * @throws QueryParserException when a parsing related error occurs (unknown attribute, unexpected token...)
     */
    public QueryParser(String queryString) {

        if (queryString == null) {
            throw new QueryParserException("Query text cannot be null.");
        }

        try {
            lexer = new QueryLexer(queryString);
            query = new LinkedList<>();
            parse();
        } catch (Exception e) {
            throw new QueryParserException(e.getMessage());
        }
    }

    /**
     * Performs the syntactical analysis.
     *
     * @throws QueryParserException when a parsing related error occurs (unknown attribute, unexpected token...)
     */
    private void parse() {

        lexer.nextToken();

        while (!(lexer.getToken().getType().equals(TokenType.EOL))) {

            if (lexer.getToken().getType().equals(TokenType.ATTRIBUTE)) {

                IFieldValueGetter fieldGetter = switch (lexer.getToken().getValue()) {
                    case "jmbag" -> FieldValueGetters.JMBAG;
                    case "firstName" -> FieldValueGetters.FIRST_NAME;
                    case "lastName" -> FieldValueGetters.LAST_NAME;
                    default -> throw new QueryParserException("Invalid attribute.");
                };

                lexer.nextToken();

                IComparisonOperator operator;
                if (lexer.getToken().equals(new Token(TokenType.OPERATOR, "="))) {
                    operator = ComparisonOperators.EQUALS;
                } else if (lexer.getToken().equals(new Token(TokenType.OPERATOR, "!="))) {
                    operator = ComparisonOperators.NOT_EQUALS;
                } else if (lexer.getToken().equals(new Token(TokenType.OPERATOR, "<="))) {
                    operator = ComparisonOperators.LESS_OR_EQUALS;
                } else if (lexer.getToken().equals(new Token(TokenType.OPERATOR, ">="))) {
                    operator = ComparisonOperators.GREATER_OR_EQUALS;
                } else if (lexer.getToken().equals(new Token(TokenType.OPERATOR, "<"))) {
                    operator = ComparisonOperators.LESS;
                } else if (lexer.getToken().equals(new Token(TokenType.OPERATOR, ">"))) {
                    operator = ComparisonOperators.GREATER;
                } else if (lexer.getToken().equals(new Token(TokenType.OPERATOR, "LIKE"))) {
                    operator = ComparisonOperators.LIKE;
                } else {
                    throw new QueryParserException("Invalid operator.");
                }

                lexer.nextToken();

                if (!lexer.getToken().getType().equals(TokenType.LITERAL)) {
                    throw new QueryParserException("Unexpected token, literal expected.");
                }

                String literal = lexer.getToken().getValue();

                ConditionalExpression expr = new ConditionalExpression(fieldGetter, literal, operator);

                query.add(expr);

                lexer.nextToken();
            } else if (lexer.getToken().getType().equals(TokenType.AND)) {

                lexer.nextToken();
                if (lexer.getToken().getType().equals(TokenType.EOL)) {
                    throw new QueryParserException("Abrupt query ending after AND.");
                }
            } else {
                throw new QueryParserException("Unexpected token.");
            }
        }
    }

    /**
     * @return true if there is only a single query condition, of form "jmbag = jmbagLiteral"
     * (i.e. jmbag is being compared to a literal via equals operator), false otherwise
     */
    public boolean isDirectQuery() {
        return query.size() == 1
                && query.get(0).getFieldGetter().equals(FieldValueGetters.JMBAG)
                && query.get(0).getComparisonOperator().equals(ComparisonOperators.EQUALS);
    }

    /**
     * @return JMBAG of the parsed direct query
     * @throws IllegalStateException if query isn't direct (i.e. isDirectQuery() == false)
     */
    public String getQueriedJMBAG() {
        if (!isDirectQuery()) {
            throw new IllegalStateException("Query isn't direct.");
        }

        return query.get(0).getStringLiteral();
    }

    /**
     * @return list of conditional expressions of the parsed query
     */
    public List<ConditionalExpression> getQuery() {
        return query;
    }
}
