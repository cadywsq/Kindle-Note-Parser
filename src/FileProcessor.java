import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Siqi Wang siqiw1 on 12/9/16.
 */
public class FileProcessor {
    private static List<NoteItem> noteItemList = new ArrayList<>();
    private static String filePath;
//    public static SimpleDateFormat enDateFormat = new SimpleDateFormat("EEEE, MMM dd, yyyy HH:mm:ss a");

    public static void main(String[] args) throws IOException {
        filePath = args[1];
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(args[0])))) {
            readFile(reader);
            Collections.sort(noteItemList);
            writeFile(filePath);
        }
    }

    private static void readFile(BufferedReader reader) throws IOException {
        // process first note
        States state = States.TITLE;
        String s = state.processLine(reader.readLine());
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
                    newItem.setTitle(s);
                    break;
                case TIME:
                    newItem.setTime(States.TIME.processLine(s));
                    break;
                case CONTENT:
                    newItem.setContent(s);
                    break;
            }
        }
    }

    private static void writeFile(String filePath) throws IOException {
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
