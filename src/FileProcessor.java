import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Siqi Wang siqiw1 on 12/9/16.
 */
public class FileProcessor {
    private String filePath;
    public static SimpleDateFormat enDateFormat = new SimpleDateFormat("EEEE, MMM dd, yyyy HH:mm:ss a");

    private static Locale locale = Locale.CHINA;
    public static SimpleDateFormat chDateFormat = new SimpleDateFormat("yyyy年MM月dd日EEEE aHH:mm:ss", locale);

    public static void main(String[] args) throws IOException {

        List<NoteItem> noteItemList = new ArrayList<>();
        FileProcessor processor = new FileProcessor();
        processor.filePath = args[1];

        try (BufferedReader reader = new BufferedReader(new FileReader(new File(args[0])))) {
            processor.readFile(reader, noteItemList);
            Collections.sort(noteItemList);
            processor.writeFile(processor.filePath, processor.aggregateNotes(noteItemList));
        }
    }

    private void readFile(BufferedReader reader, List<NoteItem> noteItemList) throws IOException {
        // process first note
        States state = States.TITLE;
        String s = reader.readLine();
        NoteItem newItem = new NoteItem();
        newItem.setTitle(s);

        while ((s = reader.readLine()) != null) {
            state = state.getNextState(state);
            switch (state) {
                case SPLITTER:
                    noteItemList.add(newItem);
                    newItem = new NoteItem();
                    break;
                case TITLE:
                    States.TITLE.processLine(s, newItem);
                    break;
                case TIME:
                    States.TIME.processLine(s, newItem);
                    break;
                case CONTENT:
                    States.CONTENT.processLine(s, newItem);
                    break;
            }
        }
    }

    private List<NoteItem> aggregateNotes(List<NoteItem> noteItemList) {
        List<NoteItem> aggregatedNotes = new ArrayList<>();
        NoteItem lastItem = noteItemList.get(0);
        Date lastDate = lastItem.getDate();
        for (int i = 1; i < noteItemList.size(); i++) {
            NoteItem thisItem = noteItemList.get(i);

            // if two notes are made within 5 minutes, aggregate them as one note.
            if (Math.abs(thisItem.getDate().getTime() - lastDate.getTime()) < 300000) {
                lastItem.setContent(thisItem.getContent());
            } else {
                aggregatedNotes.add(lastItem);
                lastItem = thisItem;
            }
            lastDate = thisItem.getDate();
        }
        aggregatedNotes.add(lastItem);
        return aggregatedNotes;
    }

    private void writeFile(String filePath, List<NoteItem> noteItemList) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath + noteItemList.get(0).getTitle() + ".txt"));

        for (int i = 0; i < noteItemList.size() - 1; i++) {
            NoteItem item = noteItemList.get(i);
            NoteItem nextItem = noteItemList.get(i + 1);
            writer.write(item.toString());

            if (!item.getTitle().equals(nextItem.getTitle())) {
                writer.close();
                writer = new BufferedWriter(new FileWriter(filePath + nextItem.getTitle() + ".txt"));
            }
        }
        writer.write(noteItemList.get(noteItemList.size() - 1).toString());
        writer.close();
    }
}
