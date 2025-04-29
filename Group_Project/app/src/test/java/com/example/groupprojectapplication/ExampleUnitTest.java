package com.example.groupprojectapplication;

import static com.example.groupprojectapplication.ListAVLTree.deleteNode;
import static com.example.groupprojectapplication.ListAVLTree.getBalance;
import static com.example.groupprojectapplication.ListAVLTree.preOrder;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.groupprojectapplication.model.News;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.logging.Filter;

// @author Jack

public class ExampleUnitTest {

    public String makeString(ListAVLTree.node root) {
        if (root == null) {
            return "{}";
        }
        String ret = "{value=";
        for (News n : root.getTopicList()) {
            ret += n.getTopic() + ",";
        }
        ret += " leftNode=" + makeString(root.left);
        ret += ", rightNode=" + makeString(root.right);
        ret += "}";
        return ret;
    }

    @Test(timeout = 1000)
    public void insertTest1() {
        ListAVLTree.node root = null;
        root = ListAVLTree.insert(root, new News("5", "5", String.valueOf(5), "User1"));
        String expected = "{value=5, leftNode={}, rightNode={}}";
        assertEquals("\nAVL tree implementation is not immutable" +
                        "\nYour AVL tree should look like: " + expected + "\nBut it actually looks like: " + makeString(root),
                expected, makeString(root));
    }

    @Test(timeout = 1000)
    public void insertTest2() {
        ListAVLTree.node root = null;
        root = ListAVLTree.insert(root, new News("1", "1", String.valueOf(1), "User1"));
        root = ListAVLTree.insert(root, new News("15", "15", String.valueOf(15), "User1"));
        root = ListAVLTree.insert(root, new News("45", "45", String.valueOf(45), "User1"));

        String expected = "{value=15, leftNode={value=1, leftNode={}, rightNode={}}, rightNode={value=45, leftNode={}, rightNode={}}}";
        assertEquals("\nAVL tree implementation is not immutable" +
                        "\nYour AVL tree should look like: " + expected + "\nBut it actually looks like: " + makeString(root),
                expected, makeString(root));
    }

    @Test(timeout = 1000)
    public void insertInOrderTest() {
        // Simply check if the insertion correctly places values
        ListAVLTree.node root = null;
        root = ListAVLTree.insert(root, new News("5", "5", String.valueOf(5), "User1"));
        root = ListAVLTree.insert(root, new News("10", "10", String.valueOf(10), "User1"));
        String expected = "{value=5, leftNode={}, rightNode={value=10, leftNode={}, rightNode={}}}";
        assertNotNull(
                "\nInsertion does not properly position values" +
                        "\nYour AVL tree should look like: " + expected + "\nBut it actually looks like: " + makeString(root),
                root.right.getTopicList().get(0));
        assertEquals(
                "\nInsertion does not properly position values" +
                        "\nYour AVL tree should look like: " + expected + "\nBut it actually looks like: " + makeString(root)
                ,
                10, (int) Integer.valueOf(root.right.getTopicList().get(0).getTopic()));

        root = ListAVLTree.insert(root, new News("1", "1", String.valueOf(1), "User1"));
        expected = "{value=5, leftNode={value=1, leftNode={}, rightNode={}}, rightNode={value=10, leftNode={}, rightNode={}}}";
        assertNotNull(
                "\nInsertion does not properly position values" +
                        "\nYour AVL tree should look like: " + expected + "\nBut it actually looks like: " + makeString(root),
                root.left.getTopicList().get(0));
        assertEquals(
                "\nInsertion does not properly position values" +
                        "\nYour AVL tree should look like: " + expected + "\nBut it actually looks like: " + makeString(root),
                1, (int) Integer.valueOf(root.left.getTopicList().get(0).getTopic()));
    }

    @Test(timeout = 1000)
    public void insertDuplicateTest() {
        ListAVLTree.node root = null;
        root = ListAVLTree.insert(root, new News("5", "5", String.valueOf(5), "User1"));
        root = ListAVLTree.insert(root, new News("5", "5", String.valueOf(5), "User1"));
        String expected = "{value=5,5, leftNode={}, rightNode={}}";
        assertEquals(
                "\nInsertion does not properly position values" +
                        "\nYour AVL tree should look like: " + expected + "\nBut it actually looks like: " + makeString(root),
                1, root.height);

        // Double checking encase anyone changes height output.
        assertNull("\nInsertion does not properly handle duplicates" +
                "\nYour AVL tree should look like: " + expected + "\nBut it actually looks like: " + makeString(root), root.right);

        assertNull("\nInsertion does not properly handle duplicates" +
                "\nYour AVL tree should look like: " + expected + "\nBut it actually looks like: " + makeString(root), root.left);
    }

