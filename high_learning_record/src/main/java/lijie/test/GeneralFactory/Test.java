package lijie.test.GeneralFactory;

public class Test {
    public static void main(String[] args) {
        Test.getPerson("men");
    }

    public static Person getPerson(String name) {
        if (name.equals("men")) {
            return new Men();
        } else if (name.equals("women")) {
            return new Women();
        } else {
            System.out.println("输入错误");
            return null;
        }
    }
}
