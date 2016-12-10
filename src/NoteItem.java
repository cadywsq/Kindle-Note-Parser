/**
 * @author Siqi Wang siqiw1 on 12/9/16.
 */
public class NoteItem implements Comparable<NoteItem> {
    private String title;
    private String time;
    private String content;

    public NoteItem() {

    }

    public NoteItem(String title, String time, String content)  {
        this.setTitle(title);
        this.setTime(time);
        this.setContent(content);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return String.format("%s\n%s\n\n", time, content);
    }

    @Override
    public int compareTo(NoteItem o) {
        if (this.title.equals(o.title)) {
            return this.time.compareTo(o.time);
        }
        return this.title.compareTo(o.title);
    }
}
