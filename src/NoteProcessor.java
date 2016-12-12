import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Siqi Wang siqiw1 on 12/9/16.
 */
public class NoteProcessor {

    public static final SimpleDateFormat EN_DATE_FORMAT = new SimpleDateFormat("EEEE, MMM dd, yyyy HH:mm:ss a");

    private static final Locale CHINA_LOCALE = Locale.CHINA;
    public static final SimpleDateFormat CH_DATE_FORMAT = new SimpleDateFormat("yyyy年MM月dd日EEEE aHH:mm:ss", CHINA_LOCALE);

    public static void main(String[] args) throws IOException {
        List<NoteUnit> noteUnitList = new ArrayList<>();
        NoteProcessor processor = new NoteProcessor();

        processor.readFile(noteUnitList, args[0]);
        Collections.sort(noteUnitList);
        processor.writeFile(processor.aggregateNotesParagraphs(noteUnitList), args[1]);

    }

    private void readFile(List<NoteUnit> noteUnitList, String inputPath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(inputPath)))) {
            // process first note block
            Property property = Property.TITLE;
            String line = reader.readLine();
            NoteUnit newUnit = new NoteUnit();
            newUnit.setTitle(line);

            while ((line = reader.readLine()) != null) {
                property = property.getNextLineProperty();
                switch (property) {
                    case SPLITTER:
                        noteUnitList.add(newUnit);
                        newUnit = new NoteUnit();
                        break;
                    case TITLE:
                        Property.TITLE.processLine(line, newUnit);
                        break;
                    case TIME:
                        Property.TIME.processLine(line, newUnit);
                        break;
                    case CONTENT:
                        Property.CONTENT.processLine(line, newUnit);
                        break;
                }
            }
        }
    }

    private List<NoteUnit> aggregateNotesParagraphs(List<NoteUnit> noteUnitList) {

        List<NoteUnit> aggregatedNotes = new ArrayList<>();
        NoteUnit lastUnit = noteUnitList.get(0);
        Date lastDate = lastUnit.getDate();

        for (int i = 1; i < noteUnitList.size(); i++) {
            NoteUnit curUnit = noteUnitList.get(i);

            // if two notes are made within 5 minutes, aggregate them as one note.
            if (Math.abs(curUnit.getDate().getTime() - lastDate.getTime()) < 300000) {
                lastUnit.setContent(curUnit.getContent());
            } else {
                aggregatedNotes.add(lastUnit);
                lastUnit = curUnit;
            }
            lastDate = curUnit.getDate();
        }
        aggregatedNotes.add(lastUnit);
        return aggregatedNotes;
    }

    // separate notes of each book to oneBookList and aggregate them into allBookList
    private List<List<NoteUnit>> aggregateBooks(List<NoteUnit> noteUnitList) {
        List<List<NoteUnit>> allBookList = new ArrayList<>();
        List<NoteUnit> oneBookList = new ArrayList<>();
        allBookList.add(oneBookList);

        NoteUnit curUnit;
        NoteUnit nextUnit = null;

        for (int i = 0; i < noteUnitList.size() - 1; i++) {
            curUnit = noteUnitList.get(i);
            nextUnit = noteUnitList.get(i + 1);
            oneBookList.add(curUnit);

            if (!curUnit.getTitle().equals(nextUnit.getTitle())) {
                allBookList.add(oneBookList);
                oneBookList = new ArrayList<>();
            }
        }
        oneBookList.add(nextUnit);
        allBookList.add(oneBookList);

        return allBookList;
    }

    private void writeFile(List<NoteUnit> noteUnitList, String outputPath) throws IOException {
        List<List<NoteUnit>> allBookList = aggregateBooks(noteUnitList);

        for (List<NoteUnit> book : allBookList) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(Paths.get(outputPath,
                    book.get(0).getTitle() + ".txt").toString()))) {
                for (NoteUnit noteUnit : book) {
                    writer.write(noteUnit.toString());
                }
            }
        }
    }
}
