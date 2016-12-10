import java.text.ParseException;

/**
 * @author Siqi Wang siqiw1 on 12/9/16.
 */
public enum States {
    TITLE {
        @Override
        void processLine(String line, NoteItem item) {
            item.setTitle(line);
        }

        @Override
        States getNextState(States curState) {
            return TIME;
        }
    },
    TIME {
        void processLine(String line, NoteItem item) {
            String[] tokens = line.split("\\|");
            try {
                item.setDate(FileProcessor.enDateFormat.parse(tokens[1].substring(10)));
            } catch (ParseException e) {
                try {
                    item.setDate(FileProcessor.chDateFormat.parse(tokens[1].substring(5)));
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }
        }

        @Override
        States getNextState(States curState) {
            return BLANK;
        }
    }, BLANK {
        @Override
        void processLine(String line, NoteItem item) {

        }

        @Override
        States getNextState(States curState) {
            return CONTENT;
        }
    }, CONTENT {
        @Override
        void processLine(String line, NoteItem item) {
            item.setContent(line);
        }

        @Override
        States getNextState(States curState) {
            return SPLITTER;
        }
    }, SPLITTER {
        @Override
        void processLine(String line, NoteItem item) {

        }

        @Override
        States getNextState(States curState) {
            return TITLE;
        }
    };

    abstract void processLine(String line, NoteItem item);

    abstract States getNextState(States curState);
}
