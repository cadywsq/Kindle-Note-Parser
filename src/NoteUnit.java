import java.util.Date;

/**
 * @author Siqi Wang siqiw1 on 12/9/16.
 */
public class NoteUnit implements Comparable<NoteUnit> {
    private String title;
    private Date date;
    private final StringBuilder content;

    public NoteUnit() {
        content = new StringBuilder();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content.toString();
    }

    public void setContent(String content) {
        this.content.append(content);
    }

    @Override
    public String toString() {
        return String.format("%s\n%s\n\n", date, content.toString());
    }

    @Override
    public int compareTo(NoteUnit o) {
        if (this.title.equals(o.title)) {
            return this.date.compareTo(o.date);
        }
        return this.title.compareTo(o.title);
    }
}
