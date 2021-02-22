package br.com.api.functional.stopwords;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import org.apache.commons.text.StringTokenizer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import br.com.api.infrastructure.preprocessing.StopWordService;
import br.com.api.infrastructure.preprocessing.TweetCleanerService;

@SpringBootTest
public class RemoveStopWordsTest {

        @Autowired
        private StopWordService _stopWordService;

        @Autowired
        private TweetCleanerService _tweetCleanerService;

        @Test
        public void shouldRemoveStopWords() throws Exception {
                String text = "ONLY 2HOURS LEFT!! Cast your vote! http://t.co/8tNPSfdgd5 Help make the world a more literate and creative place! @LAReviewofBooks #LA2050";

                // Clean text
                String textCleaned = _tweetCleanerService.setText(text).cleanHashtag()
                                .cleanMentions().cleanUrls().clean().getTextCleaned();

                // Tokenizer the text
                StringTokenizer stringToken = new StringTokenizer();
                stringToken = new StringTokenizer(textCleaned);
                List<String> keywords = stringToken.getTokenList();

                // Remove stop words
                keywords = _stopWordService.removeFromText(keywords).toList();

                assertEquals("hours left cast vote help make world literate creative place",
                                String.join(" ", keywords));
        }
}
