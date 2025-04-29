package com.example.groupprojectapplication;

import java.util.Objects;
// @author Jack
public class SearchToken {

    public enum Type {DayField,TimeField, TagField, TAG, GREATERTHAN, LESSTHAN, TIME, DAY, LISTMARK, NEXTMARK,CONTAINS, ALTSORT }

    /**
     * The following exception should be thrown if a tokenizer attempts to tokenize something that is not of one
     * of the types of tokens.
     */
    public static class IllegalTokenException extends IllegalArgumentException {
        public IllegalTokenException(String errorMessage) {
            super(errorMessage);
        }
    }

    // Fields of the class Token.
    private final String token; // Token representation in String form.
    private final Type type;    // Type of the token.

    public SearchToken(String token, Type type) {
        this.token = token;
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        if (type == Type.TAG) {
            return "TAG(" + token + ")";
        } else if (type == Type.DAY){
            return "DAY(" + token + ")";
        }else if (type == Type.TIME){
            return "TIME(" + token + ")";
        }else {
            return type + "";
        }
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true; // Same hashcode.
        if (!(other instanceof SearchToken)) return false; // Null or not the same type.
        return this.type == ((SearchToken) other).getType() && this.token.equals(((SearchToken) other).getToken()); // Values are the same.
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, type);
    }
}
