package test;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MonalTest {

    @Test
    public void test(){
        Matcher m= Pattern.compile("(?<!\\w)(?=\\w)|(?<=\\w)(?!\\w)\\w+(?<!\\w)(?=\\w)|(?<=\\w)(?!\\w)").matcher("123 sdsdsd 4545");
        System.out.println("m = " + m.find());
        String a="12344546546546465sdsdsd".replaceAll("(?=\\d)(?=(\\d{3})+(?!\\d))",",");
        String b="12344546546546465sdsdsd".replaceAll("(\\d)(?=(\\d{3})+(?!\\d))","$1,");

        System.out.println("a = " + a);
        System.out.println("b = " + b);

        Pattern p=Pattern.compile("\\G(?>(?!44)\\d{5})*(44\\d\\d\\d)");
        String num="03824531449411615213441829505344272752010217443235";
        Matcher m2=p.matcher(num);
        while (m2.find()){
            System.out.println("m2.group() = " + m2.group(1));
        }
    }

    public void test2(){
        List<String> list=new ArrayList<>();
        list.add("1");
        list.add("2");
        boolean flag=list.stream().anyMatch((l)->{
            try{
                return false;
            }catch (Exception e){
                return true;
            }
        });
    }
}
