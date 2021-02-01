package lijie.test.reflectClass;

public class ReflectClass {
    public static void main(String[] args) {
        try {
            Class<?> classBook = Class.forName("lijie.test.reflectClass.Book");
            Object objectBook = classBook.newInstance();
            Book book = (Book) objectBook;
            book.setName("进阶之光");
            book.setAge("26");
            System.out.println(book.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
