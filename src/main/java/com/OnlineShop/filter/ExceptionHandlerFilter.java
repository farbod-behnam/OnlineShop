package com.OnlineShop.filter;

import com.OnlineShop.exception.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ExceptionHandlerFilter extends OncePerRequestFilter
{
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        try
        {
            filterChain.doFilter(request, response);
        }
        catch (RuntimeException exception)
        {
            ErrorResponse errorResponse = new ErrorResponse();

            errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            errorResponse.setMessage(exception.getMessage());
            errorResponse.setTimeStamp(System.currentTimeMillis());

            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getWriter().write(asJsonString(errorResponse));
        }
    }

    private String asJsonString(Object object) throws JsonProcessingException
    {
        if (object == null)
            return null;

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);

    }
}
