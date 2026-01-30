import java.util.Scanner;
public class KingSIM {
    public static void main(String[] args) {
        System.out.println("____________________________________________________________");
        System.out.println("Hello! I'm KingSIM");
        System.out.println("What can I do for you?");
        System.out.println("____________________________________________________________");
        Scanner ed= new Scanner(System.in);
        while(true) {
            String x = ed.nextLine();
            String y = x.trim().toLowerCase();
            if (y.equals("bye")) {
                System.out.println("____________________________________________________________");
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println("____________________________________________________________");
                break;
            }
            System.out.println("____________________________________________________________");
            System.out.println(x);
            System.out.println("____________________________________________________________");
        }
        ed.close();
    }
}