    @Test(timeout = 1000)
    public void advancedRotationsTest() {
        // Cause a situation with a RR, RL, LL or LR rotation is required.
        ListAVLTree.node root = null;
        root = ListAVLTree.insert(root, new News("14", "14", String.valueOf(14), "User1"));
        root = ListAVLTree.insert(root, new News("17", "17", String.valueOf(17), "User1"));
        root = ListAVLTree.insert(root, new News("11", "11", String.valueOf(11), "User1"));
        root = ListAVLTree.insert(root, new News("7", "7", String.valueOf(7), "User1"));
        root = ListAVLTree.insert(root, new News("53", "53", String.valueOf(53), "User1"));
        root = ListAVLTree.insert(root, new News("4", "4", String.valueOf(4), "User1"));
        root = ListAVLTree.insert(root, new News("13", "13", String.valueOf(13), "User1"));
        root = ListAVLTree.insert(root, new News("12", "12", String.valueOf(12), "User1"));
        root = ListAVLTree.insert(root, new News("8", "8", String.valueOf(8), "User1"));
        root = ListAVLTree.insert(root, new News("60", "60", String.valueOf(60), "User1"));
        root = ListAVLTree.insert(root, new News("19", "19", String.valueOf(19), "User1"));
        root = ListAVLTree.insert(root, new News("16", "16", String.valueOf(16), "User1"));
        root = ListAVLTree.insert(root, new News("20", "20", String.valueOf(20), "User1"));

        String expected = "{value=14, leftNode={value=11, leftNode={value=7, leftNode={value=4, leftNode={}, rightNode={}}, rightNode={value=8, leftNode={}, rightNode={}}}, rightNode={value=12, leftNode={}, rightNode={value=13, leftNode={}, rightNode={}}}}, rightNode={value=19, leftNode={value=17, leftNode={value=16, leftNode={}, rightNode={}}, rightNode={}}, rightNode={value=53, leftNode={value=20, leftNode={}, rightNode={}}, rightNode={value=60, leftNode={}, rightNode={}}}}}";
        assertNotNull(
                "\nInsertion cannot handle either right right, right left, left left or left right double rotations." +
                        "\nYour AVL tree should look like: " + expected + "\nBut it actually looks like: " + makeString(root),
                root.getTopicList().get(0));
        assertNotNull(
                "\nInsertion cannot handle either right right, right left, left left or left right double rotations." +
                        "\nYour AVL tree should look like: " + expected + "\nBut it actually looks like: " + makeString(root),
                root.left.getTopicList().get(0));
        assertNotNull(
                "\nInsertion cannot handle either right right, right left, left left or left right double rotations." +
                        "\nYour AVL tree should look like: " + expected + "\nBut it actually looks like: " + makeString(root),
                root.right.getTopicList().get(0));
        assertEquals(
                "\nInsertion cannot handle either right right, right left, left left or left right double rotations." +
                        "\nYour AVL tree should look like: " + expected + "\nBut it actually looks like: " + makeString(root),
                0, getBalance(root)
        );
        assertEquals(
                "\nInsertion cannot handle either right right, right left, left left or left right double rotations." +
                        "\nYour AVL tree should look like: " + expected + "\nBut it actually looks like: " + makeString(root),
                14, (int) Integer.valueOf(root.getTopicList().get(0).getTopic())
        );
        assertEquals(
                "\nInsertion cannot handle either right right, right left, left left or left right double rotations." +
                        "\nYour AVL tree should look like: " + expected + "\nBut it actually looks like: " + makeString(root),
                11, (int) Integer.valueOf(root.left.getTopicList().get(0).getTopic())
        );
        assertEquals(
                "\nInsertion cannot handle either right right, right left, left left or left right double rotations." +
                        "\nYour AVL tree should look like: " + expected + "\nBut it actually looks like: " + makeString(root),
                19, (int) Integer.valueOf(root.right.getTopicList().get(0).getTopic())
        );

        // Another double rotation requiring test.

        root = null;
        root = ListAVLTree.insert(root, new News("40", "40", String.valueOf(40), "User1"));
        root = ListAVLTree.insert(root, new News("20", "20", String.valueOf(20), "User1"));
        root = ListAVLTree.insert(root, new News("10", "10", String.valueOf(10), "User1"));
        root = ListAVLTree.insert(root, new News("25", "25", String.valueOf(25), "User1"));
        root = ListAVLTree.insert(root, new News("30", "30", String.valueOf(30), "User1"));
        root = ListAVLTree.insert(root, new News("22", "22", String.valueOf(22), "User1"));
        root = ListAVLTree.insert(root, new News("50", "50", String.valueOf(50), "User1"));

        expected = "{value=25, leftNode={value=20, leftNode={value=10, leftNode={}, rightNode={}}, rightNode={value=22, leftNode={}, rightNode={}}}, rightNode={value=40, leftNode={value=30, leftNode={}, rightNode={}}, rightNode={value=50, leftNode={}, rightNode={}}}}";
        assertNotNull(
                "\nInsertion cannot handle either right right, right left, left left or left right double rotations." +
                        "\nYour AVL tree should look like: " + expected + "\nBut it actually looks like: " + makeString(root),
                root.getTopicList().get(0));
        assertNotNull(
                "\nInsertion cannot handle either right right, right left, left left or left right double rotations." +
                        "\nYour AVL tree should look like: " + expected + "\nBut it actually looks like: " + makeString(root),
                root.left.getTopicList().get(0));
        assertNotNull(
                "\nInsertion cannot handle either right right, right left, left left or left right double rotations." +
                        "\nYour AVL tree should look like: " + expected + "\nBut it actually looks like: " + makeString(root),
                root.right.getTopicList().get(0));
        assertEquals(
                "\nInsertion cannot handle either right right, right left, left left or left right double rotations." +
                        "\nYour AVL tree should look like: " + expected + "\nBut it actually looks like: " + makeString(root),
                0, getBalance(root)
        );
        assertEquals(
                "\nInsertion cannot handle either right right, right left, left left or left right double rotations." +
                        "\nYour AVL tree should look like: " + expected + "\nBut it actually looks like: " + makeString(root),
                25, (int) Integer.valueOf(root.getTopicList().get(0).getTopic())
        );
        assertEquals(
                "\nInsertion cannot handle either right right, right left, left left or left right double rotations." +
                        "\nYour AVL tree should look like: " + expected + "\nBut it actually looks like: " + makeString(root),
                20, (int) Integer.valueOf(root.left.getTopicList().get(0).getTopic())
        );
        assertEquals(
                "\nInsertion cannot handle either right right, right left, left left or left right double rotations." +
                        "\nYour AVL tree should look like: " + expected + "\nBut it actually looks like: " + makeString(root),
                40, (int) Integer.valueOf(root.right.getTopicList().get(0).getTopic())
        );
    }

