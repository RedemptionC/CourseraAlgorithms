/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;

import java.util.List;

public class Tweeter {
    private Digraph<User> network;

    private class User {
        private int userId;
        private List<post> myPost;
    }

    private class post {
        private int postId;
        private int time;
    }

    public static void main(String[] args) {

    }
}
