package com.example.groupprojectapplication;

import static com.example.groupprojectapplication.ListAVLTree.toInOrderList;

import com.example.groupprojectapplication.model.News;

import java.util.ArrayList;
import java.util.LinkedHashSet;
// @author Jack
public class Condition {
    LinkedHashSet<SearchToken> days;
    Integer lessThanTime;
    Integer greaterThanTime;
    LinkedHashSet<Integer> equalsToTime;
    LinkedHashSet<SearchToken> tags;
    Boolean altsort = false;
    private static Condition instance;

    private Condition(LinkedHashSet<SearchToken> days,Integer lessThanTime, Integer greaterThanTime,
                     LinkedHashSet<Integer> equalsToTime,LinkedHashSet<SearchToken> tags){
        this.days = days;
        this.lessThanTime = lessThanTime;
        this.greaterThanTime = greaterThanTime;
        this.equalsToTime = equalsToTime;
        this.tags = tags;
    }

    public static Condition getInstance(){
        if (instance==null)
                instance = new Condition(null,null,null,null,null);
        return instance;
    }
    public void clearCondition(){
        days = null;
        lessThanTime = null;
        greaterThanTime = null;
        equalsToTime = null;
        tags = null;
        altsort = false;
    }
    public boolean isNull(){
        if(!(days==null))
            return false;
        else if(!(lessThanTime==null))
            return false;
        else if(!(greaterThanTime==null))
            return false;
        else if(!(equalsToTime==null))
            return false;
        else if(!(tags==null))
            return false;
        else
            return true;
    }

    public void addDay(SearchToken day){
        if (!(day==null)) {
            if (days == null)
                days = new LinkedHashSet<SearchToken>();
            days.add(day);
        }
    }
    public void setAltsortTrue(){
        altsort=true;
    }

    public Boolean getAltsort() {
        return altsort;
    }

    public void setAltsortFalse(){
        altsort=false;
    }
    public void addTag(SearchToken tag){
        if (!(tag==null)) {
            if (tags == null)
                tags = new LinkedHashSet<SearchToken>();
            tags.add(tag);
        }
    }
    public void updateGreaterThan(Integer minTime){
        if (!(minTime==null)) {
            if (equalsToTime == null) {
                if (greaterThanTime == null)
                    greaterThanTime = minTime;
                else if (greaterThanTime < minTime) {
                    greaterThanTime = minTime;
                }
            }
        }
    }
    public void updateLessThan(Integer maxTime){
        if (!(maxTime==null)) {
            if (equalsToTime == null) {
                if (lessThanTime == null)
                    lessThanTime = maxTime;
                else if (lessThanTime > maxTime)
                    lessThanTime = maxTime;
            }
        }
    }
    public void updateEqualToTime(Integer time){
        if (!(time==null)) {
            if (equalsToTime == null) {
                lessThanTime=null;
                greaterThanTime = null;
                equalsToTime = new LinkedHashSet<>();
            }
            equalsToTime.add(time);
        }
    }

    public ArrayList<News> Filter(ListAVLTree.node root){
        LinkedHashSet<SearchToken> days = new LinkedHashSet<>();
        if (this.days == null) {
            days.add(new SearchToken("Mon", SearchToken.Type.DAY));
            days.add(new SearchToken("Tue", SearchToken.Type.DAY));
            days.add(new SearchToken("Wed", SearchToken.Type.DAY));
            days.add(new SearchToken("Thurs", SearchToken.Type.DAY));
            days.add(new SearchToken("Fri", SearchToken.Type.DAY));
            days.add(new SearchToken("Sat", SearchToken.Type.DAY));
            days.add(new SearchToken("Sun", SearchToken.Type.DAY));
        } else
            days.addAll(this.days);

        Integer minTime;
        if(this.greaterThanTime == null)
            minTime= 0000;
        else
            minTime = this.greaterThanTime;

        Integer maxTime;
        if(this.lessThanTime == null)
            maxTime= 2360;
        else
            maxTime = this.lessThanTime;

        ArrayList<News> retList = new ArrayList<>();

        for (SearchToken d : days){
            if (this.equalsToTime == null){

                ArrayList<Integer> bounds = new ArrayList<>();
                bounds.add(Integer.parseInt(dayToNumbers(d)+ String.format("%4s", minTime.toString()).replace(' ', '0')));
                bounds.add(Integer.parseInt(dayToNumbers(d)+String.format("%4s", maxTime.toString()).replace(' ', '0')));

                ListAVLTree.node node = KeyGreater(root, bounds.get(0));

                node = KeyLess(node, bounds.get(1));
                toInOrderList(node, retList);
            } else{
                ArrayList<Integer> timeList = new ArrayList<>();
                timeList.addAll(this.equalsToTime);
                for (Integer k : timeList){
                    KeyEquals(retList, root, Integer.parseInt( dayToNumbers(d)+String.format("%4s", k.toString()).replace(' ', '0')));
                }
            }

        }
        if (altsort){
            int count = 0;
            ArrayList<News> tempList = new ArrayList<>();
            for (News n : retList){
                if (count>100)
                    break;
                tempList.add(n);
                count++;
            }
            retList = tempList;
            sort(retList);
            count = 0;
            for (News n : retList){
                if(count >2)
                    break;
                if(n.isPromoted()==true){
                    retList.remove(n);
                    retList.add(0,n);
                    count++;
                }
            }
        }
        return retList;
    }

