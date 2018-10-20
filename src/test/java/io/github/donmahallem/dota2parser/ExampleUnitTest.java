package io.github.donmahallem.dota2parser;
import okio.BufferedSink;
import okio.Okio;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void addition_isCorrect3() throws IOException {
        FileParser fileParser=new HeroesFileParser("./src/test/npc_heroes.txt");
        fileParser.parse();

    }
    @Test
    public void addition_isCorrect4() throws IOException {
        BufferedSink sink=Okio.buffer(Okio.sink(new File("testoutput.json")));
        FileParser fileParser=new ToJsonFileParser("./src/test/npc_heroes.txt",sink);
        fileParser.parse();
        sink.flush();
        sink.close();

    }
}