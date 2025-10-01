package com.safeStatusNotifier.safeStatusNotifier.config;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GraphQLGlobalExceptionHandler {
    @GraphQlExceptionHandler
    public GraphQLError handleRuntimeException(RuntimeException ex){
        return GraphqlErrorBuilder.newError().message(ex.getMessage()).build();
    }
}