    private static final String Tokens_In_Order = "(DAY)(TIME)(TAG)albert><01000Wed,;:";

    @Test(timeout = 1000)
    public void testTokens() {
        SearchTokeniser tokenizer= new SearchTokeniser(Tokens_In_Order);

        // check the type of the first token
        assertEquals("wrong token type", SearchToken.Type.DayField, tokenizer.current().getType());

        // check the actual token value"
        assertEquals("wrong token value", "(DAY)", tokenizer.current().getToken());

        tokenizer.next();
        assertEquals("wrong token type", SearchToken.Type.TimeField, tokenizer.current().getType());
        assertEquals("wrong token value", "(TIME)", tokenizer.current().getToken());

        tokenizer.next();
        assertEquals("wrong token type", SearchToken.Type.TagField, tokenizer.current().getType());
        assertEquals("wrong token value", "(TAG)", tokenizer.current().getToken());

        tokenizer.next();
        assertEquals("wrong token type", SearchToken.Type.TAG, tokenizer.current().getType());
        assertEquals("wrong token value", "albert", tokenizer.current().getToken());

        tokenizer.next();
        assertEquals("wrong token type", SearchToken.Type.GREATERTHAN, tokenizer.current().getType());
        assertEquals("wrong token value", ">", tokenizer.current().getToken());

        tokenizer.next();
        assertEquals("wrong token type", SearchToken.Type.LESSTHAN, tokenizer.current().getType());
        assertEquals("wrong token value", "<", tokenizer.current().getToken());

        tokenizer.next();
        assertEquals("wrong token type", SearchToken.Type.TIME, tokenizer.current().getType());
        assertEquals("wrong token value", "01000", tokenizer.current().getToken());

        tokenizer.next();
        assertEquals("wrong token type", SearchToken.Type.DAY, tokenizer.current().getType());
        assertEquals("wrong token value", "Wed", tokenizer.current().getToken());

        tokenizer.next();
        assertEquals("wrong token type", SearchToken.Type.LISTMARK, tokenizer.current().getType());
        assertEquals("wrong token value", ",", tokenizer.current().getToken());

        tokenizer.next();
        assertEquals("wrong token type", SearchToken.Type.NEXTMARK, tokenizer.current().getType());
        assertEquals("wrong token value", ";", tokenizer.current().getToken());

        tokenizer.next();
        assertEquals("wrong token type", SearchToken.Type.CONTAINS, tokenizer.current().getType());
        assertEquals("wrong token value", ":", tokenizer.current().getToken());
    }


