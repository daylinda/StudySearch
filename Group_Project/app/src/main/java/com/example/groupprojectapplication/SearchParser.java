package com.example.groupprojectapplication;

import java.util.LinkedHashSet;
import java.util.Scanner;

// Skeleton taken from the labs, edited for my own amusement (Jack)
// @author Jack

/**
 * Alright like in the lab, I am going to lay out the grammar that I want this to have, for ease of creation
 * <search>    ::=  <query> | <query> ; <search>
 * <query>   ::=  <TimeField> <timeCondition> | <DayField> : <dayList> | <TagField> : <tagList> | <ALTSORT>
 * <timeCondition> ::=  :<timeList>| > <Time> |< <Time>
 * <dayList> ::= <Day> | <Day> , <dayList>
 * <tagList> ::= <Tag> | <Tag> , <tagList>
 * <timeList> ::= <Time> | <Time>, <timeList>
 *
 */
public class SearchParser {
    // Illegal error, Thanks Llew!
    public static class IllegalProductionException extends IllegalArgumentException {
        public IllegalProductionException(String errorMessage) {
            super(errorMessage);
        }
    }

    // The tokenizer (class field) this parser will use.
    SearchTokeniser tokenizer;

    public SearchParser(SearchTokeniser tokenizer) {
        this.tokenizer = tokenizer;
    }

    //Main method kept for useful testing!
    public static void main(String[] args) {
        // Create a scanner to get the user's input.
        Scanner scanner = new Scanner(System.in);
        // Then input your string for testing!
        //quit with q or cntl x
        System.out.println("Provide a search string to be parsed:");
        while (scanner.hasNext()) {
            String input = scanner.nextLine();

            // Check if 'quit' is provided.
            if (input.equals("q"))
                break;

            // Create an instance of the tokenizer.
            SearchTokeniser tokenizer = new SearchTokeniser(input);

            // Print out the expression from the parser.
            SearchParser parser = new SearchParser(tokenizer);
            Condition condition = Condition.getInstance();
            parser.parseSearch();
            System.out.println("Parsing: " + condition.show());
        }
    }
//    public Condition addCondition(Condition firstCondition, Condition secondCondition){
//        Condition combinedCondition = new Condition(firstCondition.days, firstCondition.lessThanTime, firstCondition.greaterThanTime,
//                firstCondition.equalsToTime, firstCondition.tags);
//        combinedCondition.addDays(secondCondition.days);
//        combinedCondition.addTags(secondCondition.tags);
//        combinedCondition.updateTime(secondCondition.equalsToTime, secondCondition.greaterThanTime, secondCondition.lessThanTime);
//        return combinedCondition;
//    }
    /**
     * Adheres to the grammar rule:
     * <search>   ::=  <query> | <query> ; <search>
     *
     * @return type: Condition
     */

    public void parseSearch(){
        if (!tokenizer.hasNext())
            return;

        parseQuery();
        if (!tokenizer.hasNext()){
            return;
        }
        else if (tokenizer.current().getType().equals(SearchToken.Type.NEXTMARK)) {
            tokenizer.next();
            if (!tokenizer.hasNext()){
                throw new IllegalProductionException("NextMark without next");
            }
            parseSearch();
            return;
        }
        else
            throw new IllegalProductionException("Error from Parse Search, Query Continued without next mark: " + tokenizer.current().getToken() );
    }

