import java.util.Scanner;
public class KingSIM {
    public static void main(String[] args) {
        System.out.println("____________________________________________________________");
        System.out.println("Hello! I'm KingSIM");
        System.out.println("What can I do for you?");
        System.out.println("____________________________________________________________");
        Scanner ed= new Scanner(System.in);
        String[] list= new String[100];
        int tasks= 0;
        while(true) {
            String x = ed.nextLine();
            String y = x.trim().toLowerCase();
            if (y.equals("bye")) {
                System.out.println("____________________________________________________________");
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println("____________________________________________________________");
                break;
            }
            if(y.equals("list")){
                System.out.println("____________________________________________________________");
                for(int i=0;i<tasks;i++)
                {
                    System.out.println((i+1) + ". " +list[i]);
                }
                System.out.println("____________________________________________________________");
                continue;
            }
            list[tasks++] = x;
            System.out.println("____________________________________________________________");
            System.out.println("added: " + x);
            System.out.println("____________________________________________________________");

        }
        ed.close();
    }
}
