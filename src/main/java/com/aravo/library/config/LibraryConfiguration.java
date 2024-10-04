package com.aravo.library.config;

import com.aravo.library.data.WorkFormat;
import com.aravo.library.data.entity.*;
import com.aravo.library.data.repository.AuthorRepository;
import com.aravo.library.data.repository.WorkRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@Configuration
public class LibraryConfiguration {
    @Bean
    public CommandLineRunner loadSeedData(
            AuthorRepository authorRepository,
            WorkRepository workRepository) {
        return (args) -> {
            Author mWeis = authorRepository.create(new Author("Margaret", "Weis"));
            Author tHickman = authorRepository.create(new Author("Tracy", "Hickman"));
            Author cSagan = authorRepository.create(new Author("Carl", "Sagan"));
            Author mFowler = authorRepository.create(new Author("Martin", "Fowler"));

            Work dhw = workRepository.create(
                    new Work("Demon Haunted World", LocalDate.of(1995, 1, 1), true));
            dhw.addAuthor(cSagan);
            dhw.addFormat(new AvailableFormat(WorkFormat.TABLET));
            workRepository.save(dhw);

            List<Work> deathGateCycle = Stream.of(
                    new Work("Dragon Wing", LocalDate.of(1990, 2, 1), false),
                    new Work("Elven Star", LocalDate.of(1990, 11, 1), false),
                    new Work("Fire Sea", LocalDate.of(1991, 8, 1), false),
                    new Work("Serpent Mage", LocalDate.of(1992, 4, 1), false),
                    new Work("The Hand of Chaos", LocalDate.of(1993, 4, 1), false),
                    new Work("Into The Labyrinth", LocalDate.of(1993, 12, 1), false),
                    new Work("The Seventh Gate", LocalDate.of(1994, 9, 1), false)
            ).map(workRepository::create).toList();

            int[] volume = { 1 };
            deathGateCycle.forEach(work -> {
                work.addAuthor(mWeis);
                work.addAuthor(tHickman);
                work.addFormat(new AvailableFormat(WorkFormat.SCROLL, BigDecimal.valueOf(5.99)));
                work.setVolumeInfo(new VolumeInfo(volume[0]++, "The Death Gate Cycle"));
            });
            deathGateCycle.get(6).addCitation(new Citation(
                    341, "Fire Sea, vol. 3 of The Death Gate Cycle",
                    "M. Weis", LocalDate.of(1994, 8, 31)));
            deathGateCycle.forEach(workRepository::save);

            Work patternsEAA = workRepository.create(new Work(
                    "Patterns of Enterprise Application Architecture",
                    LocalDate.of(2002, 10, 1), false));
            patternsEAA.addAuthor(mFowler);
            patternsEAA.addFormat(new AvailableFormat(WorkFormat.CODEX, BigDecimal.valueOf(24.99)));
            patternsEAA.addFormat(new AvailableFormat(WorkFormat.TABLET, BigDecimal.valueOf(14.99)));
            patternsEAA.setForward(new Forward("Kevan Simpson", "This is a very good book!"));
            workRepository.save(patternsEAA);
        };
    }
}
            /*
SELECT a.*, w.*
FROM WORKS w
INNER JOIN AUTHOR_WORK_XREF x ON w.ID = x.WORK_ID
INNER JOIN AUTHORS a ON x.AUTHOR_ID = a.ID
             */
