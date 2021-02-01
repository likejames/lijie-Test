package lijie.test.testSynchronized;

public class Account {
    // 锁：保护账户余额
    private final Object balLock
            = new Object();
    // 账户余额
    private Integer balance = 1000;
    // 锁：保护账户密码
    private final Object pwLock
            = new Object();
    // 账户密码
    private String password;

    // 取款
    public void withdraw(Integer amt) {
        synchronized (balLock) {
            if (this.balance > amt) {
                this.balance -= amt;
                System.out.println("开始取款");
            }
        }
    }

    // 查看余额
    public Integer getBalance() {
        synchronized (balLock) {
          System.out.println("查看余额");
            return balance;
        }
    }

    // 更改密码
    public void updatePassword(String pw) {
        synchronized (pwLock) {
            this.password = pw;
        }
    }

    // 查看密码
    public String getPassword() {
        synchronized (pwLock) {
            return password;
        }
    }
}