    @Test(timeout=1000)
    public void testUnknownDotCharToken() {
        assertThrows(SearchToken.IllegalTokenException.class, () -> {
            SearchTokeniser tokenizer = new SearchTokeniser("(DAY):Wed;(TIME):12.5,2000;(DAY):Tue");
            int i = -1;
            while (i++ < 13) {
                tokenizer.next();
            }
        });
    }
    @Test(timeout=1000)
    public void testMangledToken() {

        assertThrows(SearchToken.IllegalTokenException.class, () -> {
            SearchTokeniser tokenizer= new SearchTokeniser("(TIM340");
            int i = -1;
            while (i++ < 13) {
                tokenizer.next();
            }
        });
    }
    @Test(timeout=1000)
    public void testUnknownDashCharToken() {
        assertThrows(SearchToken.IllegalTokenException.class, () -> {
            SearchTokeniser tokenizer = new SearchTokeniser("(TIME):12-");
            int i = -1;
            while (i++ < 4) {
                System.out.println(i);
                tokenizer.next();
            }
        });
    }
    @Test(timeout=1000)
    public void testUnknownHashCharToken() {
        assertThrows(SearchToken.IllegalTokenException.class, () -> {
            SearchTokeniser tokenizer = new SearchTokeniser("(DAY):#Wed");
            int i = -1;
            while (i++ < 4) {
                System.out.println(i);
                tokenizer.next();
            }
        });
    }
    @Test(timeout=1000)
    public void testTooLargeTimeToken() {
        assertThrows(SearchToken.IllegalTokenException.class, () -> {
            SearchTokeniser tokenizer = new SearchTokeniser("(TIME):99999");
            int i = -1;
            while (i++ < 4) {
                System.out.println(i);
                tokenizer.next();
            }
        });
    }
    @Test(timeout=1000)
    public void testTooLargeTimeToken2() {
        assertThrows(SearchToken.IllegalTokenException.class, () -> {
            SearchTokeniser tokenizer = new SearchTokeniser("(TIME):00089999");
            int i = -1;
            while (i++ < 4) {
                System.out.println(i);
                tokenizer.next();
            }
        });
    }
    @Test(timeout=1000)
    public void testTooLargeMinToken() {
        assertThrows(SearchToken.IllegalTokenException.class, () -> {
            SearchTokeniser tokenizer = new SearchTokeniser("(TIME):80");
            int i = -1;
            while (i++ < 4) {
                System.out.println(i);
                tokenizer.next();
            }
        });
    }
    @Test(timeout=1000)
    public void testTooLargeHourToken() {
        assertThrows(SearchToken.IllegalTokenException.class, () -> {
            SearchTokeniser tokenizer = new SearchTokeniser("(TIME):2520");
            int i = -1;
            while (i++ < 4) {
                System.out.println(i);
                tokenizer.next();
            }
        });
    }
    private static SearchTokeniser tokenizer;
    private static final String SIMPLE_DAY_CASE = "(DAY):Wed";
    private static final String SORT_CASE = "(ALTSORT)";
    private static final String SIMPLE_TIME_CONTAINS_CASE = "(TIME):01000";
    private static final String MID_TIME_DAY_CASE = "(DAY):Wed; (TIME):01000";
    private static final String COMPLEX_CASE1 = "(DAY):Wed,Fri; (TIME):01000,02000; (TIME)>01000;(DAY):Tue";
    private static final String COMPLEX_TIME_CASE2 = "(TIME)>01200;(TIME)<01500;(TIME)>01230;(TIME)<01600";


