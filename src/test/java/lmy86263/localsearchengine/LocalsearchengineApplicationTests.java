package lmy86263.localsearchengine;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LocalsearchengineApplicationTests {

    @Test
    public void testNGram(){
        NGram ngram = new NGram("我喜欢你说she love the world");
        System.out.println(String.join(",", ngram.take()));
    }

    @Test
    public void testTakeMixed(){
        NGram ngram = new NGram("i love the world haha");
        System.out.println(String.join(",", ngram.take()));
    }

}
