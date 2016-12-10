/**
 * @author Siqi Wang siqiw1 on 12/9/16.
 */
public enum States {
    TITLE{
        @Override
        String processLine(String line) {
            return line;
        }

        @Override
        States getNextState(States curState) {
            return TIME;
        }
    },
    TIME {
        @Override
        String processLine(String line) {
            String[] tokens = line.split("\\|");

            if (tokens[1].trim().startsWith("Added on")) {
                return tokens[1].substring(10);
            }
            return tokens[1].substring(5);
        }

        @Override
        States getNextState(States curState) {
            return BLANK;
        }
    }, BLANK {
        @Override
        String processLine(String line) {
            return "";
        }

        @Override
        States getNextState(States curState) {
            return CONTENT;
        }
    }, CONTENT {
        @Override
        String processLine(String line) {
            return line;
        }

        @Override
        States getNextState(States curState) {
            return SPLITTER;
        }
    }, SPLITTER {
        @Override
        String processLine(String line) {
            return "\n";
        }

        @Override
        States getNextState(States curState) {
            return TITLE;
        }
    };

    abstract String processLine(String line);

    abstract States getNextState(States curState);
}
