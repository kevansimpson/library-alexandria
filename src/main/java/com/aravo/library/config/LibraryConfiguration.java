package com.aravo.library.config;

import com.aravo.library.data.WorkFormat;
import com.aravo.library.data.entity.Author;
import com.aravo.library.data.entity.AvailableFormats;
import com.aravo.library.data.entity.Work;
import com.aravo.library.data.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Configuration
public class LibraryConfiguration {
    @Bean
    public CommandLineRunner loadSeedData(
            AuthorRepository authorRepository,
            AvailableFormatsRepository formatsRepository,
            WorkRepository workRepository) {
        return (args) -> {
            Author mWeis = authorRepository.save(new Author("Margaret", "Weis"));
            Author tHickman = authorRepository.save(new Author("Tracy", "Hickman"));
            Author cSagan = authorRepository.save(new Author("Carl", "Sagan"));
            Author mFowler = authorRepository.save(new Author("Martin", "Fowler"));

            /*
SELECT a.*, w.*
FROM WORKS w
INNER JOIN AUTHOR_WORK_XREF x ON w.ID = x.WORK_ID
INNER JOIN AUTHORS a ON x.AUTHOR_ID = a.ID
             */
            Work dhw = new Work("Demon Haunted World", Date.valueOf("1995-01-01"), true);
            dhw.addAuthor(cSagan);
            dhw.addFormat(new AvailableFormats(WorkFormat.TABLET));
            workRepository.save(dhw);

            List<Work> deathGateCycle = List.of(
                    new Work("Dragon Wing", Date.valueOf("1990-02-01"), false),
                    new Work("Elven Star", Date.valueOf("1990-11-01"), false),
                    new Work("Fire Sea", Date.valueOf("1991-08-01"), false),
                    new Work("Serpent Mage", Date.valueOf("1992-04-01"), false),
                    new Work("The Hand of Chaos", Date.valueOf("1993-04-01"), false),
                    new Work("Into The Labyrinth", Date.valueOf("1993-12-01"), false),
                    new Work("The Seventh Gate", Date.valueOf("1994-09-01"), false)
            );

            deathGateCycle.forEach(work -> {
                work.addAuthor(mWeis);
                work.addAuthor(tHickman);
                work.addFormat(new AvailableFormats(WorkFormat.SCROLL, BigDecimal.valueOf(5.99)));
                workRepository.save(work);
            });

            Work patternsEAA = new Work(
                    "Patterns of Enterprise Application Architecture",
                    Date.valueOf("2002-10-01"), false);
            patternsEAA.addAuthor(mFowler);
            patternsEAA.addFormat(new AvailableFormats(WorkFormat.CODEX, BigDecimal.valueOf(24.99)));
            workRepository.save(patternsEAA);
        };
    }
}
