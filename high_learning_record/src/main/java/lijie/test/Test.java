package lijie.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * @author : lj
 * @since : 2020/12/18
 */
public class Test {
    public static void main(String[] args) {
        ArrayList arrayList=new ArrayList();
        arrayList.add(22);
        HashMap hashMap=new HashMap();
        while (true){
            Scanner scanner=new Scanner(System.in);
            String siteDomainName=scanner.nextLine();
            boolean iftrue = siteDomainName.matches("[+]{0,1}(\\d){1,3}[ ]?([-]?((\\d)|[ ]){1,12})+$");
            if (iftrue == false) {
                System.out.println("域名不正确");
            }else {
                System.out.println("域名正确");
            }
        }
    }
}
