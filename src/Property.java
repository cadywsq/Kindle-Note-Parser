import java.text.ParseException;

/**
 * @author Siqi Wang siqiw1 on 12/9/16.
 */
public enum Property {
    TITLE {
        @Override
        void processLine(String line, NoteUnit item) {
            item.setTitle(line);
        }

        @Override
        Property getNextLineProperty() {
            return TIME;
        }
    },
    TIME {
        void processLine(String line, NoteUnit item) {
            String[] tokens = line.split("\\|");
            try {
                item.setDate(NoteProcessor.EN_DATE_FORMAT.parse(tokens[1].substring(10)));
            } catch (ParseException e) {
                try {
                    item.setDate(NoteProcessor.CH_DATE_FORMAT.parse(tokens[1].substring(5)));
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }
        }

        @Override
        Property getNextLineProperty() {
            return BLANK;
        }
    }, BLANK {
        @Override
        void processLine(String line, NoteUnit item) {

        }

        @Override
        Property getNextLineProperty() {
            return CONTENT;
        }
    }, CONTENT {
        @Override
        void processLine(String line, NoteUnit item) {
            item.setContent(line);
        }

        @Override
        Property getNextLineProperty() {
            return SPLITTER;
        }
    }, SPLITTER {
        @Override
        void processLine(String line, NoteUnit item) {

        }

        @Override
        Property getNextLineProperty() {
            return TITLE;
        }
    };

    abstract void processLine(String line, NoteUnit item);

    abstract Property getNextLineProperty();
}