    /**
     * Adheres to the grammar rule:
     * <query>   ::=  <TimeField> <timeCondition> | <DayField> : <dayList> | <TagField> : <tagList>
     *
     * @return type: Condition.
     */
    public void parseQuery(){
        if (!tokenizer.hasNext())
            throw new IllegalProductionException("Attempted to parse Query but the token string ended");

        if (tokenizer.current().getType().equals(SearchToken.Type.TimeField)){
            tokenizer.next();
            parseTimeCondition();
            return;
        } else if (tokenizer.current().getType().equals(SearchToken.Type.DayField)){
            tokenizer.next();
            if (!tokenizer.hasNext()){
                throw new IllegalProductionException("Day field ended before contains was given");
            }
            if (tokenizer.current().getType().equals(SearchToken.Type.CONTAINS)) {
                tokenizer.next();
                parseDayList();
                return;
            }
            else
                throw new IllegalProductionException("Failed to make a contains mark after Day: " + tokenizer.current().getToken());
        }else if (tokenizer.current().getType().equals(SearchToken.Type.TagField)){
            tokenizer.next();
            if (!tokenizer.hasNext()){
                throw new IllegalProductionException("Tag field ended before contains was given");
            }
            if (tokenizer.current().getType().equals(SearchToken.Type.CONTAINS)){
                tokenizer.next();
                parseTagList();
                return;
            }
            else
                throw new IllegalProductionException("Failed to make a contains mark after Tag: " + tokenizer.current().getToken());
        }else if (tokenizer.current().getType().equals(SearchToken.Type.ALTSORT)){
            tokenizer.next();
            Condition.getInstance().setAltsortTrue();
            return;
        } else
            throw new IllegalProductionException("Parse Query did not begin with a valid Field: " + tokenizer.current().getToken());
    }
    /**
     * Adheres to the grammar rule:
     * <timeCondition> ::=  :<timeList>| > <Time> |< <Time>
     *
     * @return type: Condition
     */
    public void parseTimeCondition(){
        if (!tokenizer.hasNext()){
            throw new IllegalProductionException("Time condition was empty");
        }

        if (tokenizer.current().getType().equals(SearchToken.Type.CONTAINS)){
            tokenizer.next();
            if (!tokenizer.hasNext()){
                throw new IllegalProductionException("Contains symbol without a value");
            }
            parseTimeList();
            return;
        } else if (tokenizer.current().getType().equals(SearchToken.Type.GREATERTHAN)){
            tokenizer.next();
            if (!tokenizer.hasNext()){
                throw new IllegalProductionException("GreaterThan symbol without a value");
            }
            else if (tokenizer.current().getType().equals(SearchToken.Type.TIME)){
                Condition.getInstance().updateGreaterThan(Integer.parseInt(tokenizer.current().getToken().toString()));
                tokenizer.next();
                return;
            }
            else
                throw new IllegalProductionException("GreaterThan symbol then token which was not time: " + tokenizer.current().getToken());
        } else if (tokenizer.current().getType().equals(SearchToken.Type.LESSTHAN)){
            tokenizer.next();
            if (!tokenizer.hasNext()){
                throw new IllegalProductionException("LessThan symbol without a value");
            }
            else if (tokenizer.current().getType().equals(SearchToken.Type.TIME)) {
                Condition.getInstance().updateLessThan(Integer.parseInt(tokenizer.current().getToken().toString()));
                tokenizer.next();
                return;
            }
            else
                throw new IllegalProductionException("LessThan symbol then token which was not time: " + tokenizer.current().getToken());
        } else
            throw new IllegalProductionException("Time condition did not have an appropriate comparator symbol: " + tokenizer.current().getToken());
    }
    /**
     * Adheres to the grammar rule:
     * <timeList> ::= <Time> | <Time>, <timeList>
     *
     * @return type: LinkedHashSet<Integer>
     */
    public void parseTimeList(){
        if (!tokenizer.hasNext()){
            throw new IllegalProductionException("TimeList was null unexpectedly");
        }
        if (tokenizer.current().getType().equals(SearchToken.Type.TIME)){
//            LinkedHashSet<Integer> timeList = new LinkedHashSet<>();
//            timeList.add(Integer.parseInt(tokenizer.current().getToken().toString()));
            Condition.getInstance().updateEqualToTime(Integer.parseInt(tokenizer.current().getToken().toString()));
            tokenizer.next();
            if (!tokenizer.hasNext()){
                return;
            }
            if (tokenizer.current().getType().equals(SearchToken.Type.LISTMARK)){
                tokenizer.next();
                parseTimeList();
                return;
            }
            return;
        }
        else
            throw new IllegalProductionException("TimeList contains objects which are not Time: " + tokenizer.current().getToken());
    }
    /**
     * Adheres to the grammar rule:
     * <tagList> ::= <Tag> | <Tag> , <tagList>
     *
     * @return type: LinkedHashSet<Integer>
     */
    public void parseTagList(){
        if (!tokenizer.hasNext()){
            throw new IllegalProductionException("TagList was null unexpectedly");
        }
        if (tokenizer.current().getType().equals(SearchToken.Type.TAG)){
//            LinkedHashSet<SearchToken> tagList = new LinkedHashSet<>();
//            tagList.add(tokenizer.current());
            Condition.getInstance().addTag(tokenizer.current());
            tokenizer.next();
            if (!tokenizer.hasNext()){
                return;
            }
            if (tokenizer.current().getType().equals(SearchToken.Type.LISTMARK)){
                tokenizer.next();
                parseTagList();
                return;
            }
            return;
        }
        else
            throw new IllegalProductionException("TagList contains objects which are not Tags: " + tokenizer.current().getToken());
    }
    /**
     * Adheres to the grammar rule:
     * <dayList> ::= <Day> | <Day> , <dayList>
     *
     * @return type: LinkedHashSet<Integer>
     */
    public void parseDayList(){
        if (!tokenizer.hasNext()){
            throw new IllegalProductionException("Daylist was null unexpectedly");
        }
        if (tokenizer.current().getType().equals(SearchToken.Type.DAY)){
//            LinkedHashSet<SearchToken> dayList = new LinkedHashSet<>();
//            dayList.add(tokenizer.current());
            Condition.getInstance().addDay(tokenizer.current());
            tokenizer.next();
            if (!tokenizer.hasNext()){
                return;
            }
            if (tokenizer.current().getType().equals(SearchToken.Type.LISTMARK)){
                tokenizer.next();
                parseDayList();
                return;
            }
            return;
        }
        else
            throw new IllegalProductionException("Daylist contains objects which are not days: " + tokenizer.current().getToken());
    }

}