    @Test(timeout=1000)
    public void testDayCase(){
        Condition.getInstance().clearCondition();
        tokenizer = new SearchTokeniser(SIMPLE_DAY_CASE);
        LinkedHashSet<SearchToken> wedList = new LinkedHashSet<>();
        SearchToken wed = new SearchToken("Wed", SearchToken.Type.DAY);
        wedList.add(wed);
        try{
            new SearchParser(tokenizer).parseSearch();
            assertEquals("incorrect Condition", "\nDays: DAY(Wed), \n", Condition.getInstance().show());
        }catch (Exception e){
            fail(e.getMessage());
        }
    }
    @Test(timeout=1000)
    public void testSortCase(){
        Condition.getInstance().clearCondition();
        tokenizer = new SearchTokeniser(SORT_CASE);
        try{
            new SearchParser(tokenizer).parseSearch();
            assertEquals("incorrect Condition", "\naltsort set to true \n", Condition.getInstance().show());
        }catch (Exception e){
            fail(e.getMessage());
        }
    }
    @Test(timeout=1000)
    public void testTimeContainsCase(){
        Condition.getInstance().clearCondition();
        tokenizer = new SearchTokeniser(SIMPLE_TIME_CONTAINS_CASE);
        LinkedHashSet<Integer> timeList = new LinkedHashSet<>();
        timeList.add((Integer) 01000);
        try{
            new SearchParser(tokenizer).parseSearch();
            assertEquals("incorrect Condition", "\nTimes: TIME(1000), \n", Condition.getInstance().show());
        }catch (Exception e){
            fail(e.getMessage());
        }
    }
    @Test(timeout=1000)
    public void testDayTimeCase(){
        tokenizer = new SearchTokeniser(MID_TIME_DAY_CASE);
        LinkedHashSet<SearchToken> wedList = new LinkedHashSet<>();
        SearchToken wed = new SearchToken("Wed", SearchToken.Type.DAY);
        wedList.add(wed);
        LinkedHashSet<Integer> timeList = new LinkedHashSet<>();
        timeList.add((Integer) 01000);
        try{
            new SearchParser(tokenizer).parseSearch();
            assertEquals("incorrect Condition", "\nDays: DAY(Wed), \nTimes: TIME(1000), \n", Condition.getInstance().show());
        }catch (Exception e){
            fail(e.getMessage());
        }
    }
    @Test(timeout=1000)
    public void testComplexCase(){
        Condition.getInstance().clearCondition();
        tokenizer = new SearchTokeniser(COMPLEX_CASE1);
        try{
            new SearchParser(tokenizer).parseSearch();
            assertEquals("incorrect Condition", "\nDays: DAY(Wed), DAY(Fri), DAY(Tue), \nTimes: TIME(1000), TIME(2000), \n", Condition.getInstance().show());
        }catch (Exception e){
            fail(e.getMessage());
        }
    }
    @Test(timeout=1000)
    public void testComplexTimeCase(){
        Condition.getInstance().clearCondition();
        tokenizer = new SearchTokeniser(COMPLEX_TIME_CASE2);
        try{
            new SearchParser(tokenizer).parseSearch();
            assertEquals("incorrect Condition", "\nLess Than time: TIME(1500)\nGreater Than time: TIME(1230)\n", Condition.getInstance().show());
        }catch (Exception e){
            fail(e.getMessage());
        }
    }


