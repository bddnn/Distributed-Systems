public class Log extends RuntimeException{

    public Log(String message) {
        super(message);
    }

    public static void log(String message) {
        System.out.println(message);
        System.out.flush();
    }
}