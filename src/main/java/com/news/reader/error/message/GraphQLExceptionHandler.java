package com.news.reader.error.message;

import graphql.GraphQLException;
import graphql.kickstart.spring.error.ThrowableGraphQLError;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Component
public class GraphQLExceptionHandler {

    @ExceptionHandler({GraphQLException.class})
    public ThrowableGraphQLError handle(GraphQLException exception) {
        return new ThrowableGraphQLError(exception);
    }

    @ExceptionHandler({RuntimeException.class})
    public ThrowableGraphQLError handle(RuntimeException exception) {
        return new ThrowableGraphQLError(exception, ErrorMessages.INTERNAL_SERVER_ERROR.message());
    }
}
