package io.github.donmahallem.dota2parser;

import okio.BufferedSink;
import okio.Okio;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ToJsonFileParserUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void addition_isCorrect3() throws IOException {
        FileParser fileParser=new HeroesFileParser("./npc_heroes.txt");
        fileParser.parse();

    }
    @Test
    public void addition_isCorrect4() throws IOException {
        BufferedSink sink=Okio.buffer(Okio.sink(new File("testoutput.json")));
        FileParser fileParser=new ToJsonFileParser("./npc_heroes.txt",sink);
        fileParser.parse();
        sink.flush();
        sink.close();

    }
}