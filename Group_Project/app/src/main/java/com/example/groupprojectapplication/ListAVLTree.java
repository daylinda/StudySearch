package com.example.groupprojectapplication;
import com.example.groupprojectapplication.model.News;

import java.util.*;
// @author Jack
public class ListAVLTree {
    // Java program of AVL tree that handles duplicates
    // Skeleton of the program by Arnab Kundu
    //https://www.geeksforgeeks.org/avl-with-duplicate-keys/
    //Was substantially edited by me (Jack)
    public static class IllegalDeletionException extends IllegalArgumentException {
        public IllegalDeletionException(String errorMessage) {
            super(errorMessage);
        }
    }

    public static class IllegalOperationException extends IllegalArgumentException {
        public IllegalOperationException(String errorMessage) {
            super(errorMessage);
        }
    }

    // An AVL tree node
    static public class node {
        int key;
        node left;
        node right;
        int height;
        int count;
        ArrayList<News> topicList;

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public node getLeft() {
            return left;
        }

        public void setLeft(node left) {
            this.left = left;
        }

        public node getRight() {
            return right;
        }

        public void setRight(node right) {
            this.right = right;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public ArrayList<News> getTopicList() {
            return topicList;
        }

        public void setTopicList(ArrayList<News> topicList) {
            this.topicList = topicList;
        }

        //Copies a tree to ensure immutability
        public node FullCopy(node left, node right){
            if (this == null){
                return null;
            }
            node copy = new node();
            copy.key = this.getKey();
            copy.topicList = this.getTopicList();
            copy.count = this.getCount();
            copy.height = this.getHeight();
            if (left == null){
                copy.left = null;
            } else
                copy.left = left.FullCopy(left.left, left.right);
            if (right == null){
                copy.right = null;
            } else
                copy.right = right.FullCopy(right.left, right.right);
            return copy;
        }
        public String show(){
            String time = String.valueOf(this.getKey());
            String ret = "\n";
            ret += numberToTime(time);
            ArrayList<String> strings = new ArrayList<>();
            for (int i=0; i<this.getTopicList().size(); i++){
                strings.add(this.getTopicList().get(i).getTopic());
            }
            ret+= " Topics: " + String.join(", ", strings);
            return ret;
        }

    }

    public static String numberToTime(String num){
        num = String.valueOf(Integer.valueOf(num));
        if(num.length() <5 )
            return "Mon " +num;
        else if (num.charAt(0) == '1')
            return "Tue " + num.substring(1);
        else if (num.charAt(0) == '2')
            return "Wed " + num.substring(1);
        else if (num.charAt(0) == '3')
            return "Thurs " + num.substring(1);
        else if (num.charAt(0) == '4')
            return "Fri " + num.substring(1);
        else if (num.charAt(0) == '5')
            return "Sat " + num.substring(1);
        else if (num.charAt(0) == '6')
            return "Sun " + num.substring(1);
        else
            throw new IllegalArgumentException("Saw A time I didn't know how to deal with");
    }

        // A utility function to get height of the tree
        static int height(node N)
        {
            if (N == null)
                return 0;
            return N.height;
        }

        // A utility function to get maximum of two integers
        static int max(int a, int b)
        {
            return (a > b) ? a : b;
        }

    // A utility function to right rotate subtree rooted with y

        /* Helper function that allocates a new node with the given key and
        null left and right pointers. */
        static node newNode(News topic)
        {
            node node = new node();
            node.key = Integer.parseInt(topic.getTime());
            node.left = null;
            node.right = null;
            node.height = 1; // new node is initially added at leaf
            node.count = 1;
            node.topicList = new ArrayList<News>();
            node.topicList.add(topic);
            return (node);
        }

        static node rightRotate(node y)
        {
            node x = y.left;
            node T2 = x.right;

            // Perform rotation
            x.right = y;
            y.left = T2;

            // Update heights
            y.height = max(height(y.left), height(y.right)) + 1;
            x.height = max(height(x.left), height(x.right)) + 1;

            // Return new root
            return x;
        }

        // A utility function to left rotate subtree rooted with x
        static node leftRotate(node x)
        {
            node y = x.right;
            node T2 = y.left;

            // Perform rotation
            y.left = x;
            x.right = T2;

            // Update heights
            x.height = max(height(x.left), height(x.right)) + 1;
            y.height = max(height(y.left), height(y.right)) + 1;

            // Return new root
            return y;
        }

        // Get Balance factor of node N
        static int getBalance(node N)
        {
            if (N == null)
                return 0;
            return height(N.left) - height(N.right);
        }