    public static void sort(ArrayList<News> list) {

        list.sort((o1, o2)
                -> Integer.valueOf("-"+o1.getLikes()).compareTo(
                Integer.valueOf("-"+o2.getLikes())));
    }
    public void KeyEquals(ArrayList<News> retList,ListAVLTree.node root, Integer key){
        if (!(root == null)){
            if (key.equals(root.getKey()))
                retList.addAll(root.getTopicList());
            else if (key > root.getKey()) {
                KeyEquals(retList,root.right, key);
            }
            else
                KeyEquals(retList,root.left, key);
        }
    }

    public ListAVLTree.node KeyLess(ListAVLTree.node root, Integer maxKey){
        if (root==null)
            return null;

        int key = root.key;
        if (key>maxKey)
            return KeyLess(root.left, maxKey);
        else if (maxKey.equals(key))
            return root.FullCopy(root.left, null);
        else{
            return root.FullCopy(root.left, KeyLess(root.right, maxKey));
        }
    }
    public ListAVLTree.node KeyGreater(ListAVLTree.node root, Integer minKey){
        if (root==null)
            return null;

        int key = root.key;
        if (key<minKey)
            return KeyGreater(root.right, minKey);
        else if (minKey.equals(key))
            return root.FullCopy(null, root.right);
        else{
            return root.FullCopy(KeyGreater(root.left, minKey), root.right);
        }
    }

    public String dayToNumbers(SearchToken day){
        if (day.getToken().equals("Mon")){
            return "0";
        } else if (day.getToken().equals("Tue")) {
            return "1";
        }else if (day.getToken().equals("Wed")) {
            return "2";
        }else if (day.getToken().equals("Thurs")) {
            return "3";
        }else if (day.getToken().equals("Fri")) {
            return "4";
        }else if (day.getToken().equals("Sat")) {
            return "5";
        }else if (day.getToken().equals("Sun")) {
            return "6";
        }
        else
            throw new IllegalArgumentException("Number which I dont recognise as a day");
    }
    public String show(){
        String val = "\n";
        if (!(this.days == null)) {
            val = val + "Days: ";
            for (SearchToken d : this.days)
                val = val + d.toString() + ", ";
            val = val + "\n";
        }
        if (!(this.lessThanTime==null)) {
            val = val + "Less Than time: ";
            val = val + "TIME(" +lessThanTime.toString() + ")";
            val = val + "\n";
        }

        if (!(this.greaterThanTime==null)) {
            val = val + "Greater Than time: ";
            val = val + "TIME(" +greaterThanTime.toString() + ")";
            val = val + "\n";
        }

        if (!(this.equalsToTime==null)) {
            val = val + "Times: ";
            for (Integer t : this.equalsToTime)
                val = val + "TIME(" + t.toString() + "), ";
            val = val + "\n";
        }

        if (!(this.tags==null)) {
            val = val + "tags: ";
            for (SearchToken t : this.tags)
                val = val + t.toString() + ", ";
            val = val + "\n";
        }
        if(altsort==true){
            val = val +"altsort set to true \n";
        }
        if (val.equals("\n"))
            val = "\nThere were no restrictions given";
        return val;
    }

}
