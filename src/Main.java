import util.SkipList;

/**
 * Created by Michał Krasoń.
 */
public class Main {
    public static void main(String[] args) {
        SkipList list = new SkipList();
        char c = 'a';
        for (int i = 0; i < 100; i++) {
            list.insert(i, String.format("%c", c));
            c++;
        }
        list.delete(1);
        list.delete(2);
        System.out.println(list.search(0));
        System.out.println(list.search(1));
    }
}