        static public node insert(node node, News topic)
        {
            int key = Integer.parseInt(topic.getTime());
            if (node == null)
                return (newNode(topic));

            // If key already exists increment count and add to TopicList
            if (key == node.key) {
                (node.count)++;
                node.topicList.add(topic);
                return node;
            }

            //Otherwise, recur down the tree
            if (key < node.key)
                node.left = insert(node.left, topic);
            else
                node.right = insert(node.right, topic);

           //Update height of this ancestor node
            node.height = max(height(node.left), height(node.right)) + 1;

		//Get the balance factor of this ancestor node to check whether
	    //this node became unbalanced
            int balance = getBalance(node);

            // Left Left Case
            if (balance > 1 && key < node.left.key)
                return rightRotate(node);

            // Right Right Case
            if (balance < -1 && key > node.right.key)
                return leftRotate(node);

            // Left Right Case
            if (balance > 1 && key > node.left.key) {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }

            // Right Left Case
            if (balance < -1 && key < node.right.key) {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }

            //return the (unchanged) node pointer
            return node;
        }

       //Helper to get min Value of a sub tree
        static node minValueNode(node node)
        {
            node current = node;
            while (current.left != null)
                current = current.left;

            return current;
        }
        public static node replaceNode(node root, News topic){
            int key = Integer.parseInt(topic.getTime());

            //If the key does not exits then we move on
            if (root == null)
                return root;
            // If the key to be replaced is smaller than the root's key,
            // then it lies in left subtree
            if (key < root.key)
                root.left = replaceNode(root.left, topic);
            // If the key to be replaced is greater than the root's key,
            // then it lies in right subtree
            else if (key > root.key)
                root.right = replaceNode(root.right, topic);

            // if key is same as root's key, then This is the node
            // which contains the topic to be replaced
            String id = topic.getId();
            ArrayList<News> repList = new ArrayList<>();
            for (News n : root.getTopicList()){
                if (n.getId().equals(id))
                    repList.add(topic);
                else
                    repList.add(n);
            }
            root.setTopicList(repList);
            return root;
        }

        public static node deleteNode(node root, News topic)
        {
            int key = Integer.parseInt(topic.getTime());

            if (root == null)
                return root;
            if (key < root.key)
                root.left = deleteNode(root.left, topic);

            else if (key > root.key)
                root.right = deleteNode(root.right, topic);

            else {
                if (root.count > 1) {
                    if (!root.topicList.contains(topic)){
                        throw new IllegalDeletionException("Topic is not within the node");
                    }
                    System.out.println("I am deleting a topic where count is greater than one");
                    ArrayList<String> strings = new ArrayList<>();
                    for (int i=0; i<root.topicList.size(); i++){
                        strings.add(root.topicList.get(i).getTopic());
                    }
                    System.out.println("Topics: " + String.join(", ", strings));
                    root.topicList.remove(topic);
                    strings.clear();
                    for (int i=0; i<root.topicList.size(); i++){
                        strings.add(root.topicList.get(i).getTopic());
                    }
                    System.out.println("Topics: " + String.join(", ", strings));
                    (root.count)--;
                    return root;
                }
                System.out.println("I am deleting a full node");


                // node with only one child or no child
                if ((root.left == null) || (root.right == null)) {
                    node temp = root.left != null ? root.left : root.right;

                    // No child case
                    if (temp == null) {
                        temp = root;
                        root = null;
                    }
                    else // One child case
                        root = temp; // Copy the contents of the non-empty child
                }
                else {
                    // node with two children: Get the inorder successor (smallest
                    // in the right subtree)
                    node temp = minValueNode(root.right);

                    // Copy the inorder successor's data to this node and update the count
                    root.key = temp.key;
                    root.count = temp.count;
                    root.topicList = temp.topicList;
                    temp.count = 1;

                    // Delete the inorder successor
                    //Should only need to delete the first topic because we have manually set the count to be 1
                    root.right = deleteNode(root.right, temp.topicList.get(0));


                }
            }

            // If the tree had only one node then return
            if (root == null)
                return root;

            root.height = max(height(root.left), height(root.right)) + 1;

           //Rebalance the tree
            int balance = getBalance(root);

            // Left Left Case
            if (balance > 1 && getBalance(root.left) >= 0)
                return rightRotate(root);

            // Left Right Case
            if (balance > 1 && getBalance(root.left) < 0) {
                root.left = leftRotate(root.left);
                return rightRotate(root);
            }

            // Right Right Case
            if (balance < -1 && getBalance(root.right) <= 0)
                return leftRotate(root);

            // Right Left Case
            if (balance < -1 && getBalance(root.right) > 0) {
                root.right = rightRotate(root.right);
                return leftRotate(root);
            }

            return root;
        }

