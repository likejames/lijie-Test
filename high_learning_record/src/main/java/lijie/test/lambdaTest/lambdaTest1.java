package lijie.test.lambdaTest;

import java.util.function.Consumer;

/**
 * @author : lj
 * @since : 2021/1/5
 * java8 内置的四大核心函数式接口
 * Consumer<T> :消费型接口
 *              void accept(T t);
 * Supplier<T>:　供给型接口
 *　             T get();
 *Function<T,R>:函数型接口
 *              R apply(T t);
 *
 *  Predicate<T>: 断言型接口
 *  boolea test(T t);
 */
public class lambdaTest1 {

    //Consumer<T> 消费型接口
    public static void main(String[] args) {
        happer(1000,m-> System.out.println("消费：　"+m));
    }
    public static void happer(double money, Consumer<Double> consumer){
        consumer.accept(money);
    }
}