    @Test(timeout=1000)
    public void testIllegalProductionException() {
        // Provide a series of tokens that should invoke this exception
        assertThrows(SearchParser.IllegalProductionException.class, () -> {
            Condition.getInstance().clearCondition();
            tokenizer = new SearchTokeniser("uwu");
            new SearchParser(tokenizer).parseSearch();
        });

        assertThrows(SearchParser.IllegalProductionException.class, () -> {
            Condition.getInstance().clearCondition();
            tokenizer = new SearchTokeniser("(DAY)>Wed");
            new SearchParser(tokenizer).parseSearch();
        });

        assertThrows(SearchParser.IllegalProductionException.class, () -> {
            Condition.getInstance().clearCondition();
            tokenizer = new SearchTokeniser("(DAY):200");
            new SearchParser(tokenizer).parseSearch();
        });

        assertThrows(SearchParser.IllegalProductionException.class, () -> {
            Condition.getInstance().clearCondition();
            tokenizer = new SearchTokeniser("(DAY)(TIME)");
            new SearchParser(tokenizer).parseSearch();
        });

        assertThrows(SearchParser.IllegalProductionException.class, () -> {
            Condition.getInstance().clearCondition();
            tokenizer = new SearchTokeniser("(DAY),");
            new SearchParser(tokenizer).parseSearch();
        });
        assertThrows(SearchParser.IllegalProductionException.class, () -> {
            Condition.getInstance().clearCondition();
            tokenizer = new SearchTokeniser("(DAY);");
            new SearchParser(tokenizer).parseSearch();
        });
        assertThrows(SearchParser.IllegalProductionException.class, () -> {
            Condition.getInstance().clearCondition();
            tokenizer = new SearchTokeniser("(DAY):");
            new SearchParser(tokenizer).parseSearch();
        });
        assertThrows(SearchParser.IllegalProductionException.class, () -> {
            Condition.getInstance().clearCondition();
            tokenizer = new SearchTokeniser("(DAY):albert");
            new SearchParser(tokenizer).parseSearch();
        });
        assertThrows(SearchParser.IllegalProductionException.class, () -> {
            Condition.getInstance().clearCondition();
            tokenizer = new SearchTokeniser("(DAY):(TIME)");
            new SearchParser(tokenizer).parseSearch();
        });
        assertThrows(SearchParser.IllegalProductionException.class, () -> {
            Condition.getInstance().clearCondition();
            tokenizer = new SearchTokeniser("(DAY);(TIME)");
            new SearchParser(tokenizer).parseSearch();
        });
        assertThrows(SearchParser.IllegalProductionException.class, () -> {
            Condition.getInstance().clearCondition();
            tokenizer = new SearchTokeniser("(DAY):Wed(TIME)");
            new SearchParser(tokenizer).parseSearch();
        });
        assertThrows(SearchParser.IllegalProductionException.class, () -> {
            Condition.getInstance().clearCondition();
            tokenizer = new SearchTokeniser("(DAY):Wed;(TIME)>Wed");
            new SearchParser(tokenizer).parseSearch();
        });
        assertThrows(SearchParser.IllegalProductionException.class, () -> {
            Condition.getInstance().clearCondition();
            tokenizer = new SearchTokeniser("(DAY):Wed;(TIME)>(TIME)");
            new SearchParser(tokenizer).parseSearch();
        });
        assertThrows(SearchParser.IllegalProductionException.class, () -> {
            Condition.getInstance().clearCondition();
            tokenizer  = new SearchTokeniser("(DAY):Wed;(TIME)>albert");
            new SearchParser(tokenizer).parseSearch();
        });
        assertThrows(SearchParser.IllegalProductionException.class, () -> {
            Condition.getInstance().clearCondition();
            tokenizer = new SearchTokeniser("(DAY):Wed;(TIME):Sarah");
             new SearchParser(tokenizer).parseSearch();
        });
        assertThrows(SearchParser.IllegalProductionException.class, () -> {
            Condition.getInstance().clearCondition();
            tokenizer = new SearchTokeniser("(DAY):Wed;(TIME):Tue");
             new SearchParser(tokenizer).parseSearch();
        });

    }
    @Test(timeout=1000)
    public void testNoResults(){
        Condition.getInstance().clearCondition();
        tokenizer  = new SearchTokeniser("(TIME):100");
        ListAVLTree.node root = null;
        root = ListAVLTree.insert(root, new News("40", "40", String.valueOf(40), "User1"));
        root = ListAVLTree.insert(root, new News("20", "20", String.valueOf(20), "User1"));
        root = ListAVLTree.insert(root, new News("10", "10", String.valueOf(10), "User1"));
        root = ListAVLTree.insert(root, new News("25", "25", String.valueOf(25), "User1"));
        root = ListAVLTree.insert(root, new News("30", "30", String.valueOf(30), "User1"));
        root = ListAVLTree.insert(root, new News("22", "22", String.valueOf(22), "User1"));
        ArrayList<News> retList = new ArrayList<>();
        retList.add(new News("10", "10", String.valueOf(10), "User1"));
        try{
             new SearchParser(tokenizer).parseSearch();
            ArrayList<News> tryList = Condition.getInstance().Filter(root);
            assertTrue("The list is not empty " + tryList,tryList.isEmpty());
        }catch (Exception e){
            System.out.println("");
            fail(e.getMessage());
        }
    }
    @Test(timeout=1000)
    public void testOneResult(){
        Condition.getInstance().clearCondition();
        tokenizer = new SearchTokeniser("(TIME):10");
        ListAVLTree.node root = null;
        root = ListAVLTree.insert(root, new News("40", "40", String.valueOf(40), "User1"));
        root = ListAVLTree.insert(root, new News("20", "20", String.valueOf(20), "User1"));
        root = ListAVLTree.insert(root, new News("10", "10", String.valueOf(10), "User1"));
        root = ListAVLTree.insert(root, new News("25", "25", String.valueOf(25), "User1"));
        root = ListAVLTree.insert(root, new News("30", "30", String.valueOf(30), "User1"));
        root = ListAVLTree.insert(root, new News("22", "22", String.valueOf(22), "User1"));
        ArrayList<News> retList = new ArrayList<>();
        retList.add(new News("10", "10", String.valueOf(10), "User1"));
        try{
             new SearchParser(tokenizer).parseSearch();
            ArrayList<News> tryList = Condition.getInstance().Filter(root);
            assertEquals(retList.get(0).show(),tryList.get(0).show());
        }catch (Exception e){
            System.out.println("");
            fail(e.getMessage());
        }
    }
    @Test(timeout=1000)
    public void testSortPromResult(){
        Condition.getInstance().clearCondition();
        tokenizer = new SearchTokeniser("(ALTSORT);(TIME)>20");
        ListAVLTree.node root = null;
        News prom = new News("40", "40", String.valueOf(40), "User1");
        prom.setPromoted(true);
        root = ListAVLTree.insert(root, prom);
        root = ListAVLTree.insert(root, new News("20", "20", String.valueOf(20), "User1"));
        root = ListAVLTree.insert(root, new News("10", "10", String.valueOf(10), "User1"));
        root = ListAVLTree.insert(root, new News("25", "25", String.valueOf(25), "User1"));
        root = ListAVLTree.insert(root, new News("30", "30", String.valueOf(30), "User1"));
        root = ListAVLTree.insert(root, new News("22", "22", String.valueOf(22), "User1"));
        ArrayList<News> retList = new ArrayList<>();
        retList.add(prom);
        try{
            new SearchParser(tokenizer).parseSearch();
            ArrayList<News> tryList = Condition.getInstance().Filter(root);
            assertEquals(retList.get(0).show(),tryList.get(0).show());
        }catch (Exception e){
            System.out.println("");
            fail(e.getMessage());
        }
    }
    @Test(timeout=1000)
    public void testSortLikesResult(){
        Condition.getInstance().clearCondition();
        tokenizer = new SearchTokeniser("(ALTSORT);(TIME)>20");
        ListAVLTree.node root = null;
        News prom = new News("40", "40", String.valueOf(40), "User1");
        prom.setLikes("20");
        root = ListAVLTree.insert(root, prom);
        root = ListAVLTree.insert(root, new News("20", "20", String.valueOf(20), "User1"));
        root = ListAVLTree.insert(root, new News("10", "10", String.valueOf(10), "User1"));
        root = ListAVLTree.insert(root, new News("25", "25", String.valueOf(25), "User1"));
        root = ListAVLTree.insert(root, new News("30", "30", String.valueOf(30), "User1"));
        root = ListAVLTree.insert(root, new News("22", "22", String.valueOf(22), "User1"));
        ArrayList<News> retList = new ArrayList<>();
        retList.add(prom);
        try{
            new SearchParser(tokenizer).parseSearch();
            ArrayList<News> tryList = Condition.getInstance().Filter(root);
            assertEquals(retList.get(0).show(),tryList.get(0).show());
        }catch (Exception e){
            System.out.println("");
            fail(e.getMessage());
        }
    }

}