        //print the nodes, preorder
        static void preOrder(node root)
        {
            if (root != null) {
                ArrayList<String> strings = new ArrayList<>();
                for (int i=0; i<root.topicList.size(); i++){
                    strings.add(root.topicList.get(i).getTopic());
                }
                System.out.println("Time: "+ root.key + " Topics: " + String.join(", ", strings));
                if (!(root.left ==null)){
                    System.out.println("Left: " + root.left.key);
                }
                else{
                    System.out.println("Left: null");
                }
                if (!(root.right ==null)){
                    System.out.println("Right: " + root.right.key + "\n");
                }
                else{
                    System.out.println("Right: null");
                }
                preOrder(root.left);
                preOrder(root.right);
            }
        }

        static public ArrayList<News> toInOrderList(node root, ArrayList<News> flattenedList)
        {
            if (root != null) {
                flattenedList = toInOrderList(root.left, flattenedList);
                for (int i=0; i<root.topicList.size(); i++){
                    flattenedList.add(root.topicList.get(i));
                }
                flattenedList = toInOrderList(root.right, flattenedList);
            }
            return flattenedList;
        }

        static void inOrder(node root)
        {
            if (root != null) {
                inOrder(root.left);
                ArrayList<String> strings = new ArrayList<>();
                for (int i=0; i<root.topicList.size(); i++){
                    strings.add(root.topicList.get(i).getTopic());
                }
                System.out.println(root.show());

                inOrder(root.right);
            }
        }




        // The main function for doing manual testing
        public static void main(String args[])
        {
            News firstTopic = new News("JHAGEFIHU", "Data Structures", String.valueOf(00) , "JHAGEFIHU");
            News secondTopic = new News("JHAGEFIHU", "Admin Meeting", String.valueOf(100) , "JHAGEFIHU");
            News sameAsSecondTopic = new News("JHAGEFIHU", "Style points", String.valueOf(100) , "JHAGEFIHU");
            News thirdTopic = new News("JHAGEFIHU", "We Hate Sergio", String.valueOf(200) , "JHAGEFIHU");
            News fourthTopic = new News("JHAGEFIHU", "Data Problems", String.valueOf(500) , "JHAGEFIHU");
            News fifthTopic = new News("JHAGEFIHU", "Test Case Analysis", String.valueOf(600) , "JHAGEFIHU");
            News sixthTopic = new News("JHAGEFIHU", "Disaster Management", String.valueOf(900) , "JHAGEFIHU");
            News seventhTopic = new News("JHAGEFIHU", "Left Blank", String.valueOf(700) , "JHAGEFIHU");
            News sameasseventhTopic = new News("JHAGEFIHU", "Albequerke", String.valueOf(700) , "JHAGEFIHU");
            News ninthTopic = new News("JHAGEFIHU", "Rabbits", String.valueOf(800) , "JHAGEFIHU");
            News tenthTopic = new News("JHAGEFIHU", "Marxist Leninism", String.valueOf(1000) , "JHAGEFIHU");
            node root = null;

            /* Constructing tree given in the above figure */
            root = insert(root, firstTopic);
            System.out.printf("\n After first insert\n"); //0
            preOrder(root);
            root = insert(root, secondTopic);
            System.out.printf("\n After second insert\n");//100
            preOrder(root);
            root = insert(root, thirdTopic);
            System.out.printf("\nAfter third insert\n");//200
            preOrder(root);
            root = insert(root, fourthTopic);
            System.out.printf("\nAfter fourth insert\n");//300
            preOrder(root);
            root = insert(root, fifthTopic);
            System.out.printf("\nAfter fifth insert\n");//500
            preOrder(root);
            root = insert(root, sixthTopic);
            System.out.printf("\nAfter sixth insert\n");//600
            preOrder(root);
            root = insert(root, seventhTopic);
            System.out.printf("\nAfter seventh insert\n");//700
            preOrder(root);
            root = insert(root, sameAsSecondTopic);
            System.out.printf("\nAfter eighth insert\n"); //100 Repeat
            preOrder(root);
            root = insert(root, sameasseventhTopic);
            System.out.printf("\nAfter ninth insert\n"); //700 Repeat
            preOrder(root);
            root = insert(root, ninthTopic);
            System.out.printf("\nAfter tenth insert\n"); //800
            preOrder(root);
            root = insert(root, tenthTopic);
            System.out.printf("\nAfter last insert\n");//1000
            preOrder(root);

            System.out.printf("In order traversal of the constructed AVL tree is \n");
            inOrder(root);

            deleteNode(root, fourthTopic);

            System.out.printf("\nIn order traversal after deletion of " + fourthTopic.getTopic()+ "\n");
            inOrder(root);
            System.out.println("\n Looking at the structure \n");
            preOrder(root);

            deleteNode(root, secondTopic);

            System.out.printf("\nIn order traversal after deletion of "+ secondTopic.getTopic()+ "\n");
            inOrder(root);
            System.out.println("\n Looking at the structure \n");
            preOrder(root);
        }



}
